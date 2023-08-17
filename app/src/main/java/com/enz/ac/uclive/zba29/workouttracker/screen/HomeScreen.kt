package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.enz.ac.uclive.zba29.workouttracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar (
                title = {Text(stringResource(R.string.app_name)) },
            )
        },
        content = {
            val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

            if (isLandscape) {
                LandscapeLayout(navController)
            } else {
                PortraitLayout(navController)
            }
        }
    )
}

@Composable
fun PortraitLayout(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val compositeResult : LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.home_icon))
        val progressAnimation by animateLottieCompositionAsState(
            compositeResult.value,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            speed = 1.0f
        )
        Box(
            modifier = Modifier.weight(0.7f)
        ){
            LottieAnimation(composition = compositeResult.value, progress = progressAnimation)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.weight(0.25f)
        ){
            Button(
                shape = RoundedCornerShape(25.dp),
                onClick = {
                navController.navigate(Screen.MainScreen.route)
            },) {
                Text(text = stringResource(R.string.home_button))
            }
        }

    }
}

@Composable
fun LandscapeLayout(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val compositeResult : LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.home_icon))
        val progressAnimation by animateLottieCompositionAsState(
            compositeResult.value,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            speed = 1.0f
        )
        Box(
            modifier = Modifier.weight(0.7f)
        ){
            LottieAnimation(composition = compositeResult.value, progress = progressAnimation)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.weight(0.25f)
        ){
            Button(
                shape = RoundedCornerShape(25.dp),
                onClick = {
                    navController.navigate(Screen.MainScreen.route)
                },) {
                Text(text = stringResource(R.string.home_button))
            }
        }

    }
}