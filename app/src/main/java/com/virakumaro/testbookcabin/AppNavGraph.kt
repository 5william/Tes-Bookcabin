package com.virakumaro.testbookcabin


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.virakumaro.testbookcabin.presentation.checkin.CheckInSummaryScreen
import com.virakumaro.testbookcabin.presentation.checkin.CheckInSummaryViewModel
import com.virakumaro.testbookcabin.presentation.detail.PassengerDetailsScreen
import com.virakumaro.testbookcabin.presentation.detail.PassengerDetailsViewModel
import com.virakumaro.testbookcabin.presentation.onlinecheckin.OnlineCheckInScreen
import com.virakumaro.testbookcabin.presentation.onlinecheckin.OnlineCheckInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "online_check_in"
    ) {
        composable("online_check_in") {
            val viewModel: OnlineCheckInViewModel = koinViewModel()

            OnlineCheckInScreen(
                viewModel = viewModel,
                onNavigateToDetails = {
                    navController.navigate("passenger_details")
                }
            )
        }

        composable("passenger_details") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("online_check_in")
            }

            val prevViewModel: OnlineCheckInViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
            val viewModel: PassengerDetailsViewModel = koinViewModel()

            PassengerDetailsScreen(
                booking = prevViewModel.selectedBooking,
                onNavigateToSummary = {
                     navController.navigate("check_in_summary")
                },
                viewModel = viewModel
            )
        }

        composable("check_in_summary") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("online_check_in")
            }
            val sharedVm: OnlineCheckInViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
            val viewModel: CheckInSummaryViewModel = koinViewModel()

            CheckInSummaryScreen(
                booking = sharedVm.selectedBooking,
                onNavigateToBoardingPass = {
                     navController.navigate("boarding_pass")
                },
                viewModel
            )
        }

        composable("boarding_pass") {

        }
    }
}