package com.example.weather.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.view.MainActivity.Key.defaultLat
import com.example.weather.view.MainActivity.Key.defaultLon
import com.example.weather.viewmodel.ApiStatus
import com.example.weather.viewmodel.ForecastViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.security.Permissions


class ForecastFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentForecastBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val Permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val viewModel: ForecastViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(inflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocation()

        recycler = binding.recyclerView
        val adapter = WeekForecastAdapter(requireActivity())

        viewModel.status.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = it.loader
            binding.forecastLayout.visibility = it.layout
            if (it == ApiStatus.ERROR) {
                val toast = Toast.makeText(
                    activity, "Ошибка при получении данных", Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.TOP, 0, 0)
                toast.show()
            }
        }

        viewModel.forecast.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.forecast = it
                val identifier = resources.getIdentifier(
                    it.icon ?: "mock",
                    "drawable",
                    requireActivity().packageName
                )
                binding.icon.setImageResource(identifier)
            }
        }
        viewModel.city.observe(viewLifecycleOwner) { binding.city.setText(it) }
        viewModel.weekForecast.observe(viewLifecycleOwner) { adapter.setData(it) }
        viewModel.weekStatus.observe(viewLifecycleOwner) { adapter.setSt(it.loader) }

        binding.root.setOnClickListener {
            closeKeyboard()
            binding.city.clearFocus()
            binding.city.setSelection(0)

        }

        binding.city.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            var text = v.text.toString()

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeKeyboard()
                binding.city.clearFocus()
                if (text != "") {
                    text = text.substring(0, 1).uppercase() + text
                        .substring(1)
                    viewModel.getForecastByCity(text)
                    viewModel.getWeekForecastByCity(text)
                    return@OnEditorActionListener true
                } else {
                    viewModel.city.value?.let { viewModel.getForecastByCity(it) }
                    viewModel.city.value?.let { viewModel.getWeekForecastByCity(it) }
                    return@OnEditorActionListener true
                }
            }
            false
        })

        binding.location.setOnClickListener {
            closeKeyboard()
            getLocation()
        }

        recycler.adapter = adapter
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun askForPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), Permissions.toTypedArray(), 100)
        if (!checkPermissions()) {
            getDefaultWeather()
        }
    }

    private fun checkPermissions(): Boolean {
        for (permission in Permissions) {
            val permissionState = requireActivity().checkSelfPermission(permission)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                binding.location.drawable.setTint(Color.GRAY)
                binding.location.isClickable = false
                return false
            }
        }
        binding.location.drawable.setTint(Color.BLACK)
        binding.location.isClickable = true
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDefaultWeather() {
        viewModel.setCity(defaultLat, defaultLon)
        viewModel.getForecast(defaultLat, defaultLon)
        viewModel.getWeekForecast(defaultLat, defaultLon)
    }

    private fun closeKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val manager: InputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val toast = Toast.makeText(
                activity, "Геопозиция недоступна", Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
            askForPermissions()
        }
        if (checkPermissions()) {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            )
                .addOnSuccessListener { location: Location? ->
                    if (location == null) getDefaultWeather()
                    else {
                        viewModel.getForecast(location.latitude, location.longitude)
                        viewModel.getWeekForecast(location.latitude, location.longitude)
                    }
                }
        }
    }

}
