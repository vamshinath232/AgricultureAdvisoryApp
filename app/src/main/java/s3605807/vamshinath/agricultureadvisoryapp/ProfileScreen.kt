package s3605807.vamshinath.agricultureadvisoryapp

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userEmail: String,
    navController: NavHostController
) {

    val p1 = Color(0xFF4CAF50)
    val p2 = Color(0xFF81C784)

    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("FarmerAccounts")

    var userData by remember { mutableStateOf(TesterData()) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        database.child(userEmail.replace(".", ","))
            .get()
            .addOnSuccessListener {
                userData = it.getValue(TesterData::class.java) ?: TesterData()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Profile", color = Color.White)
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
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                userData.name,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                userData.emailid,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    ProfileItem(Icons.Default.Person, "Name", userData.name)
                    Spacer(Modifier.height(12.dp))

                    ProfileItem(Icons.Default.Email, "Email", userData.emailid)
                    Spacer(Modifier.height(12.dp))

                    ProfileItem(Icons.Default.LocationOn, "Place", userData.place)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Update Password",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = p1
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Old Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("New Password") },
                leadingIcon = { Icon(Icons.Default.LockReset, null) },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {

                    if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (oldPassword != userData.password) {
                        Toast.makeText(context, "Old password is incorrect", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    database.child(userEmail.replace(".", ","))
                        .child("password")
                        .setValue(newPassword)
                        .addOnSuccessListener {
                            showSuccessDialog = true
                            oldPassword = ""
                            newPassword = ""
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = p1)
            ) {

                Icon(Icons.Default.LockReset, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Update Password", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))


            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Logout, null, tint = Color.Red)
                Spacer(Modifier.width(8.dp))
                Text("Logout", color = Color.Red)
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {

                    showLogoutDialog = false

                    UserDetails.saveUserLoginStatus(context, false)

                    // Navigate to login screen
                    navController.navigate("login") {
                        popUpTo(0)
                    }

                }) {
                    Text("Logout", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Success") },
            text = { Text("Password updated successfully") }
        )
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    value: String
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(icon, contentDescription = null, tint = Color(0xFF4CAF50))

        Spacer(Modifier.width(10.dp))

        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}