package com.virakumaro.testbookcabin.presentation.checkin

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.virakumaro.testbookcabin.R
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.ui.theme.DarkBlue
import com.virakumaro.testbookcabin.ui.theme.PrimaryOrange
import com.virakumaro.testbookcabin.ui.theme.White

@Composable
fun CheckInSummaryScreen(
    booking: Booking?,
    onNavigateToBoardingPass: () -> Unit,
    viewModel: CheckInSummaryViewModel
) {
    val saveState by viewModel.checkInState.collectAsState()

    LaunchedEffect(saveState) {
        if (saveState is Results.Success) {
            onNavigateToBoardingPass()
            viewModel.setStateIdle()
        }
    }

    CheckInSummaryContent(
        booking = booking,
        saveState = saveState,
        onCheckInClick = {
            booking?.let { viewModel.performCheckIn(it.passengerId) }
        }
    )
}

@Composable
fun CheckInSummaryContent(
    booking: Booking?,
    saveState: Results<BoardingPass>?,
    onCheckInClick: () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(all = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.check_in),
                color = White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (booking != null) {
            Text(
                text = "${booking.firstName} ${booking.lastName}",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${booking.flightNumber}  ${booking.departureCity} to ${booking.arrivalCity}",
                fontSize = 18.sp,
                color = White,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            enabled = saveState !is Results.Loading,
            onClick = onCheckInClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryOrange,
                disabledContainerColor = DarkBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .navigationBarsPadding()
                .height(48.dp)
        ) {
            if (saveState is Results.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = stringResource(R.string.check_in_button),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CheckInSummaryPreview() {
    val dummyBooking = Booking(
        pnr = "ABC1234",
        firstName = "JOHN",
        lastName = "DOE",
        passengerId = "p01.01",
        flightNumber = "OD 202",
        departureCity = "KUL",
        arrivalCity = "PEN",
        departureTime = "10:00 AM",
        documentId = "", documentType = "", emergencyContactId = "",
        dateOfBirth = "", expiryDate = "", nationality = "", gender = "", weightCategory = ""
    )

    CheckInSummaryContent(
        booking = dummyBooking,
        saveState = null,
        onCheckInClick = {}
    )
}
