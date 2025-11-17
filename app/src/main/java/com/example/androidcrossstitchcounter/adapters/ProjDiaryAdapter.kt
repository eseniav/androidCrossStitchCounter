package com.example.androidcrossstitchcounter.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val onDelete: () -> Unit,
    private val onEdit: () -> Unit
): RecyclerView.Adapter<ProjDiaryAdapter.DiaryViewHolder>()  {

    private suspend fun getTotalCrossDone(projId: Int): Int {
        return diaryDao.getProjEntriesById(projId)
            .sumOf { it.crossQuantity }
    }

    private var deletedItem: ProjDiary? = null
    private fun showEditDialog(position: Int, context: Context) {
        val diaryEntry = diaryNotes.getOrNull(position) ?: return

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Редактировать запись")

        // Создаём View для диалога (можно использовать layout)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_diary, null)

        val editDate = dialogView.findViewById<TextView>(R.id.date)
        val editCross = dialogView.findViewById<EditText>(R.id.editCross)

        // Заполняем текущие значения
        editDate.text = diaryEntry.diary.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        editCross.setText(diaryEntry.diary.crossQuantity.toString())

        builder.setView(dialogView)

        builder.setPositiveButton("Сохранить") { _, _ ->
            // Получаем новые значения
            val newCross = editCross.text.toString().toIntOrNull() ?: return@setPositiveButton


            // Обновляем данные
            lifecycleOwner.lifecycleScope.launch {
                val updatedEntry = diaryEntry.diary.copy(crossQuantity = newCross)
                diaryDao.updateProjDiary(updatedEntry) // предполагаем, что есть такой метод


                // Обновляем список в адаптере
                diaryNotes = diaryNotes.toMutableList().apply {
                    set(position, ProjDiaryEntry(updatedEntry, diaryEntry.done, diaryEntry.remains))
                }
                notifyItemChanged(position)
                onEdit()
            }
        }
        builder.setNegativeButton("Отмена", null)
        builder.show()
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
                    onDelete()
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
                onDelete()
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
