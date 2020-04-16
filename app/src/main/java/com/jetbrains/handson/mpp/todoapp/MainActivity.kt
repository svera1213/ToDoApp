package com.jetbrains.handson.mpp.todoapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.jetbrains.handson.mpp.mobile.createApplicationScreenMessage
import com.jetbrains.handson.mpp.todoapp.data.DataController
import helpers.getCurrentDate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    val saveController = DataController(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.title).text = createApplicationScreenMessage()
        findViewById<TextView>(R.id.text_date_display).text = getCurrentDate()
        task_list.layoutManager = LinearLayoutManager(this)

        val userUID = FirebaseAuth.getInstance().currentUser!!.uid
        val jsonRef: StorageReference = storageRef.child("tasks$userUID/file.json")

        val edittext = findViewById<EditText>(R.id.editText)
        btn_sign_out.isEnabled = true

        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled = false
                    val message = btn_sign_out.text.toString()
                    val intent = Intent(this, AppLauncherActivity::class.java).apply {
                        putExtra(EXTRA_MESSAGE, message)
                    }
                    startActivity(intent)
                }
                .addOnFailureListener {
                        e-> Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
        }

        btn_add.setOnClickListener {
            saveController.addTask(edittext, task_list)
        }

        btn_save.setOnClickListener {
            saveController.saveJsonData(jsonRef)
        }

        btn_delete.setOnClickListener {
            saveController.deleteJsonData(jsonRef, task_list)
        }
    }

    override fun onResume() {
        super.onResume()

        val userUID = FirebaseAuth.getInstance().currentUser!!.uid
        val jsonRef: StorageReference = storageRef.child("tasks$userUID/file.json")
        saveController.getJsonData(jsonRef, task_list)
    }
}
