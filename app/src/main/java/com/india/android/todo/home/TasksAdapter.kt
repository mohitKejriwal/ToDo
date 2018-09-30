package com.india.android.todo.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.india.android.todo.ListClickListener
import com.india.android.todo.R
import com.india.android.todo.TaskStatus
import com.india.android.todo.ToDo

/**
 * Created by admin on 30-09-2018.
 */
class TasksAdapter(var dataList:ArrayList<ToDo>, var listener:ListClickListener):RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_tasks,parent,false),listener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.tvTitle.text=dataList[position].title
        if (dataList[position].status==TaskStatus.HOLD.status){
            holder.tvStatus.text="Status: On Hold"
        }else if (dataList[position].status==TaskStatus.PROGRESS.status){
            holder.tvStatus.text="Status: In Progress"
        }else{
            holder.tvStatus.text="Status: Completed"
        }
    }


    class TasksViewHolder(itemView: View,listener:ListClickListener):RecyclerView.ViewHolder(itemView){
        var tvTitle:TextView = itemView.findViewById(R.id.tasksTitle)
        var tvStatus:TextView = itemView.findViewById(R.id.taskStatus)

        init {
            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}