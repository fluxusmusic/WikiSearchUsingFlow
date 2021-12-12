package com.mc.kim.test.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mc.kim.test.R
import com.mc.kim.test.toolkit.KEY_DESCRIPTION
import com.mc.kim.test.toolkit.KEY_TITLE

class NoticeDialogFragment : DialogFragment(R.layout.dialog_notice) {

    private lateinit var title: String
    private lateinit var description: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(KEY_TITLE, "")
            description = it.getString(KEY_DESCRIPTION, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TransparentWindowDialogTheme)
        val window: Window? = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setGravity(Gravity.CENTER)
        dialog.setOnDismissListener(this)
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleView: TextView = view.findViewById(R.id.title)
        val descriptionView: TextView = view.findViewById(R.id.description)
        val dialogButton: TextView = view.findViewById(R.id.dialogButton)

        titleView.text = title
        descriptionView.text = description
        dialogButton.setOnClickListener {
            dismiss()
        }

    }


    companion object {
        val CLASS_NAME = NoticeDialogFragment::class.simpleName
    }

}