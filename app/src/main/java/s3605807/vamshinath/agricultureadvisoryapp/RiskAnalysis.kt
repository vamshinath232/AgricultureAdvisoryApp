package s3605807.vamshinath.agricultureadvisoryapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class PredictionResult(
    val nextMonths: List<String>,
    val predictedPrices: List<Float>
)

fun predictNext3Months(data: List<PricePoint>): PredictionResult {

    if (data.size < 3) return PredictionResult(emptyList(), emptyList())

    val sorted = sortByMonth(data)

    val last3 = sorted.takeLast(3)

    val trend = (last3.last().price - last3.first().price) / 2f

    val lastPrice = sorted.last().price

    val monthsOrder = listOf(
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    )

    val lastMonthIndex = monthsOrder.indexOf(sorted.last().month)

    val nextMonths = mutableListOf<String>()
    val predicted = mutableListOf<Float>()

    var currentPrice = lastPrice

    for (i in 1..3) {

        val nextIndex = (lastMonthIndex + i) % 12
        val month = monthsOrder[nextIndex]

        currentPrice += trend

        nextMonths.add(month)
        predicted.add(currentPrice)
    }

    return PredictionResult(nextMonths, predicted)
}

data class RiskResult(
    val volatility: Float,
    val riskLevel: String
)

fun calculateRisk(data: List<PricePoint>): RiskResult {

    val prices = data.map { it.price }

    val mean = prices.average().toFloat()

    val variance = prices.map {
        (it - mean) * (it - mean)
    }.average().toFloat()

    val stdDev = kotlin.math.sqrt(variance)

    val riskLevel = when {
        stdDev < 3 -> "Low Risk 🟢"
        stdDev < 7 -> "Medium Risk 🟡"
        else -> "High Risk 🔴"
    }

    return RiskResult(stdDev, riskLevel)
}

@Composable
fun PredictionCard(prediction: PredictionResult) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Future Prediction 🔮", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            prediction.nextMonths.zip(prediction.predictedPrices).forEach {
                Text("${it.first}: £${"%.2f".format(it.second)}")
            }
        }
    }
}

@Composable
fun RiskCard(risk: RiskResult) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (risk.riskLevel) {
                "Low Risk 🟢" -> Color(0xFFE8F5E9)
                "Medium Risk 🟡" -> Color(0xFFFFF8E1)
                else -> Color(0xFFFFEBEE)
            }
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Risk Analysis ⚠️", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Volatility: ${"%.2f".format(risk.volatility)}")
            Text("Risk Level: ${risk.riskLevel}")
        }
    }
}