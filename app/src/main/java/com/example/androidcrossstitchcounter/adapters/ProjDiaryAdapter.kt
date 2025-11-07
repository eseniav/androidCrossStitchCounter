package com.example.androidcrossstitchcounter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DiaryViewHolder,
        position: Int
    ) {
        val diaryNote = diaryNotes[position]
        holder.dateView.text = diaryNote.diary.date
        holder.dayCrossView.text = diaryNote.diary.crossQuantity.toString()
        holder.crossDoneView.text = diaryNote.done.toString()
        holder.remainsView.text = diaryNote.remains.toString()
        holder.editCrossView.setText(diaryNote.diary.crossQuantity.toString())
        holder.dayCrossView.setOnClickListener {
            holder.dayCrossView.visibility = View.GONE
            holder.editCrossView.visibility = View.VISIBLE
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
    }

    fun updateDiaryNotes(newDiaryNotes: List<ProjDiaryEntry>) {
        diaryNotes = newDiaryNotes
        notifyDataSetChanged()
    }
}
