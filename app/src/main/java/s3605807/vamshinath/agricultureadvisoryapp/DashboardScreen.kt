package s3605807.vamshinath.agricultureadvisoryapp

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import s3605807.vamshinath.agricultureadvisoryapp.weather.WeatherSection
import androidx.core.net.toUri


@Composable
fun DashboardScreen(navController: NavController) {

    Scaffold(
        topBar = { HomeTopBar(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            item { WeatherSection() }

            item { PremiumQuickActions(navController) }


            item {
                OpenMarketsButton()
            }

            item { TipsSection() }
        }
    }
}

@Composable
fun OpenMarketsButton() {

    val context = LocalContext.current

    Button(
        onClick = {

            val gmmIntentUri = "geo:0,0?q=farmers market near me".toUri()

            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            mapIntent.setPackage("com.google.android.apps.maps")

            try {
                context.startActivity(mapIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Google Maps not installed", Toast.LENGTH_SHORT).show()
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(55.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {

        Icon(Icons.Default.Map, contentDescription = null, tint = Color.White)

        Spacer(Modifier.width(8.dp))

        Text("Find Nearby Markets", color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                )
            )
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Agriculture Advisory",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
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

            Text(
                text = location,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = temperature,
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = condition,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

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
        Triple("Pest Detection", Icons.Default.CameraAlt, Color(0xFFFF9800)),
        Triple("Saved Reports", Icons.Default.Bookmark, Color(0xFFFF9800)),
        Triple("Crop Advisory", Icons.Default.Eco, Color(0xFF4CAF50)),
        Triple("Weather", Icons.Default.Cloud, Color(0xFF2196F3)),
        Triple("Market Prices", Icons.Default.AttachMoney, Color(0xFF9C27B0)),
        Triple("About App", Icons.Default.Info, Color(0xFF9C27B0))
    )

    Column(modifier = Modifier.padding(12.dp)) {

        Text(
            "Quick Actions",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column {

            Row(modifier = Modifier.fillMaxWidth()) {
                ActionItem(actions[0], Modifier.weight(1f), onClicked = {
                    navController.navigate(Screen.PestDetection.route)
                })
                ActionItem(actions[1], Modifier.weight(1f), onClicked = {
                    navController.navigate(Screen.SavedReports.route)

                })
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ActionItem(actions[2], Modifier.weight(1f), onClicked = {

                    navController.navigate(Screen.CropAdvisory.route)
                })
                ActionItem(actions[3], Modifier.weight(1f), onClicked = {

                    navController.navigate(Screen.WeatherForecast.route)


                })
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                ActionItem(actions[4], Modifier.weight(1f), onClicked = {
                    navController.navigate(Screen.MarketTrend.route)
                })
                ActionItem(actions[5], Modifier.weight(1f), onClicked = {
                    navController.navigate(Screen.AboutApp.route)

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
        modifier = modifier
            .padding(8.dp)
            .clickable {
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
fun TipsSection() {

    val tips = listOf(
        Triple("Crop Rotation", "Maintain soil fertility", Icons.Default.Autorenew),
        Triple("Soil Moisture", "Check moisture regularly", Icons.Default.WaterDrop),
        Triple("Organic Fertilizer", "Use natural compost", Icons.Default.Eco),
        Triple("Pest Control", "Inspect crops weekly", Icons.Default.BugReport),
        Triple("Weather Check", "Check forecast before irrigation", Icons.Default.Cloud),
        Triple("Weed Control", "Remove weeds early", Icons.Default.Grass)
    )

    val gradients = listOf(
        listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)),
        listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)),
        listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3)),
        listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)),
        listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2)),
        listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))
    )

    Column(modifier = Modifier.padding(12.dp)) {

        Text(
            "Quick Tips 🌱",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow {

            itemsIndexed(tips) { index, (title, desc, icon) ->

                val gradient = Brush.horizontalGradient(
                    gradients[index % gradients.size]
                )

                Card(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .width(200.dp)
                        .height(140.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .background(gradient)
                            .padding(14.dp)
                    ) {

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Icon(
                                icon,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(28.dp)
                            )

                            Column {

                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = desc,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
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