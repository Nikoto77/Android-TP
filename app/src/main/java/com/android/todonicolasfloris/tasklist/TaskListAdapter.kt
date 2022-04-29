package com.android.todonicolasfloris.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.todonicolasfloris.R


object TaskDiffCallBack : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id // comparison: are they the same "entity" ? (usually same id)
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.equals(newItem)// comparison: are they the same "content" ? (simplified for data class)
    }
}

class TaskListAdapter : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(TaskDiffCallBack) {


    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val textViewTitle = itemView.findViewById<TextView>(R.id.task_title)
            textViewTitle.text = task.title
            val textViewDescription = itemView.findViewById<TextView>(R.id.task_description)
            textViewDescription.text = task.description
            val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
            imageButton.setOnClickListener { onClickDelete(task) }
            val editButton = itemView.findViewById<ImageButton>(R.id.editButton)
            editButton.setOnClickListener { onClickEdit(task) }



        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
       return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}