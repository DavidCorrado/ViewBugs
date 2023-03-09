package com.example.viewbugs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var error: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            lifecycleScope.launch {
                val dialog = AlertUtil.getLoadingDialog(this@MainActivity)
                dialog.show()
                delay(500L)
                dialog.dismiss()
                // It seems that with a dialog there is a bug that when you pop back there is no talkback focus.  This causes when you try to show an error to not be read but be interrupted by focus
                // We work around the issue by setting focus and waiting a bit to allow settling and then update error text
                findViewById<Button>(R.id.saveButton).isFocusableInTouchMode = true
                findViewById<Button>(R.id.saveButton).requestFocus()
                delay(500L)
                error = !error
                findViewById<TextInputLayout>(R.id.firstNameInputLayout).showError(error)
            }
        }
    }
}

fun TextInputLayout.showError(
    showError: Boolean = true
) {
    if (showError) {
        this.error = "this field has an error"
    } else {
        this.error = null
    }
}

object AlertUtil {
    fun getLoadingDialog(context: Context): androidx.appcompat.app.AlertDialog {
        return MaterialAlertDialogBuilder(context).setView(View(context)).create()
    }
}
