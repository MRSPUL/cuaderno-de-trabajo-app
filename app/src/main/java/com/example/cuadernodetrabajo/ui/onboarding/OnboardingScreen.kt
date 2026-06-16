package com.example.cuadernodetrabajo.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.cuadernodetrabajo.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.ic_libro_onboarding,
            title = "¡Hola! Tu nuevo cuaderno de trabajo",
            description = "Acá vas a poder llevar el control de todos los cortes que termines de planchar de forma rápida y sin complicaciones."
        ),
        OnboardingPage(
            image = R.drawable.ic_camara_onboarding,
            title = "Sacá foto y anotá la cantidad",
            description = "Cuando termines un lote, sacale una foto de muestra, escribí de qué marca es y cuántas camisas planchaste. ¡Así de fácil!"
        ),
        OnboardingPage(
            image = R.drawable.ic_check_onboarding,
            title = "Todo guardado y seguro",
            description = "Olvidate de perder los papeles. Todo tu historial de trabajo queda guardado acá para que lo consultes cuando quieras."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Pager deslizable
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { position ->
            PagerScreen(page = pages[position])
        }

        // Indicadores (puntitos) y Botón
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicadores
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }

            // Botón "Siguiente" o "Empezar a trabajar!"
            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.size - 1) "¡Empezar a trabajar!" else "Siguiente",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun PagerScreen(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = page.image),
            contentDescription = "Onboarding Icon",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}