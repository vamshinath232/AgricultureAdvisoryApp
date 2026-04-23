package s3605807.vamshinath.agricultureadvisoryapp.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import s3605807.vamshinath.agricultureadvisoryapp.LocationHelper
import s3605807.vamshinath.agricultureadvisoryapp.PremiumWeatherCard


// Weather API KEY - bb9afdfdada04c6696993046260904





data class WeatherResponse(
    val location: Location,
    val current: Current
)


data class Location(
    val name: String,
    val country: String
)


data class Current(
    val temp_c: Double,
    val humidity: Int,
    val condition: Condition,
    val precip_mm: Double
)

data class Condition(
    val text: String,
    val icon: String
)

interface WeatherApi {

    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String // "lat,lon"
    ): WeatherResponse

    @GET("v1/forecast.json")
    suspend fun get7DayForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int
    ): ForecastResponse
}

object RetrofitInstance {

    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}

class WeatherViewModel : ViewModel() {

    var weatherData by mutableStateOf<WeatherResponse?>(null)

    fun fetchWeather(lat: Double, lon: Double) {

        viewModelScope.launch {

            Log.d("WeatherAPI", "Request → lat: $lat, lon: $lon")

            try {
                val response = RetrofitInstance.api.getWeather(
                    apiKey = "bb9afdfdada04c6696993046260904",
                    location = "$lat,$lon"
                )

                Log.d("WeatherAPI", "Response Success → $response")

                weatherData = response

            } catch (e: Exception) {

                Log.e("WeatherAPI", "Error → ${e.message}", e)
            }
        }
    }
}

@Composable
fun WeatherSection(viewModel: WeatherViewModel = viewModel()) {

    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }


    LocationPermissionWrapper {

        val isLocationOn = remember {
            mutableStateOf(isLocationEnabled(context))
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    isLocationOn.value = isLocationEnabled(context)
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        if (!isLocationOn.value) {

            LocationDisabledUI {
                openLocationSettings(context)
            }

        } else {

            LaunchedEffect(Unit) {
                locationHelper.getCurrentLocation { location ->
                    viewModel.fetchWeather(
                        location.latitude,
                        location.longitude
                    )
                }
            }

            val data = viewModel.weatherData

            if (data != null) {
                PremiumWeatherCard(
                    location = "${data.location.name}, ${data.location.country}",
                    temperature = "${data.current.temp_c.toInt()}°C",
                    condition = data.current.condition.text,
                    humidity = "${data.current.humidity}%",
                    rainChance = "${data.current.precip_mm} mm"
                )
            } else {
                Text("Loading weather...")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionWrapper(
    content: @Composable () -> Unit   // ✅ FIX HERE
) {

    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    when {
        permissionState.status.isGranted -> {
            content()   // ✅ now composable works
        }

        permissionState.status.shouldShowRationale -> {
            PermissionRationale {
                permissionState.launchPermissionRequest()
            }
        }

        else -> {
            PermissionRequestUI {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

@Composable
fun PermissionRequestUI(onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Location Permission Required")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "We need your location to show weather and nearby market prices."
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onClick) {
            Text("Allow Location")
        }
    }
}

@Composable
fun PermissionRationale(onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Location Needed")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Without location, weather and advisory features won't work properly."
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onClick) {
            Text("Grant Permission")
        }
    }
}

@Composable
fun LocationDisabledUI(onEnableClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Location is turned OFF")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Please enable location to get weather updates.")

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onEnableClick) {
            Text("Turn On Location")
        }
    }
}

fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}