package s3605807.vamshinath.agricultureadvisoryapp.mlhelper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grass
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import s3605807.vamshinath.agricultureadvisoryapp.data.Report
import s3605807.vamshinath.agricultureadvisoryapp.data.SaveReportViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveReportScreen(
    navController: NavHostController,
    plant: String,
    disease: String,
    confidence: Float,
    imageUri: String,
    viewModel: SaveReportViewModel = viewModel()
) {

    val context = LocalContext.current
    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    val snackbarHostState = remember { SnackbarHostState() }
    val saveState by viewModel.saveState.collectAsState()

    val notes = remember { mutableStateOf("") }

    LaunchedEffect(saveState) {
        if (saveState) {
            snackbarHostState.showSnackbar("Report Saved Successfully!")
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Save Report", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = p1)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // 🌿 IMAGE PREVIEW (PREMIUM)
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay label
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
                            Icon(Icons.Default.Image, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Captured", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🌿 INFO CARD (PLANT + DISEASE + CONFIDENCE)
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.White, p2.copy(alpha = 0.1f))
                            )
                        )
                        .padding(18.dp)
                ) {

                    InfoRow(Icons.Default.Grass, "Plant", plant, p1)
                    Spacer(Modifier.height(10.dp))

                    InfoRow(Icons.Default.BugReport, "Disease", disease, p1)
                    Spacer(Modifier.height(10.dp))

                    InfoRow(
                        Icons.Default.Analytics,
                        "Confidence",
                        String.format("%.1f%%", confidence * 100),
                        p1
                    )

                    Spacer(Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = confidence,
                        color = p1,
                        trackColor = p1.copy(alpha = 0.2f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 📝 NOTES FIELD (PREMIUM)
            OutlinedTextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Add Notes") },
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = null)
                },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // 🔥 SAVE BUTTON (PREMIUM)
            Button(
                onClick = {
                    val report = Report(
                        plant = plant,
                        disease = disease,
                        confidence = confidence,
                        imageUri = imageUri,
                        notes = notes.value,
                        date = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                        ).format(Date())
                    )
                    viewModel.saveReport(report)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = p1)
            ) {
                Icon(Icons.Default.Save, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Save Report", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(icon, contentDescription = null, tint = color)

        Spacer(Modifier.width(10.dp))

        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}