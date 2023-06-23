package com.example.weatherapp.auth.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.app.SharedPreferenceManager
import com.example.weatherapp.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.databinding.FragmentLoginBinding
import com.example.weatherapp.user.presentation.fragment.AddUserFragmentDirections
import com.example.weatherapp.utils.network.NetworkUtils
import com.example.weatherapp.utils.network.Resource
import com.example.weatherapp.utils.network.TextUtils.isValidEmail
import com.example.weatherapp.utils.network.UiUtils.showLogE
import com.example.weatherapp.utils.network.UiUtils.showToast
import com.example.weatherapp.utils.network.UserException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: SharedPreferenceManager

    companion object {
        const val TAG = "LoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewClick()
        observeGetLoginViaEmail()
    }

    private fun getEmail() =
        binding.etEmail.text.toString().trim()

    private fun isValidEmail() =
        getEmail().isValidEmail()

    private fun getPassword() =
        binding.etPassword.text.toString().trim()

    private fun isValidPassword() =
        getPassword().length > 4

    private fun onViewClick() {
        binding.btnLogin.setOnClickListener {
            when {
                !isValidEmail() -> {
                    requireActivity().showToast(getString(R.string.enter_valid_email))
                }
                !isValidPassword() -> {
                    requireActivity().showToast(getString(R.string.enter_valid_password))
                }
                else -> {
                    getLoginViaEmail(
                        email = getEmail(),
                        password = getPassword()
                    )
                }
            }
        }
    }

    private fun getLoginViaEmail(email: String, password: String) {
        authViewModel.getLoginViaEmail(
            email = email,
            pass = password
        )
    }

    private fun observeGetLoginViaEmail() {
        authViewModel.doObserverGetLoginViaEmail().observe(viewLifecycleOwner) {
            it?.let { res ->
                when (res) {
                    is Resource.Loading -> {
                        showProgress(true)
                    }
                    is Resource.Success -> {
                        showProgress(show = false)
                        requireActivity().showToast("Logged in")

                        preferenceManager.putBoolean(SharedPreferenceManager.IS_LOGGED_IN, true)

                        findNavController().navigate(R.id.action_loginFragment_to_userListFragment)
                    }
                    is Resource.Failure -> {
                        showProgress(show = false)
                        showLogE(res.exception.message)

                        if (res.exception is UserException) {
                            if (res.exception.code == UserException.PASSWORD_NOT_MATCH) {
                                requireActivity().showToast(getString(R.string.password_not_match))
                            }
                        } else {
                            requireActivity().showToast(
                                res.exception.message ?: "something went wrong"
                            )
                        }

                    }
                    is Resource.NoResult -> {
                        showProgress(show = false)
                        requireActivity().showToast("User not found. Try sign up")

                        val action = LoginFragmentDirections.actionLoginFragmentToAddUserFragment(TAG)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            binding.progress.visibility = View.VISIBLE
        else
            binding.progress.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}