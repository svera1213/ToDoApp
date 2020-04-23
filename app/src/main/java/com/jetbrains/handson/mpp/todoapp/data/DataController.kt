package com.jetbrains.handson.mpp.todoapp.data

import android.content.Context
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.jetbrains.handson.mpp.todoapp.TaskAdapter
import com.jetbrains.handson.mpp.todoapp.json.PostJson
import helpers.createJsonData

class DataController(val context: Context) {

    var jsonFileData: MutableList<String> = mutableListOf()

    fun saveData(dataRef: DatabaseReference, userUID: String) {
        val jsonData = createJsonData(1,"MyList", userUID, jsonFileData)
        try {
            dataRef.child(userUID).setValue(jsonData)
        }
        catch (e: Exception){
            println(e)
        }
    }

    fun getData(dataRef: DatabaseReference, userUID: String, task_list: RecyclerView){
        dataRef.child(userUID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<PostJson>()

                try {
                    jsonFileData = value!!.postTag!!.toMutableList()
                    task_list.adapter = TaskAdapter(jsonFileData, context)
                    println("Data Saved !!")
                }
                catch (e: Exception){
                    println(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }
        })
    }

    fun addTask(edit_text: EditText, task_list: RecyclerView)  {
        jsonFileData.add(edit_text.text.toString())
        task_list.adapter = TaskAdapter(jsonFileData, context)
        edit_text.text.clear()
    }

    fun deleteData(dataRef: DatabaseReference, userUID: String, task_list: RecyclerView){
        jsonFileData = mutableListOf()
        task_list.adapter = TaskAdapter(jsonFileData, context)
        val jsonData = createJsonData(1,"MyList", userUID, jsonFileData)
        try {
            dataRef.child(userUID).setValue(jsonData)
            println("Tasks Deleted !!")
        }
        catch (e: Exception){
            println(e)
        }
    }
}