package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.R
import abolfazli.mahdi.weather.WeatherApp
import abolfazli.mahdi.weather.databinding.FragmentWeatherBinding
import abolfazli.mahdi.weather.weather.data.entities.CurrentWeatherEntity
import abolfazli.mahdi.weather.weather.data.entities.HourlyEntity
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_weather.*
import javax.inject.Inject

class WeatherFragment : Fragment() {
    @Inject
    lateinit var viewModel: WeatherViewModel

    private lateinit var binding: FragmentWeatherBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var appId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).weatherComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, abolfazli.mahdi.weather.R.layout.fragment_weather, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appId = getString(R.string.api_key)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val cityName = arguments?.getString("cityName")
        if (cityName != null) {
            viewModel.getCurrentWeatherByCityName(appId, cityName)
            viewModel.getHourlyForecastByCityName(appId, cityName)
        } else {
            getLastLocation()
        }


        val currentWeatherObserver = Observer<CurrentWeatherEntity> { weather ->
            binding.weather = weather
        }
        viewModel.currentWeather.observe(this, currentWeatherObserver)

        val hourlyForecastObserver = Observer<HourlyEntity> { hourlyForecast ->
            binding.hourlyForecastWeather = hourlyForecast
        }
        viewModel.hourlyForecast.observe(this, hourlyForecastObserver)

        val dailyForecastObserver = Observer<HourlyEntity> { dailyForecast ->
            binding.dailyForecastWeather = dailyForecast
        }
        viewModel.dailyForecast.observe(this, dailyForecastObserver)

        val errorObserver = Observer<String> { error ->
            Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show()
        }
        viewModel.errorString.observe(this, errorObserver)


        iv_search.setOnClickListener {
            findNavController().navigate(R.id.actoin_weatherFragment_to_searchFragment)
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    val PERMISSION_ID = 42

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        viewModel.getCurrentWeatherByLatLon(
                            appId,
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        viewModel.getHourlyForecastByLatLon(
                            appId,
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation
            viewModel.getCurrentWeatherByLatLon(
                appId,
                location.latitude.toString(),
                location.longitude.toString()
            )
            viewModel.getHourlyForecastByLatLon(
                appId,
                location.latitude.toString(),
                location.longitude.toString()
            )
        }
    }
}