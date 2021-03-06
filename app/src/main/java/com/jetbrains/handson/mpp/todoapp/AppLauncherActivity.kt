package com.jetbrains.handson.mpp.todoapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import service.MessageDialog


const val EXTRA_MESSAGE = "Launching Main Activity"

class AppLauncherActivity : AppCompatActivity() {
    val MY_REQUEST_CODE: Int = 7117
    lateinit var providers: List<AuthUI.IdpConfig>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_launcher)


        providers = listOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        showSignInOptions()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                checkIfEmailVerified(user!!)
            } else {
                Toast.makeText(this, ""+response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .build(), MY_REQUEST_CODE)
    }

    private fun checkIfEmailVerified( user: FirebaseUser) {
        if(user.isEmailVerified){
            Toast.makeText(this, ""+user.email, Toast.LENGTH_SHORT).show()
            val editText = findViewById<TextView>(R.id.title_launcher)
            val message = editText.text.toString()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
        else{
            val messageDialog = MessageDialog(this)
            messageDialog.showMessage("Verification email needed. Please verify account. Email will be sent")
            sendVerificationEmail(user)
        }
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(OnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, this::class.java).apply {
                            putExtra(EXTRA_MESSAGE, "Welcome")
                        }
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        overridePendingTransition(0,0)
                        finish()
                        overridePendingTransition(0,0)
                        val intent = Intent(this, this::class.java).apply {
                            putExtra(EXTRA_MESSAGE, "Error when sending Email")
                        }
                        startActivity(intent)
                    }
                }
            })
    }
}
