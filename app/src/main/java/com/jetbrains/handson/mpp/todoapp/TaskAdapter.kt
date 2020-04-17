package com.jetbrains.handson.mpp.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_list_item.view.*

class TaskAdapter(val data: MutableList<String>, val context: Context): RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskType?.text = data[position]
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val taskType = view.task_card
}