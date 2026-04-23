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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import s3605807.vamshinath.agricultureadvisoryapp.LocationHelper

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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "7-Day Weather Forecast",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
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

                    items(forecastData!!.forecast.forecastday) { day ->

                        ForecastDayCard(day)

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF1976D2),
                        Color(0xFF64B5F6)
                    )
                )
            )
            .padding(18.dp)
    ) {

        Column {

            Text(
                text = "Weekly Weather Outlook",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Plan irrigation and crop protection based on upcoming weather.",
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

/* ---------------------------------------------------------
   FORECAST ITEM CARD
--------------------------------------------------------- */

@Composable
fun ForecastDayCard(
    day: ForecastDay
) {

    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = Color(0xFF1976D2)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = day.date,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = day.day.condition.text,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = "${day.day.maxtemp_c.toInt()}° / ${day.day.mintemp_c.toInt()}°",
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${day.day.daily_chance_of_rain}%",
                        fontSize = 12.sp
                    )
                }
            }
        }
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