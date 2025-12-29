package com.example.androidcrossstitchcounter.adapters

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.Project
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArchivedProjectAdapter(private var projects: List<Project>,
                             private var projDao: ProjDao,
                             private val lifecycleOwner: LifecycleOwner,
                             private val view: RecyclerView,
                             private val onChange: () -> Unit,
                             private val getProjectList: () -> List<Project>,
                             private val onItemClick: (Project) -> Unit) : ProjectAdapter(projects, projDao, lifecycleOwner,
    view, onChange, getProjectList, onItemClick) {

    override fun showUndoDialog() {
        Snackbar.make(view, "Проект удалён!", Snackbar.LENGTH_LONG)
            .setAction("Отмена"){undoDel()}.show()
    }
    override fun undoDel() {
        savedItem?.let { item ->
            CoroutineScope(Dispatchers.IO).launch {
                projDao.insertProject(item)
                withContext(Dispatchers.Main) {
                    onChange()
                    Toast.makeText(view.context, "Проект восстановлен!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun removeItem(position: Int) {
        synchronized(this) {
            var notes = projects.toMutableList()
            if (position < 0 || position >= projects.size) {
                notes = getProjectList().toMutableList()
            }

            savedItem = notes[position]
            CoroutineScope(Dispatchers.IO).launch {
                projDao.deleteProject(savedItem!!)
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
}