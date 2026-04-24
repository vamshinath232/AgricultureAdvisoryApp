@file:OptIn(ExperimentalMaterial3Api::class)

package s3605807.vamshinath.agricultureadvisoryapp

import androidx.compose.ui.text.font.FontWeight

// ---------------- IMPORTS ----------------

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.database.FirebaseDatabase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

// ---------------- DATA MODEL ----------------

data class PricePoint(
    val month: String = "",
    val price: Float = 0f
)

data class SellInsight(
    val bestMonth: String,
    val highestPrice: Float,
    val suggestion: String
)

// ---------------- MONTH SORT ----------------

fun sortByMonth(list: List<PricePoint>): List<PricePoint> {
    val order = listOf(
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    )
    return list.sortedBy { order.indexOf(it.month) }
}

// ---------------- FIREBASE FETCH ----------------

fun fetchCropData(
    crop: String,
    onResult: (Map<String, List<PricePoint>>) -> Unit
) {
    val db = FirebaseDatabase.getInstance().reference
        .child("crop_prices")
        .child(crop)

    db.get().addOnSuccessListener { snapshot ->

        val result = mutableMapOf<String, List<PricePoint>>()

        snapshot.children.forEach { yearSnap ->

            val year = yearSnap.key ?: return@forEach

            val list = yearSnap.children.map {
                PricePoint(
                    month = it.key ?: "",
                    price = (it.value as Number).toFloat()
                )
            }

            result[year] = list
        }

        onResult(result)
    }
}

// ---------------- AI LOGIC ----------------

fun getBestSellMonth(data: List<PricePoint>): SellInsight {

    val max = data.maxByOrNull { it.price }
        ?: return SellInsight("", 0f, "")

    return SellInsight(
        bestMonth = max.month,
        highestPrice = max.price,
        suggestion = "Sell in ${max.month} when price peaks at £${max.price}"
    )
}

// ---------------- CHART ----------------

@Composable
fun MultiLineChart(data: Map<String, List<PricePoint>>) {

    AndroidView(
        factory = { context ->

            LineChart(context).apply {

                val dataSets = mutableListOf<ILineDataSet>()

                data.forEach { (year, list) ->

                    val sorted = sortByMonth(list)

                    val max = sorted.maxByOrNull { it.price }
                    val min = sorted.minByOrNull { it.price }

                    val entries = sorted.mapIndexed { index, item ->
                        Entry(index.toFloat(), item.price).apply {
                            if (item == max) this.data = "MAX"
                            if (item == min) this.data = "MIN"
                        }
                    }

                    val set = LineDataSet(entries, year).apply {
                        lineWidth = 3f
                        circleRadius = 5f
                        setDrawValues(false)
                    }

                    dataSets.add(set)
                }

                this.data = LineData(dataSets)

                val months = data.values.firstOrNull()
                    ?.let { sortByMonth(it).map { it.month } }
                    ?: emptyList()

                xAxis.valueFormatter = IndexAxisValueFormatter(months)

                axisRight.isEnabled = false
                description.isEnabled = false

                animateX(1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    )
}

// ---------------- UI SCREEN ----------------

@Composable
fun PriceTrendScreen(
    onBackClick: () -> Unit
) {

    val crops = listOf(
        "Wheat","Barley","Potato","Oilseed Rape",
        "Oats","Sugar Beet"
    )

    val cropIcons = mapOf(
        "Wheat" to Icons.Default.Grass,
        "Barley" to Icons.Default.Grass,
        "Potato" to Icons.Default.Spa,
        "Oilseed Rape" to Icons.Default.LocalFlorist,
        "Oats" to Icons.Default.Grass,
        "Sugar Beet" to Icons.Default.Eco
    )

    var selectedCrop by remember { mutableStateOf("Wheat") }
    var searchQuery by remember { mutableStateOf("") }
    var data by remember { mutableStateOf<Map<String, List<PricePoint>>>(emptyMap()) }

    // 🔄 Fetch real Firebase data
    LaunchedEffect(selectedCrop) {
        fetchCropData(selectedCrop) {
            data = it
        }
    }

    val latestYear = data.keys.maxOrNull()
    val latestData = data[latestYear] ?: emptyList()
    val insight = getBestSellMonth(latestData)

    val prediction = remember(latestData) {
        predictNext3Months(latestData)
    }

    val risk = remember(latestData) {
        calculateRisk(latestData)
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
                            "Price Trends",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // 🔍 Search + Crop Chips
            item {

                Column(modifier = Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search Crop") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val filtered = crops.filter {
                        it.contains(searchQuery, ignoreCase = true)
                    }

                    LazyRow {
                        items(filtered) { crop ->

                            Card(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        selectedCrop = crop
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (crop == selectedCrop)
                                        Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    else Color.White
                                )
                            ) {

                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        cropIcons[crop] ?: Icons.Default.Eco,
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(crop)
                                }
                            }
                        }
                    }
                }
            }

            // 📊 Chart
            item {
                AnimatedContent(targetState = data, label = "") {
                    if (it.isNotEmpty()) {
                        MultiLineChart(it)
                    } else {
                        Text(
                            "Loading...",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // 🤖 Insight Card
            item {

                if (latestData.isNotEmpty()) {

                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Best Selling Insight 💰",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Month: ${insight.bestMonth}")
                            Text("Price: £${insight.highestPrice}")
                            Text(insight.suggestion)
                        }
                    }
                }
            }

            item {
                PredictionCard(prediction)
            }

            item {
                RiskCard(risk)
            }
        }
    }
}