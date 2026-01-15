package com.virakumaro.testbookcabin.presentation.onlinecheckin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virakumaro.testbookcabin.R
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.ui.theme.DarkBlue
import com.virakumaro.testbookcabin.ui.theme.PrimaryOrange
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineCheckInScreen(
    onNavigateToDetails: (Booking) -> Unit,
    viewModel: OnlineCheckInViewModel = koinViewModel()
) {
    val state by viewModel.bookingState.collectAsState()

    LaunchedEffect(state) {
        if (state is Results.Success) {
            onNavigateToDetails((state as Results.Success).data)
            viewModel.setStateIdle()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(R.string.online_check_in),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 44.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Text(
            text = stringResource(R.string.pnr),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )

        OutlinedTextField(
            value = viewModel.pnr,
            onValueChange = { viewModel.pnr = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = DarkBlue,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp));

        Text(
            text = stringResource(R.string.last_name),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = DarkBlue,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (state is Results.Error) {
            val errorMsg = (state as Results.Error).message
            Text(
                text = errorMsg,
                color = PrimaryOrange,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
        }
        val isLoading = state is Results.Loading

        Button(
            onClick = { viewModel.findBooking() },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.conti), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }


    }
}