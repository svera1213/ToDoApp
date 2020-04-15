package com.jetbrains.handson.mpp.todoapp

import android.app.Activity
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
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.jetbrains.handson.mpp.mobile.createApplicationScreenMessage
import com.jetbrains.handson.mpp.todoapp.data.DataController
import helpers.getCurrentDate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val MY_REQUEST_CODE: Int = 7117
    lateinit var providers: List<AuthUI.IdpConfig>

    private val storage = Firebase.storage
    private val userUID = FirebaseAuth.getInstance().currentUser!!.uid
    private val storageRef = storage.reference
    val jsonRef: StorageReference = storageRef.child("tasks$userUID/file.json")

    val saveController = DataController(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.title).text = createApplicationScreenMessage()
        findViewById<TextView>(R.id.text_date_display).text = getCurrentDate()
        task_list.layoutManager = LinearLayoutManager(this)

        val edittext = findViewById<EditText>(R.id.editText)

        providers = listOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        showSignInOptions()

        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled = false
                    showSignInOptions()
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
        saveController.getJsonData(jsonRef, task_list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, ""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_sign_out.isEnabled = true
            } else {
                Toast.makeText(this, ""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .build(), MY_REQUEST_CODE)
    }
}
