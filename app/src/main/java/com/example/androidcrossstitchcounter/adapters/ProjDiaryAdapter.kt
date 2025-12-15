package com.example.androidcrossstitchcounter.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.ProjDiaryDao
import com.example.androidcrossstitchcounter.models.ProjDiaryEntry
import com.example.androidcrossstitchcounter.models.Project
import java.time.format.DateTimeFormatter
import com.example.androidcrossstitchcounter.listeners.DoubleTapListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjDiaryAdapter(
    private var diaryNotes: List<ProjDiaryEntry>,
    private val diaryDao: ProjDiaryDao,
    private val projDao: ProjDao,
    private val lifecycleOwner: LifecycleOwner,
    private val view: RecyclerView,
    private val onChange: () -> Unit,
    private val onUpdate: (diaryEntry: ProjDiary, isFinished: Boolean) -> Unit
): RecyclerView.Adapter<ProjDiaryAdapter.DiaryViewHolder>()  {

    private suspend fun getTotalCrossDone(projId: Int): Int {
        return diaryDao.getProjEntriesById(projId)
            .sumOf { it.crossQuantity }
    }

    private var deletedItem: ProjDiary? = null
    var isFinish= false
    private fun showEditDialog(position: Int, context: Context) {
        val diaryEntry = diaryNotes.getOrNull(position) ?: return

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Редактировать запись")

        // Создаём View для диалога (можно использовать layout)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_diary, null)

        val editDate = dialogView.findViewById<TextView>(R.id.date)
        val editCross = dialogView.findViewById<EditText>(R.id.editCross)
        val remainsText = dialogView.findViewById<TextView>(R.id.remains)
        val finishProj = dialogView.findViewById<LinearLayout>(R.id.finishProj)
        val finishCheck = dialogView.findViewById<CheckBox>(R.id.finishCheck)
        // Заполняем текущие значения
        editDate.text = diaryEntry.diary.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        editCross.setText(diaryEntry.diary.crossQuantity.toString())

        val remains = diaryNotes.first().remains
        var dialog: AlertDialog? = null
        fun handleRemains(s: Editable?) {
            remainsText.visibility = View.GONE
            finishProj.visibility = View.GONE
            finishCheck.isChecked = false
            isFinish = false
            dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = true
            val newCrossEdit = s.toString().toIntOrNull()
            var newRemains: Int?
            if (remains != null && newCrossEdit != null) {
                newRemains = remains - newCrossEdit + diaryEntry.diary.crossQuantity
                dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false
                if (newRemains < 0) {
                    remainsText.visibility = View.VISIBLE
                } else if (newRemains == 0) {
                    finishProj.visibility = View.VISIBLE
                } else {
                    dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = true
                }
            }
        }

        if (remains != null) {
            editCross.addTextChangedListener(
                object : TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
                        handleRemains(s)
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }
                }
            )
        }
        builder.setView(dialogView)

        builder.setPositiveButton("Сохранить") { _, _ ->
            // Получаем новые значения
            val newCross = editCross.text.toString().toIntOrNull() ?: return@setPositiveButton
            val updatedEntry = diaryEntry.diary.copy(crossQuantity = newCross)
            onUpdate(updatedEntry, isFinish)
        }

        builder.setNegativeButton("Отмена", null)
        dialog = builder.create()
        finishCheck.setOnCheckedChangeListener {_, isChecked ->
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isChecked
            isFinish = isChecked
        }
        dialog.show()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.proj_diary_item, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DiaryViewHolder,
        position: Int
    ) {
        val diaryNote = diaryNotes[position]
        holder.dateView.text = diaryNote.diary.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        holder.dayCrossView.text = diaryNote.diary.crossQuantity.toString()
        holder.crossDoneView.text = diaryNote.done.toString()
        holder.remainsView.text = diaryNote.remains.toString()
        holder.itemView.setOnTouchListener(DoubleTapListener(holder.adapterPosition) { position ->
            showEditDialog(position, holder.itemView.context)
            //showDialog(holder.itemView.context, position)
            Log.d("DoubleTap", "Двойной клик по элементу!")
        })
    }

    override fun getItemCount(): Int {
        return diaryNotes.size
    }

    fun undoDel() {
        deletedItem?.let { item ->
            CoroutineScope(Dispatchers.IO).launch {
                diaryDao.insertProjDiary(item)
                withContext(Dispatchers.Main) {
                    onChange()
                    Toast.makeText(lifecycleOwner as Context, "Запись восстановлена!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun showUndoDialog() {
        Snackbar.make(view, "Запись удалена!", Snackbar.LENGTH_LONG)
            .setAction("Отмена"){undoDel()}.show()
    }

    fun removeItem(position: Int) {
        val notes = diaryNotes.toMutableList()
        deletedItem = notes[position].diary
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.deleteEntry(deletedItem!!)
            withContext(Dispatchers.Main) {
                notes.removeAt(position)
                diaryNotes = notes
                notifyItemRemoved(position)
                onChange()
                showUndoDialog()
            }
        }
    }
    inner class DiaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.date)
        val dayCrossView: TextView = itemView.findViewById(R.id.dayCross)
        val crossDoneView: TextView = itemView.findViewById(R.id.crossDone)
        val remainsView: TextView = itemView.findViewById(R.id.remains)
    }

    fun updateDiaryNotes(newDiaryNotes: List<ProjDiaryEntry>) {
        diaryNotes = newDiaryNotes
        notifyDataSetChanged()
    }
}
