package com.virakumaro.testbookcabin.presentation.boardingpass


import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.virakumaro.testbookcabin.R
import com.virakumaro.testbookcabin.domain.model.BoardingPass
import com.virakumaro.testbookcabin.ui.theme.Cream
import com.virakumaro.testbookcabin.ui.theme.DarkBlue
import com.virakumaro.testbookcabin.ui.theme.PrimaryOrange
import com.virakumaro.testbookcabin.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun BoardingPassScreen(
    boardingPass: BoardingPass?
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.boarding_pass),
            color = DarkBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 32.dp)
        )

        if (boardingPass != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Cream),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryOrange)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = boardingPass.passengerName.uppercase(),
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = boardingPass.flightNumber,
                                fontSize = 16.sp,
                                color = DarkBlue
                            )

                            Text(
                                text = stringResource(R.string.seat),
                                fontSize = 16.sp,
                                color = DarkBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = boardingPass.gate,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue
                            )

                            Text(
                                text = boardingPass.seatNumber,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue
                            )
                        }


                    }
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = Cream),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {

                    DashedDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = PrimaryOrange
                    )


                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = boardingPass.origin,
                                fontSize = 16.sp,
                                color = DarkBlue
                            )

                            Text(
                                text = boardingPass.destination,
                                fontSize = 16.sp,
                                color = DarkBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        if(!view.isInEditMode) {
                            BarcodeView(
                                data = boardingPass.barcodeString
                            )
                        }


                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Boarding Pass Data Found", color = Color.Gray)
            }
        }
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 5.dp,
    gapLength: Dp = 5.dp,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()

        drawLine(
            color = color,
            start = Offset(0f, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = thickness.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashPx, gapPx),
                phase = 0f
            )
        )
    }
}

@Composable
fun BarcodeView(
    data: String,
    modifier: Modifier = Modifier,
    format: BarcodeFormat = BarcodeFormat.CODE_128,
    heightDp: Dp = 60.dp
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val widthDp = maxWidth
        val density = LocalDensity.current

        val widthPx = with(density) { widthDp.roundToPx() }
        val heightPx = with(density) { heightDp.roundToPx() }

        var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(data, format, widthPx, heightPx) {
            barcodeBitmap = generateBarcodeBitmap(data, format, widthPx, heightPx)
        }

        barcodeBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Barcode $data",
                modifier = Modifier.fillMaxWidth().height(heightDp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

suspend fun generateBarcodeBitmap(
    contents: String,
    format: BarcodeFormat,
    width: Int,
    height: Int
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val writer = MultiFormatWriter()
        val bitMatrix: BitMatrix = writer.encode(contents, format, width, height)

        val w = bitMatrix.width
        val h = bitMatrix.height
        val pixels = IntArray(w * h)

        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix[x, y]) {
                    Color.Black.toArgb()
                } else {
                    Color.Transparent.toArgb()
                }
            }
        }

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return@withContext bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext null
    }
}

@Preview(showBackground = true)
@Composable
fun BoardingPassScreenPreview() {
    val dummyBoardingPass = BoardingPass(
        passengerName = "John Doe",
        flightNumber = "OD 202",
        seatNumber = "12A",
        origin = "CGK",
        destination = "SIN",
        boardingTime = "14:00",
        terminal = "3",
        gate = "G12",
        barcodeString = "M1DOEJ/JOHN      EABC123 CGKSINOD 0202 100"
    )
    BoardingPassScreen(boardingPass = dummyBoardingPass)
}