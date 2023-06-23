package com.example.weatherapp.weather.presentation.fragment

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
import com.example.weatherapp.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.databinding.FragmentAddUserBinding
import com.example.weatherapp.databinding.FragmentUserListBinding
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.utils.network.NetworkUtils
import com.example.weatherapp.utils.network.Resource
import com.example.weatherapp.utils.network.UiUtils
import com.example.weatherapp.utils.network.UiUtils.showToast
import com.example.weatherapp.weather.domain.model.Weather
import com.example.weatherapp.weather.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!


    private val weatherViewModel: WeatherViewModel by viewModels()

    private val args: WeatherFragmentArgs by navArgs()

    private var userId: Int = -1

    @Inject
    lateinit var preferenceManager: SharedPreferenceManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*for this demo we are not using UserId for getting weather*/
        userId = args.userId

        onViewClick()
        getWeather()

        observeGetWeather()

    }

    private fun onViewClick() {
        binding.cvLogout.setOnClickListener {
            preferenceManager.putBoolean(SharedPreferenceManager.IS_LOGGED_IN, false)

            findNavController().navigate(R.id.action_weatherFragment_to_loginFragment)
        }

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getWeather() {
        if (NetworkUtils.hasInternetConnection(requireContext())) {
            weatherViewModel.getWeather()
        } else {
            requireActivity().showToast(getString(R.string.txt_check_your_connection))
        }
    }

    private fun observeGetWeather() {
        weatherViewModel.doObserverGetWeather().observe(viewLifecycleOwner) {
            it?.let { res ->
                when (res) {
                    is Resource.Loading -> {
                        showProgress(true)
                    }
                    is Resource.Success -> {
                        showProgress(false)

                        bindData(res.result)
                    }
                    is Resource.Failure -> {
                        showProgress(false)
                        UiUtils.showLogE(res.exception.message)
                        requireActivity().showToast(res.exception.message ?: "something went wrong")
                    }
                    is Resource.NoResult -> {
                        showProgress(false)
                    }
                }
            }
        }
    }

    private fun bindData(weather: Weather) {

        binding.tvTemp.text = "Temp:- ${weather.current.temp}"
        binding.tvHumidity.text = "Humidity:- ${weather.current.humidity}"
        binding.tvWindSpeed.text = "Wind Speed:- ${weather.current.wind_speed}"

        if (weather.current.weather.isEmpty().not()) {
            val weatherList = weather.current.weather.first()
            binding.tvWeather.text = "Weather:- ${weatherList.main}"
            binding.tvWeathers.text = "Weather:- ${weatherList.description}"
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            binding.progress.visibility = View.VISIBLE
        else
            binding.progress.visibility = View.GONE
    }
}