package com.example.androidcrossstitchcounter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.ProjDiaryDao
import com.example.androidcrossstitchcounter.models.Project

class ProjDiaryAdapter(
    private var diaryNotes: List<ProjDiary>,
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
        holder.dateView.text = diaryNote.date
        holder.dayCrossView.text = diaryNote.crossQuantity.toString()

        lifecycleOwner.lifecycleScope.launch {
            try {
                val crossDone = getTotalCrossDone(diaryNote.projId)
                holder.crossDoneView.text = crossDone.toString()

                val project = projDao.getProjectById(diaryNote.projId)
                val remains = project?.let {
                    it.totalCross?.minus(crossDone) ?: 0
                } ?: 0

                holder.remainsView.text = remains.toString()
            } catch (e: Exception) {
                Log.e("ProjDiaryAdapter", "Ошибка при вычислении: ${e.message}")
            }
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
    }

    fun updateDiaryNotes(newDiaryNotes: List<ProjDiary>) {
        diaryNotes = newDiaryNotes
        notifyDataSetChanged()
    }
}
