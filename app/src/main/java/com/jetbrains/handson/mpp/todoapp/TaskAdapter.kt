package com.jetbrains.handson.mpp.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.todoapp.data.DataController
import helpers.updateProgressBar
import kotlinx.android.synthetic.main.task_list_item.view.*

class TaskAdapter(val dataController: DataController, val context: Context, val progressbar: ProgressBar, val progresspecent: TextView): RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return dataController.jsonFileData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskType?.text = dataController.jsonFileData[position]
        setCheckBox(holder, position)
        holder.doneTask?.setOnCheckedChangeListener { buttonView, isChecked ->
            dataController.taskCompleted[position] = isChecked
            dataController.setProgress()
            updateProgressBar(dataController.progressPercent, progressbar, progresspecent)
        }
    }

    private fun setCheckBox(holder: ViewHolder, position: Int) {
        if(position <= dataController.taskCompleted.size - 1){
            val boxChecked = dataController.taskCompleted[position]
            holder.doneTask?.setChecked(boxChecked)
            return
        }
        holder.doneTask?.setChecked(false)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val taskType = view.task_card
    val doneTask = view.check_box_task
}