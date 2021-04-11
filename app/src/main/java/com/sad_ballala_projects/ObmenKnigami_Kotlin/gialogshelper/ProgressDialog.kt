package com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper

import android.app.Activity
import android.app.AlertDialog
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ProgressDialogLayoutBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.SignDialogBinding

object ProgressDialog {

    fun createProgressDialog(act: Activity): AlertDialog{
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)

        dialog.show();
        return dialog
    }

}