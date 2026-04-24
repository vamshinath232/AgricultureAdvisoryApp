import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

//package s3605807.vamshinath.agricultureadvisoryapp.cropAdvisory
//
//import android.content.Context
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Agriculture
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Eco
//import androidx.compose.material.icons.filled.Grass
//import androidx.compose.material.icons.filled.LocalDining
//import androidx.compose.material.icons.filled.LocalFlorist
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Spa
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.github.mikephil.charting.charts.LineChart
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
//import com.google.firebase.database.FirebaseDatabase
//import org.json.JSONObject
//
//data class PricePoint(
//    val month: String = "",
//    val price: Float = 0f
//)
//
//fun fetchCropData(
//    crop: String,
//    onResult: (Map<String, List<PricePoint>>) -> Unit
//) {
//
//    val db = FirebaseDatabase.getInstance().reference
//        .child("crop_prices")
//        .child(crop)
//
//    db.get().addOnSuccessListener { snapshot ->
//
//        val yearlyData = mutableMapOf<String, List<PricePoint>>()
//
//        snapshot.children.forEach { yearSnap ->
//
//            val year = yearSnap.key ?: return@forEach
//
//            val list = yearSnap.children.map {
//                PricePoint(
//                    month = it.key ?: "",
//                    price = it.getValue(Float::class.java) ?: 0f
//                )
//            }
//
//            yearlyData[year] = list
//        }
//
//        onResult(yearlyData)
//    }
//}
//
//@Composable
//fun MultiLineChart(data: Map<String, List<PricePoint>>) {
//
//    AndroidView(
//        factory = { context ->
//
//            LineChart(context).apply {
//
//                val lineDataSets = mutableListOf<LineDataSet>()
//
//                val monthOrder = listOf(
//                    "Jan","Feb","Mar","Apr","May","Jun",
//                    "Jul","Aug","Sep","Oct","Nov","Dec"
//                )
//
//                data.forEach { (year, list) ->
//
//                    // ✅ SORT HERE
//                    val sortedList = list.sortedBy { monthOrder.indexOf(it.month) }
//
//                    val maxPoint = sortedList.maxByOrNull { it.price }
//                    val minPoint = sortedList.minByOrNull { it.price }
//
//
//                    val entries = sortedList.mapIndexed { index, item ->
//                        Entry(index.toFloat(), item.price).apply {
//
//                            // Mark special points
//                            if (item == maxPoint) {
//                                data = "MAX"
//                            } else if (item == minPoint) {
//                                data = "MIN"
//                            }
//                        }
//                    }
//
//
//                    val dataSet = LineDataSet(entries, year).apply {
//                        lineWidth = 3f
//                        circleRadius = 5f
//                        setDrawValues(false)
//
//                        // Highlight colors
//                        setCircleColor(Color.Blue)
//                        highLightColor = Color.Red
//                    }
//
//
//                    lineDataSets.add(dataSet)
//                }
//
//
//                this.data = LineData(lineDataSets.map { it as ILineDataSet })
//
//                val months = data.values.firstOrNull()?.map { it.month } ?: emptyList()
//                xAxis.valueFormatter = IndexAxisValueFormatter(months)
//
//                description.isEnabled = false
//                axisRight.isEnabled = false
//                animateX(1000)
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(320.dp)
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CropSelector(
//    crops: List<String>,
//    selected: String,
//    onSelected: (String) -> Unit
//) {
//
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(expanded, { expanded = it }) {
//
//        OutlinedTextField(
//            value = selected,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Select Crop") },
//            modifier = Modifier.fillMaxWidth().menuAnchor(),
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
//            }
//        )
//
//        ExposedDropdownMenu(expanded, { expanded = false }) {
//            crops.forEach {
//                DropdownMenuItem(
//                    text = { Text(it) },
//                    onClick = {
//                        onSelected(it)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//fun analyzeYearComparison(data: Map<String, List<PricePoint>>): String {
//
//    if (data.size < 2) return "Not enough data"
//
//    val years = data.keys.sorted()
//    val lastYear = data[years.last()] ?: return ""
//    val prevYear = data[years[years.size - 2]] ?: return ""
//
//    val lastAvg = lastYear.map { it.price }.average()
//    val prevAvg = prevYear.map { it.price }.average()
//
//    return if (lastAvg > prevAvg) {
//        "📈 Prices improved compared to last year. Good time to hold crops."
//    } else {
//        "📉 Prices declined. Consider early selling."
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PriceTrendScreen(
//    onBackClick: () -> Unit
//) {
//
//    val crops = listOf(
//        "Wheat","Barley","Potato","Oilseed Rape",
//        "Maize","Oats","Sugar Beet","Peas","Beans","Carrots"
//    )
//
//    // Crop icons (can replace with real images later)
//    val cropIcons = mapOf(
//        "Wheat" to Icons.Default.Grass,
//        "Barley" to Icons.Default.Grass,
//        "Potato" to Icons.Default.Spa,
//        "Oilseed Rape" to Icons.Default.LocalFlorist,
//        "Maize" to Icons.Default.Agriculture,
//        "Oats" to Icons.Default.Grass,
//        "Sugar Beet" to Icons.Default.Eco,
//        "Peas" to Icons.Default.Spa,
//        "Beans" to Icons.Default.Spa,
//        "Carrots" to Icons.Default.LocalDining
//    )
//
//    var selectedCrop by remember { mutableStateOf("Wheat") }
//    var searchQuery by remember { mutableStateOf("") }
//    var data by remember { mutableStateOf<Map<String, List<PricePoint>>>(emptyMap()) }
//
//    val context = LocalContext.current
//
//    // 🔄 Fetch Firebase Data
//    LaunchedEffect(selectedCrop) {
//        fetchCropData(selectedCrop) {
//            data = it
//        }
//    }
//
//    val insight = remember(data) {
//        analyzeYearComparison(data)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Price Trends") },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                }
//            )
//        }
//    ) { padding ->
//
//        LazyColumn(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//        ) {
//
//            // 🌿 Header Card
//            item {
//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    shape = RoundedCornerShape(20.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//
//                        Text(
//                            "Crop Price Trends 🇬🇧",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Text(
//                            "Analyze price fluctuations and make better selling decisions",
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//
//            // 🔍 Searchable Dropdown
//            item {
//
//                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//
//                    OutlinedTextField(
//                        value = searchQuery,
//                        onValueChange = { searchQuery = it },
//                        label = { Text("Search Crop") },
//                        leadingIcon = {
//                            Icon(Icons.Default.Search, null)
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(16.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    val filtered = crops.filter {
//                        it.contains(searchQuery, ignoreCase = true)
//                    }
//
//                    LazyRow {
//                        items(filtered) { crop ->
//
//                            Card(
//                                modifier = Modifier
//                                    .padding(end = 8.dp)
//                                    .clickable {
//                                        selectedCrop = crop
//                                    },
//                                shape = RoundedCornerShape(16.dp),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = if (crop == selectedCrop)
//                                        Color(0xFF4CAF50).copy(alpha = 0.2f)
//                                    else Color.White
//                                )
//                            ) {
//
//                                Row(
//                                    modifier = Modifier.padding(12.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//
//                                    Icon(
//                                        cropIcons[crop] ?: Icons.Default.Eco,
//                                        contentDescription = null,
//                                        tint = Color(0xFF2E7D32)
//                                    )
//
//                                    Spacer(modifier = Modifier.width(6.dp))
//
//                                    Text(crop)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // 📊 Chart with Animation
//            item {
//
//                AnimatedContent(
//                    targetState = data,
//                    label = ""
//                ) { chartData ->
//
//                    Card(
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth(),
//                        shape = RoundedCornerShape(20.dp),
//                        elevation = CardDefaults.cardElevation(6.dp)
//                    ) {
//
//                        Column(modifier = Modifier.padding(16.dp)) {
//
//                            Text(
//                                "$selectedCrop Price Trend",
//                                fontWeight = FontWeight.Bold
//                            )
//
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            if (chartData.isNotEmpty()) {
//                                MultiLineChart(chartData)
//                            } else {
//                                Text("Loading...", color = Color.Gray)
//                            }
//                        }
//                    }
//                }
//            }
//
//            // 🤖 Insight Card
//            item {
//
//                Card(
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp, vertical = 8.dp),
//                    shape = RoundedCornerShape(20.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFFE3F2FD)
//                    )
//                ) {
//
//                    Column(modifier = Modifier.padding(16.dp)) {
//
//                        Text(
//                            "Smart Insights 🤖",
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Text(insight)
//                    }
//                }
//            }
//        }
//    }
//}
//
fun uploadCropData(context: Context) {

    val db = FirebaseDatabase.getInstance().reference
    val json = context.assets.open("crop_prices.json")
        .bufferedReader().use { it.readText() }

    val jsonObject = JSONObject(json)

    jsonObject.keys().forEach { crop ->

        val cropObj = jsonObject.getJSONObject(crop)

        cropObj.keys().forEach { year ->

            val yearObj = cropObj.getJSONObject(year)

            yearObj.keys().forEach { month ->

                val price = yearObj.getDouble(month)

                db.child("crop_prices")
                    .child(crop)
                    .child(year)
                    .child(month)
                    .setValue(price)
            }
        }
    }
}