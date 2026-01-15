package com.virakumaro.testbookcabin.presentation.detail

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.virakumaro.testbookcabin.R
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.ui.theme.DarkBlue
import com.virakumaro.testbookcabin.ui.theme.PrimaryOrange
import com.virakumaro.testbookcabin.ui.theme.LightGray
import com.virakumaro.testbookcabin.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerDetailsScreen(
    booking: Booking?,
    onNavigateToSummary: () -> Unit,
    viewModel: PassengerDetailsViewModel
) {
    val view = LocalView.current
    val saveState by viewModel.saveState.collectAsState()

    val scrollState = rememberScrollState()

    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }

    LaunchedEffect(viewModel.validationErrorMessage) {
        if (viewModel.validationErrorMessage != null) {
            scrollState.animateScrollTo(0)
        }
    }

    LaunchedEffect(saveState) {
        if (saveState is Results.Success) {
            onNavigateToSummary()
            viewModel.setStateIdle()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Button(
                    enabled = saveState !is Results.Loading,
                    onClick = { booking?.let { viewModel.saveDetails(it) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryOrange,
                        disabledContainerColor = DarkBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .navigationBarsPadding()
                        .height(48.dp)
                ) {
                    if (saveState is Results.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "SAVE", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(144.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.passenger_details),
                    color = DarkBlue,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (booking != null) {
                Text(
                    text = "${booking.firstName} ${booking.lastName}",
                    color = DarkBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${booking.flightNumber}  ${booking.departureCity} to ${booking.arrivalCity}",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(24.dp))

            AnimatedVisibility(
                visible = viewModel.validationErrorMessage != null
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = PrimaryOrange,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = viewModel.validationErrorMessage ?: "",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.please_enter_ite_required_document_details_below),
                fontSize = 16.sp,
                color = DarkBlue
            )
            Spacer(Modifier.height(24.dp))

            DetailInputField(
                label = stringResource(R.string.passport_number),
                value = viewModel.passportField,
                onValueChange = { viewModel.passportField = it.uppercase() },
            )

            DetailInputField(
                label = stringResource(R.string.first_name),
                value = viewModel.firstNameField,
                onValueChange = { viewModel.firstNameField = it.uppercase() }
            )

            DetailInputField(
                label = stringResource(R.string.last_name),
                value = viewModel.lastNameField,
                onValueChange = { viewModel.lastNameField = it.uppercase() }
            )

            Text(text = stringResource(R.string.gender), style = MaterialTheme.typography.labelLarge, color = DarkBlue)
            Spacer(Modifier.height(8.dp))
            var expandedGender by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedGender.displayName,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = LightGray,
                        focusedTextColor = DarkBlue,
                        unfocusedTextColor = DarkBlue
                    )
                )
                ExposedDropdownMenu(expanded = expandedGender, onDismissRequest = { expandedGender = false }) {
                    Gender.entries.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender.displayName) },
                            onClick = {
                                viewModel.selectedGender = gender
                                expandedGender = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            DetailInputField(
                label = stringResource(R.string.contact_name),
                value = viewModel.contactNameField,
                onValueChange = { viewModel.contactNameField = it.uppercase() })
            DetailInputField(
                label = stringResource(R.string.contact_number),
                value = viewModel.contactNumberField,
                onValueChange = { viewModel.contactNumberField = it },
                keyboardType = KeyboardType.Number
            )
            DetailInputField(
                label = stringResource(R.string.relationship),
                value = viewModel.relationshipField,
                onValueChange = { viewModel.relationshipField = it.uppercase() })

            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
fun DetailInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = DarkBlue)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (keyboardType == KeyboardType.Number) {
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                } else {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = LightGray,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue
            )
        )
    }
}