package com.example.androidcrossstitchcounter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.models.Project

class ProjectAdapter(private var projects: List<Project>):
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.proj_item, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProjectViewHolder,
        position: Int
    ) {
        val project = projects[position]
        holder.titleView.text = project.projName
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    inner class ProjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById<TextView>(R.id.nameProj)
    }

    fun updateProjects(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}
