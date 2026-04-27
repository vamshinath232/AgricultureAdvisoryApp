package s3605807.vamshinath.agricultureadvisoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import s3605807.vamshinath.agricultureadvisoryapp.cropAdvisory.CropAdvisoryScreen
import s3605807.vamshinath.agricultureadvisoryapp.mlhelper.RemedyScreen
import s3605807.vamshinath.agricultureadvisoryapp.mlhelper.SaveReportScreen
import s3605807.vamshinath.agricultureadvisoryapp.ui.theme.AgricultureAdvisoryAppTheme
import s3605807.vamshinath.agricultureadvisoryapp.weather.WeatherForecastScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            AgricultureAdvisoryAppTheme {
                AppNavigationMain()
            }
        }
    }
}


@Composable
fun AppNavigationMain() {

    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            LoadingLogic(
                onNavigate = {
                    if (UserDetails.findLoginStatus(context)) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            UserLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            CreateAccountScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            DashboardScreen(navController)
        }

        composable(Screen.CropAdvisory.route) {
            CropAdvisoryScreen(onBack = {
                navController.popBackStack()
            })
        }

        composable(Screen.WeatherForecast.route) {
            WeatherForecastScreen(onBack = {
                navController.popBackStack()
            })
        }

        composable(Screen.MarketTrend.route)
        {
            PriceTrendScreen(onBackClick = {
                navController.popBackStack()
            })
        }

        composable(Screen.PestDetection.route)
        {
            PlantScanScreen(
                navController
            )
        }

        composable(
            route = "scan_result/{plant}/{plantConf}/{disease}/{diseaseConf}/{imageUriString}",
            arguments = listOf(
                navArgument("plant") { type = NavType.StringType },
                navArgument("plantConf") { type = NavType.FloatType },
                navArgument("disease") { type = NavType.StringType },
                navArgument("diseaseConf") { type = NavType.FloatType },
                navArgument("imageUriString") { type = NavType.StringType } // New argument
            )
        ) { backStackEntry ->
            val plant = backStackEntry.arguments?.getString("plant") ?: "N/A"
            val plantConf = backStackEntry.arguments?.getFloat("plantConf") ?: 0.0f
            val disease = backStackEntry.arguments?.getString("disease") ?: "N/A"
            val diseaseConf = backStackEntry.arguments?.getFloat("diseaseConf") ?: 0.0f
            val imageUriString = backStackEntry.arguments?.getString("imageUriString") ?: ""

            ResultScreen(
                navController = navController,
                plant = plant,
                plantConf = plantConf,
                disease = disease,
                diseaseConf = diseaseConf,
                imageUriString = imageUriString
            )
        }

        composable("remedy/{disease}") { backStackEntry ->
            val disease = backStackEntry.arguments?.getString("disease") ?: "Unknown"
            RemedyScreen(navController, disease)
        }

        composable(
            route = "save_report_screen/{plant}/{disease}/{confidence}/{imageUri}",
            arguments = listOf(
                navArgument("plant") { type = NavType.StringType },
                navArgument("disease") { type = NavType.StringType },
                navArgument("confidence") { type = NavType.FloatType },
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val plantEncoded = backStackEntry.arguments?.getString("plant") ?: ""
            val diseaseEncoded = backStackEntry.arguments?.getString("disease") ?: ""

            val plant = URLDecoder.decode(plantEncoded, StandardCharsets.UTF_8.toString())
            val disease = URLDecoder.decode(diseaseEncoded, StandardCharsets.UTF_8.toString())

            val confidence = backStackEntry.arguments?.getFloat("confidence") ?: 0f

            val imageEncoded = backStackEntry.arguments?.getString("imageUri") ?: ""
            val imageUri = URLDecoder.decode(imageEncoded, StandardCharsets.UTF_8.toString())

            SaveReportScreen(
                navController = navController,
                plant = plant,
                disease = disease,
                confidence = confidence,
                imageUri = imageUri
            )
        }

        composable(Screen.SavedReports.route) {
            SavedReportsScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(UserDetails.findEmail(context)!!, navController)
        }

        composable(Screen.AboutApp.route)
        {
            AboutScreen(onBack = {
                navController.popBackStack()
            })
        }

    }
}


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")


    object PestDetection : Screen("pest_detection")
    object SavedReports : Screen("saved_reports")

    object CropAdvisory : Screen("crop_advisory")
    object WeatherForecast : Screen("weather_forecast")
    object MarketTrend : Screen("market_trend")
    object AboutApp : Screen("about_app")

}


@Composable
fun LoadingLogic(onNavigate: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(3000)
        onNavigate()
    }

    LaunchView()


}

@Composable
fun LaunchView() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.green_color_bg)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(26.dp)),

                ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green_color),
                        contentColor = colorResource(
                            id = R.color.white
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    Text(
                        text = "Agriculture Advisor App",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.agriculture_advisory),
                    contentDescription = "Agriculture Advisor App",
                )

                Spacer(modifier = Modifier.height(18.dp))

            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LaunchViewPreview() {
    LaunchView()
}