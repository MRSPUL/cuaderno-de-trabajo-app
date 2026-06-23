package com.cuadernodetrabajo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.cuadernodetrabajo.R
import com.cuadernodetrabajo.model.Corte
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cortes: List<Corte>,
    onNavigateToNewCorte: () -> Unit,
    onNavigateToDetalle: (String) -> Unit,

) {
    var textoBusqueda by remember { mutableStateOf("") }

    val cortesFiltrados = cortes.filter {
        it.numeroCorte.contains(textoBusqueda, ignoreCase = true)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Cortes", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewCorte,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar nuevo corte")
            }
        }
    ) { paddingValues ->

        if (cortes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_caja_home),
                    contentDescription = "Caja vacía",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("No hay cortes registrados", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tocá el botón + para agregar tu primer corte", fontSize = 14.sp, color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar por número de corte") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(cortesFiltrados) { corte ->
                        CorteCard(corte = corte, onNavigateToDetalle = onNavigateToDetalle)
                    }
                }
            }
        }
    }
}

@Composable
fun CorteCard(
    corte: Corte,
    onNavigateToDetalle: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onNavigateToDetalle(corte.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {

                if (corte.photoUri.isNotEmpty()) {
                    AsyncImage(
                        model = corte.photoUri.toUri(),
                        contentDescription = "Foto del corte",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${corte.marca} - Corte #${corte.numeroCorte}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${corte.cantidadCamisas}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaString = sdf.format(Date(corte.fechaCreacion))

                Text(
                    text = fechaString,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}