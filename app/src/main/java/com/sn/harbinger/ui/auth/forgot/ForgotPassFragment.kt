package com.sn.harbinger.ui.auth.forgot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.sn.harbinger.databinding.ForgotPassFragmentBinding
import com.sn.harbinger.util.extensions.inAnimation
import com.sn.harbinger.util.extensions.inDownAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassFragment : Fragment() {

    private lateinit var viewModel: ForgotPassViewModel
    private lateinit var binding: ForgotPassFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ForgotPassFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPassViewModel::class.java)
        initObserve()
        startAnimation()

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) viewModel.validate(text.toString())
        }

        binding.btnSendEmail.setOnClickListener {
            viewModel.sendEmail(binding.etEmail.text.toString())
        }

    }

    private fun initObserve() {
        viewModel.emailValidateLiveData.observe(viewLifecycleOwner) {
            binding.btnSendEmail.isEnabled = it
        }
    }

    private fun startAnimation() {
        binding.tilSendEmail.inAnimation(requireContext())
        binding.image.inDownAnimation(requireContext())
    }
}