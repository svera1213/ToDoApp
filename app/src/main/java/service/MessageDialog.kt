package service

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class MessageDialog(activity: Context) {
    val builder = AlertDialog.Builder(activity)

    fun showMessage(message: String) {

        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })

        val alert = builder.create()
        alert.setTitle("App Message")
        alert.show()
    }
}