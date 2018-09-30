package com.india.android.todo.details_todo

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
 * Created by admin on 01-10-2018.
 */



class CommentsAdapter(var dataList:ArrayList<String>): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_tasks,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.tvTitle.text=dataList[position]
    }


    class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvTitle: TextView = itemView.findViewById(R.id.tasksTitle)
        var tvStatus: TextView = itemView.findViewById(R.id.taskStatus)
        init {
            tvStatus.visibility=View.GONE
        }
    }
}