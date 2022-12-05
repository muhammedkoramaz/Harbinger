package com.sn.harbinger.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sn.harbinger.databinding.LoginFragmentBinding
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sn.harbinger.ui.dashboard.DashboardActivity
import com.sn.harbinger.R
import com.sn.harbinger.util.State
import com.sn.harbinger.util.extensions.SnackBarType
import com.sn.harbinger.util.extensions.inAnimation
import com.sn.harbinger.util.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var navigator: NavController
    private val vm: LoginViewModel by viewModels()
    private val binding by lazy {
        LoginFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = requireActivity().findNavController(R.id.auth_nav_host)
        startAnimation()
        initObserve()

        binding.tvRegister.setOnClickListener {
            navigator.navigate(R.id.action_login_to_register)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val errorMsg = vm.validate(email, pass)

            if (errorMsg.isNotEmpty()) {
                showSnackBar(errorMsg, SnackBarType.ERROR, view)
                return@setOnClickListener
            }

            vm.loginRequest(email, pass)
        }

        binding.tvForgotPass.setOnClickListener {
            navigator.navigate(R.id.action_login_to_pass)
        }

    }

    private fun initObserve() {
        vm.loginLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {
                    binding.btnLogin.startAnimation()
                }
                is State.Success -> {
                    if (response.data.result?.token != null) {
                        vm.setToken(response.data.result.token)
                        navigateDashboard()
                    }
                    buttonDoneAnimation()
                }
                is State.Error -> {
                    binding.btnLogin.revertAnimation()
                    if (response.throwable is HttpException) {
                        when {
                            response.throwable.code() == 404 -> {
                                showSnackBar(
                                    getString(R.string.user_not_found),
                                    SnackBarType.ERROR,
                                    requireView()
                                )
                            }
                            response.throwable.code() == 403 -> {
                                showSnackBar(
                                    getString(R.string.incorrect_pass),
                                    SnackBarType.ERROR,
                                    requireView()
                                )
                            }
                            else -> {
                                showSnackBar(
                                    getString(R.string.server_error),
                                    SnackBarType.ERROR,
                                    requireView()
                                )
                            }
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

    private fun navigateDashboard() {
        val intent = Intent(activity, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun startAnimation() {
        binding.tilEmail.inAnimation(requireContext())
        binding.tilPassword.inAnimation(requireContext(), 1200)
    }

    private fun buttonDoneAnimation() {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)?.toBitmap()
        val color = Color.parseColor("#FF4971AC")
        icon?.let { bitmap -> binding.btnLogin.doneLoadingAnimation(color, bitmap) }
    }

}