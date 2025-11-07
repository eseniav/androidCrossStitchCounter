package com.example.androidcrossstitchcounter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.ProjDiaryDao
import com.example.androidcrossstitchcounter.models.ProjDiaryEntry
import com.example.androidcrossstitchcounter.models.Project
import java.time.format.DateTimeFormatter
import com.example.androidcrossstitchcounter.listeners.DoubleTapListener

class ProjDiaryAdapter(
    private var diaryNotes: List<ProjDiaryEntry>,
    private val diaryDao: ProjDiaryDao,
    private val projDao: ProjDao,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ProjDiaryAdapter.DiaryViewHolder>()  {

    private suspend fun getTotalCrossDone(projId: Int): Int {
        return diaryDao.getProjEntriesById(projId)
            .sumOf { it.crossQuantity }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.proj_diary_item, parent, false)
        view.setOnTouchListener(DoubleTapListener {
            // Ваша логика при двойном клике
            Log.d("DoubleTap", "Двойной клик по элементу!")
            // Здесь можно, например:
            // - открыть диалог редактирования
            // - перейти на другой экран
            // - изменить состояние элемента
        })
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
        holder.editCrossView.setText(diaryNote.diary.crossQuantity.toString())
        fun changeVisibility(isEdit: Boolean) {
            if(isEdit) {
                holder.dayCrossView.visibility = View.GONE
                holder.editLayoutView.visibility = View.VISIBLE
            } else {
                holder.dayCrossView.visibility = View.VISIBLE
                holder.editLayoutView.visibility = View.GONE
            }
        }
        holder.dayCrossView.setOnClickListener {
            changeVisibility(true)
        }
        holder.imageCheckView.setOnClickListener {
            changeVisibility(false)
        }
        holder.imageCancelView.setOnClickListener {
            changeVisibility(false)
        }
    }

    override fun getItemCount(): Int {
        return diaryNotes.size
    }

    inner class DiaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.date)
        val dayCrossView: TextView = itemView.findViewById(R.id.dayCross)
        val crossDoneView: TextView = itemView.findViewById(R.id.crossDone)
        val remainsView: TextView = itemView.findViewById(R.id.remains)
        val editCrossView: EditText = itemView.findViewById(R.id.editCross)
        val editLayoutView: LinearLayout = itemView.findViewById(R.id.editLayout)
        val imageCheckView: ImageView = itemView.findViewById(R.id.imageCheck)
        val imageCancelView: ImageView = itemView.findViewById(R.id.imageCancel)
    }

    fun updateDiaryNotes(newDiaryNotes: List<ProjDiaryEntry>) {
        diaryNotes = newDiaryNotes
        notifyDataSetChanged()
    }
}
