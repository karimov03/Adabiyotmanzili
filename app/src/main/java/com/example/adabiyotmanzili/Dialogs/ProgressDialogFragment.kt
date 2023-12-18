package com.example.adabiyotmanzili.Dialogs

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ProgressDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progressDialog = ProgressDialog(activity)
        progressDialog.isIndeterminate = false
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Yuklanmoqda...")
        progressDialog.setCancelable(false)
        return progressDialog
    }
}
