package s3605807.vamshinath.agricultureadvisoryapp.mlhelper

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import s3605807.vamshinath.agricultureadvisoryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemedyScreen(
    navController: NavHostController,
    diseaseName: String
) {

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    val info = DiseaseInfoProvider.getInfo(diseaseName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(info.name, color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = p1)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            HeaderCard(info.name, p1, p2)

            Spacer(Modifier.height(20.dp))

            PremiumSection(
                title = "Symptoms",
                icon = Icons.Default.Warning,
                items = info.symptoms,
                color = Color(0xFFD32F2F)
            )

            Spacer(Modifier.height(16.dp))

            PremiumSection(
                title = "General Management",
                icon = Icons.Default.Build,
                items = info.management,
                color = p1
            )

            Spacer(Modifier.height(16.dp))

            PremiumSection(
                title = "Chemical Treatment",
                icon = Icons.Default.Science,
                items = info.chemical,
                color = Color(0xFFD32F2F)
            )

            Spacer(Modifier.height(16.dp))

            PremiumSection(
                title = "Organic Treatment",
                icon = Icons.Default.Eco,
                items = info.organic,
                color = p1
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderCard(title: String, p1: Color, p2: Color) {

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(p1, p2))
                )
                .padding(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.LocalFlorist,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        "Treatment Guide",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )

                    Text(
                        title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}




@Composable
fun PremiumSection(
    title: String,
    icon: ImageVector,
    items: List<String>,
    color: Color
) {

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(icon, contentDescription = null, tint = color)

            Spacer(Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, color.copy(alpha = 0.05f))
                        )
                    )
                    .padding(16.dp)
            ) {

                items.forEach { point ->

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {

                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

