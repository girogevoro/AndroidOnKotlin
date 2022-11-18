package com.girogevoro.androidonkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.girogevoro.androidonkotlin.R
import com.girogevoro.androidonkotlin.databinding.FragmentWeatherListBinding
import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.model.Location
import com.girogevoro.androidonkotlin.viewmodel.WeatherListViewModel
import com.girogevoro.androidonkotlin.viewmodel.data.AppState
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    private lateinit var viewModel: WeatherListViewModel

    var isRussian = true

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java).apply {
            getLiveData().observe(viewLifecycleOwner) { appState -> renderData(appState) }
        }

        binding.weatherListFragmentFAB.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.sentRequest(Location.Russian)
                binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                viewModel.sentRequest(Location.World)
                binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_earth)
            }
        }

        viewModel.sentRequest(Location.Russian)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.root.snackbar(R.string.error , Snackbar.LENGTH_SHORT)
            }
            AppState.Loading -> {}
            is AppState.Success -> {
                val result = appState.weatherData
            }
            is AppState.SuccessMulti -> {
                binding.mainFragmentRecyclerView.adapter =
                    WeatherListAdapter(appState.weatherList) { weather ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .hide(this@WeatherListFragment)
                                .add(R.id.container, DetailsFragment.newInstance(weather))
                                .addToBackStack("")
                                .commit()

                    }
            }
        }


    }



    fun View.snackbar(message:String, duratinon:Int){
        Snackbar.make(this, message ,duratinon).show()
    }
    fun View.snackbar(message:Int, duratinon:Int){
        Snackbar.make(this, message ,duratinon).show()
    }
}