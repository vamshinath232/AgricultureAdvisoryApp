@file:OptIn(ExperimentalMaterial3Api::class)


package s3605807.vamshinath.agricultureadvisoryapp.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.util.Log
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import s3605807.vamshinath.agricultureadvisoryapp.LocationHelper
import java.util.Locale

/* ---------------------------------------------------------
   DATA MODELS (WeatherAPI Forecast Response)
--------------------------------------------------------- */

data class ForecastResponse(
    val location: Location,
    val forecast: Forecast
)

//data class Location(
//    val name: String,
//    val country: String
//)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val daily_chance_of_rain: Int,
    val condition: Condition
)

//data class Condition(
//    val text: String,
//    val icon: String
//)

/* ---------------------------------------------------------
   SCREEN
--------------------------------------------------------- */

@Composable
fun WeatherForecastScreen(
    viewModel: WeatherForecastViewModel = viewModel(),
    onBack: () -> Unit
) {


    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val forecastData by viewModel.forecastData.collectAsState()

    LaunchedEffect(Unit) {

        Log.d("ForecastScreen", "LaunchedEffect started")

        locationHelper.getCurrentLocation { location ->

            Log.d("ForecastScreen", "getCurrentLocation callback triggered")

            if (location != null) {

                Log.d(
                    "ForecastScreen",
                    "Location received → lat: ${location.latitude}, lon: ${location.longitude}"
                )

                viewModel.fetch7DayForecast(
                    lat = location.latitude,
                    lon = location.longitude
                )

                Log.d(
                    "ForecastScreen",
                    "fetch7DayForecast called with real location"
                )

            } else {

                Log.e(
                    "ForecastScreen",
                    "Location is NULL — using fallback location"
                )

                val fallbackLat = 51.5074
                val fallbackLon = 0.1278

                Log.d(
                    "ForecastScreen",
                    "Fallback location → lat: $fallbackLat, lon: $fallbackLon"
                )

                viewModel.fetch7DayForecast(
                    lat = fallbackLat,
                    lon = fallbackLon
                )

                Log.d(
                    "ForecastScreen",
                    "fetch7DayForecast called with fallback location"
                )
            }
        }
    }


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        )
                    )
            ) {

                TopAppBar(
                    title = {
                        Text(
                            "3-Day Weather Forecast",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent // 🔥 important for gradient
                    )
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
        ) {

            WeatherHeaderCard()

            if (forecastData == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            } else if (forecastData!!.forecast.forecastday.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No forecast data available")
                }

            } else {

                LazyColumn(
                    modifier = Modifier.padding(12.dp)
                ) {

                    itemsIndexed(forecastData!!.forecast.forecastday) { index, day ->

                        ForecastDayCard(
                            day = day,
                            index = index
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   HEADER CARD
--------------------------------------------------------- */

@Composable
fun WeatherHeaderCard() {

    val gradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF2E7D32),
            Color(0xFF66BB6A)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box(
            modifier = Modifier
                .background(gradient)
                .padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🌦 Icon Container (Glass effect)
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "Weekly Weather Outlook",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Plan irrigation and crop protection based on upcoming weather.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   FORECAST ITEM CARD
--------------------------------------------------------- */

@Composable
fun ForecastDayCard(
    day: ForecastDay,
    index: Int // 👈 pass position for variation + labels
) {

    val condition = day.day.condition.text.lowercase()
    val temp = day.day.maxtemp_c

    // 🌤 Label (Premium UX)
    val dayLabel = when (index) {
        0 -> "Today"
        1 -> "Tomorrow"
        else -> formatDate(day.date)
    }

    // 🎨 Dynamic Gradient with variation
    val gradient = when {
        condition.contains("sun") || condition.contains("clear") -> {
            val shades = listOf(
                listOf(Color(0xFFFFB300), Color(0xFFFFD54F)),
                listOf(Color(0xFFFFA000), Color(0xFFFFE082)),
                listOf(Color(0xFFFF8F00), Color(0xFFFFD740))
            )
            Brush.horizontalGradient(shades[index % shades.size])
        }

        condition.contains("rain") -> Brush.horizontalGradient(
            listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
        )

        temp < 10 -> Brush.horizontalGradient(
            listOf(Color(0xFF00ACC1), Color(0xFF80DEEA))
        )

        else -> Brush.horizontalGradient(
            listOf(Color(0xFF546E7A), Color(0xFF90A4AE))
        )
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(12.dp), // 🔥 more depth
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {

        Box(
            modifier = Modifier
                .background(gradient)
                .padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🌤 Icon Container (Improved contrast)
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.30f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = getWeatherIcon(condition),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // 📅 Date + Condition
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = dayLabel,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp // 👈 slightly bigger
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = day.day.condition.text,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }

                // 🌡 Temperature + Rain
                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "${day.day.maxtemp_c.toInt()}° / ${day.day.mintemp_c.toInt()}°",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp // 👈 reduced (better balance)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${day.day.daily_chance_of_rain}%",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

fun getWeatherIcon(condition: String): ImageVector {

    return when {
        condition.contains("rain", true) -> Icons.Default.WaterDrop
        condition.contains("cloud", true) -> Icons.Default.Cloud
        condition.contains("sun", true) -> Icons.Default.WbSunny
        else -> Icons.Default.Cloud
    }
}

fun formatDate(date: String): String {
    return try {
        val parser = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = java.text.SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        formatter.format(parser.parse(date)!!)
    } catch (e: Exception) {
        date
    }
}

/* ---------------------------------------------------------
   VIEWMODEL
--------------------------------------------------------- */



class WeatherForecastViewModel : ViewModel() {

    private val _forecastData =
        MutableStateFlow<ForecastResponse?>(null)

    val forecastData: StateFlow<ForecastResponse?> =
        _forecastData

    fun fetch7DayForecast(
        lat: Double,
        lon: Double
    ) {

        viewModelScope.launch {

            Log.d(
                "ForecastViewModel",
                "API request started → lat: $lat, lon: $lon"
            )

            try {

                val response =
                    RetrofitInstance.api.get7DayForecast(
                        apiKey = "bb9afdfdada04c6696993046260904",
                        location = "$lat,$lon",
                        days = 7
                    )

                Log.d(
                    "ForecastViewModel",
                    "API SUCCESS → received ${response.forecast.forecastday.size} days"
                )

                _forecastData.value = response

            } catch (e: Exception) {

                Log.e(
                    "ForecastViewModel",
                    "API ERROR → ${e.message}",
                    e
                )
            }
        }
    }
}