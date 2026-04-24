package s3605807.vamshinath.agricultureadvisoryapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


data class PriceData(
    val month: String,
    val price: Float
)

fun loadPriceData(context: Context): List<PriceData> {
    val json = context.assets.open("wheat_prices.json")
        .bufferedReader().use { it.readText() }

    val type = object : TypeToken<List<PriceData>>() {}.type
    return Gson().fromJson(json, type)
}

@Composable
fun PriceChart(data: List<PriceData>) {

    AndroidView(
        factory = { context ->
            LineChart(context).apply {

                val entries = data.mapIndexed { index, item ->
                    Entry(index.toFloat(), item.price)
                }

                val dataSet = LineDataSet(entries, "Price Trend").apply {
                    lineWidth = 3f
                    circleRadius = 5f
                    setDrawValues(false)
                }

                this.data = LineData(dataSet)

                xAxis.valueFormatter = IndexAxisValueFormatter(
                    data.map { it.month }
                )

                description.isEnabled = false
                axisRight.isEnabled = false
                animateX(1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

data class TrendResult(
    val trend: String,
    val insight: String,
    val advice: String
)

fun analyzeTrend(data: List<PriceData>): TrendResult {

    val first = data.first().price
    val last = data.last().price

    return if (last > first) {
        TrendResult(
            trend = "📈 Increasing",
            insight = "Prices increased over time",
            advice = "Hold crop for better profit"
        )
    } else {
        TrendResult(
            trend = "📉 Decreasing",
            insight = "Prices are falling",
            advice = "Sell now to avoid loss"
        )
    }
}

@Composable
fun PriceTrendScreenOld() {

    val context = LocalContext.current
    val data = remember { loadPriceData(context) }
    val result = remember { analyzeTrend(data) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🌿 Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        )
                    )
                    .padding(20.dp)
            ) {
                Text(
                    "Crop Price Trends 🇬🇧",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }

        // 📊 Chart
        item {
            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Wheat Price Trend", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))

                    PriceChart(data)
                }
            }
        }

        // 🤖 Insights Card
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Smart Insights 🤖", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Trend: ${result.trend}")
                    Text("Insight: ${result.insight}")
                    Text("Advice: ${result.advice}")
                }
            }
        }
    }
}