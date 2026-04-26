package s3605807.vamshinath.agricultureadvisoryapp

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("About App", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

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
                            Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                "Agriculture Advisory App",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Text(
                                "Smart Farming Assistant",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            AboutCard(
                icon = Icons.Default.Description,
                title = "About Us",
                content = "The Agriculture Advisory App helps farmers access crop guidance, weather updates, pest control advice, and market prices in one place. It uses smart technology to support better decisions, improve productivity, and reduce farming risks.",
                color = p1
            )

            Spacer(Modifier.height(16.dp))

            AboutCard(
                icon = Icons.Default.Person,
                title = "Developed By",
                content = "Vamshinath Yadav Nidhuramolu (S3605807)",
                color = p1
            )

            Spacer(Modifier.height(16.dp))

            AboutCard(
                icon = Icons.Default.ContactMail,
                title = "Contact Us",
                content = "Email: Vamshinath232@gmail.com\nLocation: Teesside University, Middlesbrough, UK",
                color = p1
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun AboutCard(
    icon: ImageVector,
    title: String,
    content: String,
    color: Color
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, color.copy(alpha = 0.05f))
                    )
                )
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(icon, contentDescription = null, tint = color)

                Spacer(Modifier.width(8.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = color
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = content,
                color = Color.DarkGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}