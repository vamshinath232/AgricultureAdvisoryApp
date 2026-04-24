package s3605807.vamshinath.agricultureadvisoryapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavHostController,
    plant: String,
    plantConf: Float,
    disease: String,
    diseaseConf: Float,
    imageUriString: String
) {

    val decodedUri = URLDecoder.decode(imageUriString, StandardCharsets.UTF_8.toString())
    val diseaseConfidenceText = String.format("%.1f%%", diseaseConf * 100)

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Scan Result", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = p1
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            ImagePreviewModern(decodedUri, p1, p2)

            Spacer(Modifier.height(20.dp))

            ModernResultCard(
                title = "Disease Detected",
                value = disease.replace("___", " "),
                confidence = diseaseConfidenceText,
                icon = Icons.Default.BugReport,
                primaryColor = p1,
                secondaryColor = p2
            )

            Spacer(Modifier.height(16.dp))

            ModernResultCard(
                title = "Plant Identified",
                value = plant,
                confidence = String.format("%.1f%%", plantConf * 100),
                icon = Icons.Default.Grass,
                primaryColor = p1,
                secondaryColor = p2
            )

            Spacer(Modifier.height(24.dp))

            ActionButtonsModern(
                p1 = p1,
                navController = navController,
                plant = plant,
                disease = disease.replace("___", " "),
                confidence = diseaseConf,
                imageUri = decodedUri
            )
        }
    }
}


@Composable
fun ImagePreviewModern(uri: String, p1: Color, p2: Color) {

    Card(
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(p2.copy(0.2f), Color.White)
                    )
                )
        ) {

            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            // 🌿 Overlay badge
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .background(
                        p1.copy(alpha = 0.9f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Image,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Captured", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ModernResultCard(
    title: String,
    value: String,
    confidence: String,
    icon: ImageVector,
    primaryColor: Color,
    secondaryColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(primaryColor.copy(0.05f), secondaryColor.copy(0.05f))
                    )
                )
                .padding(18.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(icon, contentDescription = null, tint = primaryColor)

                Spacer(Modifier.width(8.dp))

                Text(
                    text = title,
                    color = primaryColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = confidence.replace("%", "").toFloat() / 100f,
                color = primaryColor,
                trackColor = primaryColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Confidence: $confidence",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ActionButtonsModern(
    p1: Color,
    navController: NavHostController,
    plant: String,
    disease: String,
    confidence: Float,
    imageUri: String
) {

    Column {

        Button(
            onClick = {
                navController.navigate("remedy/$disease")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = p1)
        ) {
            Icon(Icons.Default.Healing, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Find Treatment", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                val encodedUri = URLEncoder.encode(imageUri, StandardCharsets.UTF_8.toString())
                val encodedDisease = URLEncoder.encode(disease, StandardCharsets.UTF_8.toString())
                val encodedPlant = URLEncoder.encode(plant, StandardCharsets.UTF_8.toString())

                navController.navigate(
                    "save_report_screen/$encodedPlant/$encodedDisease/$confidence/$encodedUri"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(Icons.Default.Save, null, tint = p1)
            Spacer(Modifier.width(8.dp))
            Text("Save Report", color = p1)
        }
    }
}



