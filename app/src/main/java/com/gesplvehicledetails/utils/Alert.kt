package com.gespl.bgv.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

class Alert {
    companion object {
        fun showalert(act: Activity, message: String) {
            var alertDialog: AlertDialog = AlertDialog.Builder(act).create()
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(message);
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                "Ok",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    alertDialog.dismiss()
                })
            alertDialog.show()
        }
    }
}