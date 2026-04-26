package s3605807.vamshinath.agricultureadvisoryapp.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                WeatherLoadingUI()
            }
        }
    }
}

@Composable
fun WeatherLoadingUI() {

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(p1.copy(alpha = 0.1f), p2.copy(alpha = 0.1f))
                    )
                )
                .padding(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                val infiniteTransition = rememberInfiniteTransition()
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.9f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = p1,
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scale)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Fetching Weather...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = p1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Getting real-time weather data for your farm",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = p1,
                    trackColor = p1.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionWrapper(
    content: @Composable () -> Unit
) {

    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    when {
        permissionState.status.isGranted -> {
            content()
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

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(p2.copy(alpha = 0.2f), Color.White)
                    )
                )
                .padding(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(p1.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = p1,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location Permission Required",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = p1,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Allow location access to get accurate weather updates and nearby market prices for your farm.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = p1)
                ) {

                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Allow Location", color = Color.White)
                }
            }
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

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(p2.copy(alpha = 0.2f), Color.White)
                    )
                )
                .padding(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(p1.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOff,
                        contentDescription = null,
                        tint = p1,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location Disabled",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = p1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enable location to receive accurate weather updates and nearby agricultural insights.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onEnableClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = p1)
                ) {

                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Turn On Location", color = Color.White)
                }
            }
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