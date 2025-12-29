package com.example.androidcrossstitchcounter.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.listeners.SwipeableAdapter
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.Project
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class ProjectAdapter(private var projects: List<Project>,
                     private var projDao: ProjDao,
                     private val lifecycleOwner: LifecycleOwner,
                     private val view: RecyclerView,
                     private val onChange: () -> Unit,
                     private val getProjectList: () -> List<Project>,
                     private val onItemClick: (Project) -> Unit):
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>(), SwipeableAdapter<ProjectAdapter> {
        override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.proj_item, parent, false)
        return ProjectViewHolder(view)
    }

    private var archivedItem: Project? = null

    fun showUndoDialog() {
        Snackbar.make(view, "Проект удален!", Snackbar.LENGTH_LONG)
            .setAction("Отмена"){undoDel()}.show()
    }
    fun undoDel() {
        archivedItem?.let { item ->
            CoroutineScope(Dispatchers.IO).launch {
                projDao.restoreProject(item.id)
                withContext(Dispatchers.Main) {
                    onChange()
                    Toast.makeText(view.context, "Проект восстановлен!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onBindViewHolder(
        holder: ProjectViewHolder,
        position: Int
    ) {
        val project = projects[position]
        holder.sizeView.text = "${project.width} X ${project.height}"
        holder.startDateView.text = project.startDate
        holder.planDateView.text = if(project.projStatusId == 3) project.finishDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            else project.finishDreamDate
        holder.stitchedView.text = project.stitchedCrossBeforeRegistration.toString()
        holder.projNameView.text = project.projName
        if(project.projStatusId == 1) {
            holder.startDateView.visibility = View.GONE
            holder.stitchedView.visibility = View.GONE
        }
        holder.projNameView.setOnClickListener {
            onItemClick(project)
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    override fun removeItem(position: Int) {
        synchronized(this) {
            var notes = projects.toMutableList()
            if (position < 0 || position >= projects.size) {
                notes = getProjectList().toMutableList()
            }

            archivedItem = notes[position]
            CoroutineScope(Dispatchers.IO).launch {
                projDao.archiveProject(archivedItem?.id!!)
                withContext(Dispatchers.Main) {
                    notes.removeAt(position)
                    projects = notes
                    notifyItemRemoved(position)
                    onChange()
                    showUndoDialog()
                }
            }
        }
    }
    inner class ProjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sizeView: TextView = itemView.findViewById<TextView>(R.id.size)
        val startDateView: TextView = itemView.findViewById<TextView>(R.id.startDate)
        val planDateView: TextView = itemView.findViewById<TextView>(R.id.planDate)
        val stitchedView: TextView = itemView.findViewById<TextView>(R.id.stitched)
        val projNameView: TextView = itemView.findViewById<TextView>(R.id.projName)
    }

    fun updateProjects(newProjects: List<Project>) {
        synchronized(this) {
            projects = newProjects
            notifyDataSetChanged()
        }
    }
}
