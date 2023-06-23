package com.example.weatherapp.user.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.R
import com.example.weatherapp.app.SharedPreferenceManager
import com.example.weatherapp.auth.domain.model.User
import com.example.weatherapp.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.databinding.FragmentAddUserBinding
import com.example.weatherapp.databinding.FragmentUserListBinding
import com.example.weatherapp.utils.network.TextUtils.isValidEmail
import com.example.weatherapp.utils.network.UiUtils.showToast
import com.example.weatherapp.weather.presentation.fragment.WeatherFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    private val args: AddUserFragmentArgs by navArgs()

    private var from: String =  ""

    @Inject
    lateinit var preferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)

        from = args.from

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewClick()
    }

    private fun onViewClick() {
        binding.btnAdd.setOnClickListener {
            when {
                isValidFirstName().not() -> {
                    requireActivity().showToast(getString(R.string.enter_first_name))
                }
                isValidLastName().not() -> {
                    requireActivity().showToast(getString(R.string.enter_last_name))
                }
                isValidEmail().not() -> {
                    requireActivity().showToast(getString(R.string.enter_valid_email))
                }
                isValidPassword().not() -> {
                    requireActivity().showToast(getString(R.string.enter_valid_password))
                }
                else -> {
                    addUser(
                        User(
                            firstName = getFirstName(),
                            lastName = getLastName(),
                            email = getEmail(),
                            passWord = getPassword()
                        )
                    )

                    requireActivity().showToast("User created")

                    if (from == UserListFragment.TAG) {
                        findNavController().popBackStack()
                    } else {
                        preferenceManager.putBoolean(SharedPreferenceManager.IS_LOGGED_IN, true)
                        findNavController().navigate(R.id.action_addUserFragment_to_userListFragment)
                    }

                }
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getPassword() =
        binding.etPassword.text.toString().trim()

    private fun isValidPassword() =
        getPassword().length > 4

    private fun getEmail() =
        binding.etEmail.text.toString().trim()

    private fun isValidEmail() =
        getEmail().isValidEmail()

    private fun getFirstName() =
        binding.etFirstName.text.toString().trim()

    private fun isValidFirstName() =
        getFirstName().isNotEmpty()

    private fun getLastName() =
        binding.etLastName.text.toString().trim()

    private fun isValidLastName() =
        getLastName().isNotEmpty()


    private fun addUser(user: User) {
        authViewModel.addUser(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}