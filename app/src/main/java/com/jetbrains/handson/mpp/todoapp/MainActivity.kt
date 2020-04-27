package com.jetbrains.handson.mpp.todoapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jetbrains.handson.mpp.mobile.createApplicationScreenMessage
import com.jetbrains.handson.mpp.todoapp.data.DataController
import helpers.getCurrentDate
import kotlinx.android.synthetic.main.activity_main.*
import service.LoadingDialog

class MainActivity : AppCompatActivity() {

    val database = Firebase.database

    val saveController = DataController(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.title).text = createApplicationScreenMessage()
        findViewById<TextView>(R.id.text_date_display).text = getCurrentDate()
        task_list.layoutManager = LinearLayoutManager(this)

        val loadingDialog = LoadingDialog(this)

        val userUID = FirebaseAuth.getInstance().currentUser!!.uid

        val myRef: DatabaseReference = database.getReference("/users")


        val edittext = findViewById<EditText>(R.id.editText)
        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val progresspecent = findViewById<TextView>(R.id.progressPercent)
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
            saveController.addTask(edittext, task_list, progressbar, progresspecent)
        }

        btn_save.setOnClickListener {
            loadingDialog.showDialog()
            val handler = Handler()
            handler.postDelayed({
                saveController.saveData(myRef, userUID)
                loadingDialog.hideDialog()
            },5000)
        }

        btn_delete.setOnClickListener {
            loadingDialog.showDialog()
            val handler = Handler()
            handler.postDelayed({
                saveController.deleteData(myRef, userUID, task_list, progressbar, progresspecent)
                loadingDialog.hideDialog()
            },5000)
        }
    }

    override fun onResume() {
        super.onResume()

        val userUID = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef: DatabaseReference = database.getReference("/users")

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val progresspecent = findViewById<TextView>(R.id.progressPercent)

        saveController.getData(myRef, userUID, task_list, progressbar, progresspecent)
    }
}
