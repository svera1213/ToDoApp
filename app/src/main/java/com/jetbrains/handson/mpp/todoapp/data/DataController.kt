package com.jetbrains.handson.mpp.todoapp.data

import android.content.Context
import android.net.Uri
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import com.jetbrains.handson.mpp.todoapp.TaskAdapter
import helpers.createJson
import helpers.readJson
import java.io.File

class DataController(val context: Context) {

    var jsonFileData: MutableList<String> = mutableListOf()

    fun saveJsonData(jsonRef: StorageReference) {
        val file = File(context.cacheDir, "fileName")
        val jsonDataString = createJson(1,"MyList", "CurrentUser", jsonFileData)
        file.writeText(jsonDataString)

        val uriFile = Uri.fromFile(file)
        println(jsonDataString)
        val uploadTask = jsonRef.putFile(uriFile)

        uploadTask.addOnFailureListener {
            println("Error on upload")
        }.addOnSuccessListener {
            println("Upload Completed!!")
        }
    }

    fun getJsonData(jsonRef: StorageReference, task_list: RecyclerView){
        val localFile = File.createTempFile("taskList", ".json")
        jsonRef.getFile(localFile).addOnSuccessListener {
            println("File downloaded!")
            val data = readJson(localFile)
            jsonFileData = data.toMutableList()
            task_list.adapter = TaskAdapter(jsonFileData, context)
        }.addOnFailureListener {
            println("Error in download")
        }
    }

    fun addTask(edit_text: EditText, task_list: RecyclerView)  {
        jsonFileData.add(edit_text.text.toString())
        task_list.adapter = TaskAdapter(jsonFileData, context)
        edit_text.text.clear()
    }

    fun deleteJsonData(jsonRef: StorageReference, task_list: RecyclerView){
        jsonFileData = mutableListOf()
        task_list.adapter = TaskAdapter(jsonFileData, context)

        jsonRef.delete().addOnSuccessListener {
            println("JsonFile Deleted !!")
        }.addOnFailureListener {
            println("Error on delete !!")
        }
    }
}