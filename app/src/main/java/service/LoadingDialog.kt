package service

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.jetbrains.handson.mpp.todoapp.R

class LoadingDialog(val activity: Activity) {
    lateinit var dialog: Dialog

    fun showDialog() {
        dialog = Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)

        val gifImageView: ImageView = dialog.findViewById(R.id.custom_loading_imageView)
        val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)

        Glide.with(activity)
            .load(R.drawable.loading)
            .placeholder(R.drawable.loading)
            .centerCrop()
            .crossFade()
            .into(imageViewTarget)

        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}