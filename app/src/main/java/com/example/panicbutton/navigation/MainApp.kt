package com.example.panicbutton.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.panicbutton.viewmodel.ViewModel
import com.example.panicbutton.prensentation.components.OnBoarding
import com.example.panicbutton.prensentation.screens.UserProfileScreen
import com.example.panicbutton.prensentation.screens.DetailRekapScreen
import com.example.panicbutton.prensentation.screens.DataRekapScreen
import com.example.panicbutton.prensentation.screens.DashboardAdminScreen
import com.example.panicbutton.prensentation.screens.DashboardUserScreen
import com.example.panicbutton.prensentation.screens.HelpScreen
import com.example.panicbutton.prensentation.screens.LoginScreen
import com.example.panicbutton.prensentation.screens.ResetPasswordScreen
import com.example.panicbutton.prensentation.screens.SignUpScreen
import com.example.panicbutton.viewmodel.ViewModelFactory

@Composable
fun MainApp() {
    val context = LocalContext.current // Dapatkan konteks
    val viewModel: ViewModel = viewModel(factory = ViewModelFactory(context)) // Inisialisasi ViewModel
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isOnboardingShown = sharedPreferences.getBoolean("OnBoardingShown", false)

    val navController = rememberNavController()


    // Cek status login
    val startDestination = when {
        !isOnboardingShown -> "onboarding"
        viewModel.isAdminLoggedIn() -> "dashboard_admin"
        viewModel.isUserLoggedIn() -> "dashboard"
        else -> "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        
        //onBoarding
        composable("onboarding") {
            OnBoarding(navController = navController)
            LaunchedEffect(Unit) {
                sharedPreferences.edit().putBoolean("OnBoardingShown", true).apply()
            }
        }
        
        // Halaman Sign Up
        composable("signup") {
            SignUpScreen(
                navController = navController,
                context = context
            )
        }

        // Halaman Login
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = viewModel, // Mengoper ViewModel ke LoginScreen
                context = context
            )
        }

        // Halaman Dashboard
        composable("dashboard") {
            DashboardUserScreen(
                context = context,
                viewModel = viewModel,
                navController = navController,
                onLogout = {
                    viewModel.logout() // Panggil fungsi logout dari ViewModel
                    navController.navigate("login") // Navigasi kembali ke layar login
                }
            )
        }

        composable("dashboard_admin") {
            DashboardAdminScreen(
                context = context,
                navController = navController,
                viewModel = viewModel,
                onLogout = {
                    viewModel.adminLogout()
                    navController.navigate("login")
                }
            )
        }

        composable("data_rekap") {
            DataRekapScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("detail_rekap/{houseNumber}") {backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("houseNumber")
            DetailRekapScreen(
                houseNumber = nomorRumah ?:"",
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("user_profile") {
            UserProfileScreen(
                context = context,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(
            "help",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))},
            exitTransition = {slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))}
        ) {
            HelpScreen(
                navController =navController
            )
        }
        composable("reset_password") {
            ResetPasswordScreen(
                context = context,
                navController = navController
            )
        }
    }
}


