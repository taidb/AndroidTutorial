package com.example.androidtutorial.activity.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StartDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Thông báo").setPositiveButton("Ok") { _, _ ->
                //Start
            }.setNegativeButton("Hủy") { _, _ ->
                //Hủy
            }
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }
}
