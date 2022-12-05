package com.sn.harbinger.ui.auth.register

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.sn.harbinger.R
import com.sn.harbinger.util.State
import com.sn.harbinger.databinding.RegisterFragmentBinding
import com.sn.harbinger.util.extensions.SnackBarType
import com.sn.harbinger.util.extensions.inAnimation
import com.sn.harbinger.util.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val vm: RegisterViewModel by viewModels()

    private val binding by lazy {
        RegisterFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        initObserve()

        binding.tvLogin.setOnClickListener {
            goToLogin()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val errorMsg = (vm.validate(email, pass))

            if (errorMsg.isNotEmpty()) {
                showSnackBar(errorMsg, SnackBarType.ERROR, view)
                return@setOnClickListener
            }

            vm.signupRequest(email, pass)
        }
    }

    private fun initObserve() {
        vm.signupLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {
                    binding.btnRegister.startAnimation()
                }
                is State.Success -> {
                    returnLoginPage()
                }
                is State.Error -> {
                    binding.btnRegister.revertAnimation()
                    if (response.throwable is HttpException) {
                        if (response.throwable.code() == 409) {
                            showSnackBar(
                                getString(R.string.user_already_exists),
                                SnackBarType.ERROR,
                                requireView()
                            )
                        } else {
                            showSnackBar(
                                getString(R.string.server_error),
                                SnackBarType.ERROR,
                                requireView()
                            )
                        }
                    } else {
                        showSnackBar(
                            getString(R.string.unexpected_error),
                            SnackBarType.ERROR,
                            requireView()
                        )
                    }
                }
            }
        }
    }

    private fun returnLoginPage() {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)?.toBitmap()
        val color = Color.parseColor("#FF4971AC")
        icon?.let { bitmap -> binding.btnRegister.doneLoadingAnimation(color, bitmap) }
        goToLogin()
    }

    private fun startAnimation() {
        binding.tilEmail.inAnimation(requireContext())
        binding.tilPassword.inAnimation(requireContext(), 1200)
    }

    private fun goToLogin() {
        val navigator = requireActivity().findNavController(R.id.auth_nav_host)
        navigator.popBackStack()
    }

}