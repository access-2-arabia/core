package com.a2a.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.a2a.core.R
import com.a2a.core.callbacks.CommunicationListener
import com.a2a.core.databinding.DialogErrorBinding
import com.a2a.core.extensions.viewBinding
import com.google.android.gms.common.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint

class ErrorFragment : DialogFragment() {

  private val binding by viewBinding(DialogErrorBinding::bind)

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_error, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.apply {
           tvErrorMessage.text = arguments?.getString("message")

           btnClose.setOnClickListener{
               (arguments?.getSerializable("callBacks") as CommunicationListener).onExecute()
               dismiss()
           }
       }

    }
}