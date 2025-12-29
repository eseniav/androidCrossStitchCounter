package com.example.androidcrossstitchcounter.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrossstitchcounter.R
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

    fun showDeleteConfirm() {
        Snackbar.make(view, "Проект удалён!", Snackbar.LENGTH_LONG).show()
    }
    override fun undoDel() {
        onChange()
    }
    private  fun showDialog(position: Int, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Удаление проекта")

        // Создаём View для диалога (можно использовать layout)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirm_delete, null)

        builder.setView(dialogView)

        builder.setPositiveButton("Удалить") { _, _ ->
            deleteProject(position)
        }

        builder.setNegativeButton("Отмена"){ _, _ ->
            undoDel()
        }
        val dialog = builder.create()
        dialog.show()
    }
    fun deleteProject(position: Int) {
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
                    showDeleteConfirm()
                }
            }
        }
    }
    override fun removeItem(position: Int) {
        showDialog(position, view.context)

    }
}