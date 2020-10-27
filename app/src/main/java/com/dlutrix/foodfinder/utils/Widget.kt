package com.dlutrix.foodfinder.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.dlutrix.foodfinder.R

/**
 * w0rm1995 on 16/10/20.
 * risfandi@dlutrix.com
 */
object Widget {

    fun customDialog(
        context: Context,
        yes: () -> Unit,
        buttonText: String,
        message: String,
        isCancelable: Boolean,
    ): CFAlertDialog.Builder {
        return if (!isCancelable) {
            CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Oops!")
                .setCancelable(false)
                .setMessage(message)
                .addButton(
                    buttonText,
                    Color.BLACK,
                    ContextCompat.getColor(context, R.color.colorAccent),
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, _ ->
                    yes()
                    dialog.dismiss()
                }
        } else {
            CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Oops!")
                .setCancelable(true)
                .setMessage(message)
                .addButton(
                    buttonText,
                    Color.BLACK,
                    ContextCompat.getColor(context, R.color.colorAccent),
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, _ ->
                    yes()
                    dialog.dismiss()
                }
        }
    }
}
