package com.example.androidcrossstitchcounter.listeners

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrossstitchcounter.adapters.ProjDiaryAdapter

interface SwipeableAdapter<T> {
    fun removeItem(position: Int)

}
class SwipeToDeleteCallback<T : SwipeableAdapter<T>>(
    private val adapter: T
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeItem(viewHolder.adapterPosition)
    }
}
