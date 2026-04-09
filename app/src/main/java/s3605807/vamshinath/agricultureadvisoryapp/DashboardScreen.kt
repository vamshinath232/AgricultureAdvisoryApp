package s3605807.vamshinath.agricultureadvisoryapp

import android.content.Context
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import s3605807.vamshinath.agricultureadvisoryapp.weather.WeatherSection
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun DashboardScreen(navController: NavController) {

    Scaffold(
        topBar = { HomeTopBar() }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            item { WeatherSection() }

            item { PremiumQuickActions(navController) }

            item { CropAlertsSection() }

            item { MarketPriceSection() }

            item { TipsSection() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Agriculture Advisory",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* TODO Profile */ }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}


@Composable
fun PremiumWeatherCard(
    location: String,
    temperature: String,
    condition: String,
    humidity: String,
    rainChance: String
) {

    Box(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF4CAF50),
                        Color(0xFF81C784)
                    )
                )
            )
            .padding(20.dp)
    ) {

        Column {

            // 📍 Location
            Text(
                text = location,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🌡 Temperature
            Text(
                text = temperature,
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            // ☁️ Condition
            Text(
                text = condition,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 💧 Humidity + 🌧 Rain
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💧 $humidity",
                    color = Color.White
                )
                Text(
                    text = "🌧 $rainChance",
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun PremiumQuickActions(navController: NavController) {

    val actions = listOf(
        Triple("Crop Advisory", Icons.Default.Eco, Color(0xFF4CAF50)),
        Triple("Pest Detection", Icons.Default.CameraAlt, Color(0xFFFF9800)),
        Triple("Weather", Icons.Default.Cloud, Color(0xFF2196F3)),
        Triple("Market Prices", Icons.Default.AttachMoney, Color(0xFF9C27B0))
    )

    Column(modifier = Modifier.padding(12.dp)) {

        Text(
            "Quick Actions",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column {

            // Row 1
            Row(modifier = Modifier.fillMaxWidth()) {
                ActionItem(actions[0], Modifier.weight(1f), onClicked = {
                    navController.navigate(Screen.CropAdvisory.route)
                })
                ActionItem(actions[1], Modifier.weight(1f), onClicked = {

                })
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                ActionItem(actions[2], Modifier.weight(1f), onClicked = {

                })
                ActionItem(actions[3], Modifier.weight(1f), onClicked = {

                })
            }
        }
    }
}

@Composable
fun ActionItem(
    item: Triple<String, ImageVector, Color>,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val (title, icon, color) = item

    Card(
        modifier = modifier.padding(8.dp)
            .clickable{
                onClicked.invoke()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(title, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CropAlertsSection() {

    Column(modifier = Modifier.padding(12.dp)) {

        Text("Crop Alerts", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD))
        ) {
            Text(
                "⚠️ High chance of pest attack this week",
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD1ECF1))
        ) {
            Text(
                "💧 Irrigation recommended due to low rainfall",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun MarketPriceSection() {

    Column(modifier = Modifier.padding(12.dp)) {

        Text("Market Prices", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        val crops = listOf(
            "Wheat - £210/ton",
            "Barley - £180/ton",
            "Potato - £150/ton"
        )

        crops.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun TipsSection() {

    Column(modifier = Modifier.padding(12.dp)) {

        Text("Quick Tips", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        val tips = listOf(
            "Rotate crops to maintain soil fertility",
            "Monitor soil moisture regularly",
            "Use organic fertilizers when possible"
        )

        LazyRow {
            items(tips) { tip ->
                Card(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(220.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = tip,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

class LocationHelper(private val context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { onLocationReceived(it) }
        }
    }
}