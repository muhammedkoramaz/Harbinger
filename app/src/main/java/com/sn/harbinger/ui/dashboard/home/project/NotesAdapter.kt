package com.sn.harbinger.ui.dashboard.home.project

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sn.harbinger.R
import com.sn.harbinger.data.model.Note
import com.sn.harbinger.data.model.Priority
import com.sn.harbinger.databinding.ItemTaskBinding

class NotesAdapter(val context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val items: MutableList<Note> = mutableListOf()

    var onclick: (Note) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(bills: List<Note>) {
        items.clear()
        items.addAll(bills)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.item = item
            binding.imgPriority.setImageResource(getPriorityIcon(item.priority))
        }
    }

    private fun getPriorityIcon(priority: String): Int {
        return when (priority) {
            Priority.LOW.priority -> {
                R.drawable.ic_low
            }
            Priority.MEDIUM.priority -> {
                R.drawable.ic_medium
            }
            Priority.HIGH.priority -> {
                R.drawable.ic_high
            }
            else -> R.drawable.ic_low
        }
    }
    fun deleteNote(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

}