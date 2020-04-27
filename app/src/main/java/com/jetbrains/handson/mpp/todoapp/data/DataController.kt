package com.jetbrains.handson.mpp.todoapp.data

import android.content.Context
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.jetbrains.handson.mpp.todoapp.TaskAdapter
import com.jetbrains.handson.mpp.todoapp.json.PostJson
import helpers.createJsonData
import helpers.updateProgressBar
import service.MessageDialog

class DataController(val context: Context) {

    var jsonFileData: MutableList<String> = mutableListOf()
    var taskCompleted: MutableList<Boolean> = mutableListOf()
    var progressPercent: Double = 0.0

    fun saveData(dataRef: DatabaseReference, userUID: String) {
        val jsonData = createJsonData(1,"MyList", userUID, jsonFileData, progressPercent, taskCompleted)
        try {
            dataRef.child(userUID).setValue(jsonData)
            println("Data Saved !!")
        }
        catch (e: Exception){
            val messageDialog = MessageDialog(context)
            messageDialog.showMessage(e.toString())
        }
    }

    fun getData(dataRef: DatabaseReference, userUID: String, task_list: RecyclerView, progressbar: ProgressBar, progresspecent: TextView){
        dataRef.child(userUID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<PostJson>()

                try {
                    if(value?.postTag != null){
                        jsonFileData = value.postTag!!.toMutableList()
                        val savedTaskDone = value.postTaskDone!!.toMutableList()
                        resetTaskUndone(savedTaskDone)
                        setProgress()
                        updateProgressBar(progressPercent, progressbar, progresspecent)

                        task_list.adapter = TaskAdapter( this@DataController,  context, progressbar, progresspecent)
                        println("Data Loaded !!")
                    }
                }
                catch (e: Exception){
                    val messageDialog = MessageDialog(context)
                    messageDialog.showMessage(e.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }
        })
    }

    fun addTask(edit_text: EditText, task_list: RecyclerView, progressbar: ProgressBar, progresspecent: TextView)  {
        val dataInput = edit_text.text.toString().trim()

        if(dataInput.isNotEmpty() && !dataInput.contains("\\n".toRegex())){
            jsonFileData.add(dataInput)
            taskCompleted.add(false)
            setProgress()
            updateProgressBar(progressPercent, progressbar, progresspecent)
            task_list.adapter = TaskAdapter( this@DataController, context, progressbar, progresspecent)
        }
        edit_text.text.clear()
    }

    fun deleteData(dataRef: DatabaseReference, userUID: String, task_list: RecyclerView, progressbar: ProgressBar, progresspecent: TextView){
        jsonFileData = mutableListOf()
        task_list.adapter = TaskAdapter( this@DataController, context, progressbar, progresspecent)
        taskCompleted = mutableListOf()
        progressPercent = 0.0

        val jsonData = createJsonData(1,"MyList", userUID, jsonFileData, progressPercent, taskCompleted)
        try {
            dataRef.child(userUID).setValue(jsonData)
            updateProgressBar(progressPercent, progressbar, progresspecent)
            println("Tasks Deleted !!")
        }
        catch (e: Exception){
            val messageDialog = MessageDialog(context)
            messageDialog.showMessage(e.toString())
        }
    }

    fun setProgress(){
        var tasksDone = 0
        for(task in taskCompleted){
            if(task) {
                tasksDone++
            }
        }
        progressPercent = tasksDone.toDouble() / jsonFileData.size.toDouble()
    }

    private fun resetTaskUndone(savedTaskDone: MutableList<Boolean>) {
        taskCompleted = mutableListOf()
        for(i in 1 .. jsonFileData.size) {
            taskCompleted.add(false)
        }

        for((index, task) in savedTaskDone.withIndex()) {
            if(task){ taskCompleted[index] = task }
        }
    }
}