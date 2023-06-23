package com.example.weatherapp.user.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.auth.domain.model.User
import com.example.weatherapp.auth.presentation.fragment.LoginFragment
import com.example.weatherapp.auth.presentation.fragment.LoginFragmentDirections
import com.example.weatherapp.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.databinding.FragmentUserListBinding
import com.example.weatherapp.user.presentation.adapter.UserListAdapter
import com.example.weatherapp.user.presentation.adapter.UserListAdapterListener
import com.example.weatherapp.utils.network.Resource
import com.example.weatherapp.utils.network.UiUtils
import com.example.weatherapp.utils.network.UiUtils.showToast
import com.example.weatherapp.weather.presentation.fragment.WeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment(), UserListAdapterListener {


    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var userListAdapter: UserListAdapter

    companion object {
        const val TAG = "UserListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewClick()
        setupUserRecycler()
        observeGetAllUsers()

        getAllUsers()
    }

    private fun onViewClick() {
        binding.cvAdd.setOnClickListener {
            val action = UserListFragmentDirections.actionUserListFragmentToAddUserFragment(LoginFragment.TAG)
            findNavController().navigate(action)
        }
    }

    private fun setupUserRecycler() {
        userListAdapter = UserListAdapter(this)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.apply {
            this.layoutManager = layoutManager
            adapter = userListAdapter
        }
    }

    private fun getAllUsers() {
        authViewModel.getAllUser()
    }

    private fun observeGetAllUsers() {
        authViewModel.doObserverGetAllUser().observe(viewLifecycleOwner) {
            it?.let { res ->
                when (res) {
                    is Resource.Loading -> {
                        showProgress(true)
                        showNoResult(false)
                        showRvUsersList(false)
                    }
                    is Resource.Success -> {
                        showProgress(false)
                        showNoResult(false)
                        showRvUsersList(true)

                        /*Add to recycler adapter*/
                        userListAdapter.submitList(res.result)

                    }
                    is Resource.Failure -> {
                        showProgress(false)
                        showNoResult(false)
                        showRvUsersList(false)
                        UiUtils.showLogE(res.exception.message)
                        requireActivity().showToast(res.exception.message ?: "something went wrong")
                    }
                    is Resource.NoResult -> {
                        showProgress(false)
                        showNoResult(true)
                        showRvUsersList(false)
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

    private fun showNoResult(show: Boolean) {
        if (show)
            binding.llNoResult.visibility = View.VISIBLE
        else
            binding.llNoResult.visibility = View.GONE
    }

    private fun showRvUsersList(show: Boolean) {
        if (show)
            binding.rvUsers.visibility = View.VISIBLE
        else
            binding.rvUsers.visibility = View.GONE
    }

    override fun onUserClick(user: User) {
        val action = UserListFragmentDirections.actionUserListFragmentToWeatherFragment(user.userId)
        findNavController().navigate(action)
    }

    override fun onDelete(user: User) {
        authViewModel.deleteUser(user)
    }
}