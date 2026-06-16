package com.example.cuadernodetrabajo.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.cuadernodetrabajo.ui.CorteViewModel
import com.example.cuadernodetrabajo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCorteScreen(
    corteId: Int,
    viewModel: CorteViewModel,
    onNavigateBack: () -> Unit
) {
    // Buscamos el corte seleccionado desde el estado del ViewModel
    val corteList by viewModel.allCortes.collectAsState()
    val corte = corteList.find { it.id == corteId }


    // mostramos un indicador de carga mientras Room lee la base de datos.
    if (corte == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return // Pausamos la construcción de esta vista hasta que 'corte' tenga datos
    }

    // Estados para la edición
    var isEditing by remember { mutableStateOf(false) }
    var marca by remember { mutableStateOf(corte.marca) }
    var numeroCorte by remember { mutableStateOf(corte.numeroCorte) }
    var cantidad by remember { mutableStateOf(corte.cantidadCamisas.toString()) }
    var datosAdicionales by remember { mutableStateOf(corte.datosAdicionales) }

    // Estado para el diálogo de eliminación (Frame 10 de tu Figma)
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar corte?") },
            text = { Text("Esta acción no se puede deshacer. ¿Estás seguro de que querés eliminar este corte?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.delete(corte)
                        showDeleteDialog = false
                        onNavigateBack()
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Estado para el cartel de edición exitosa
    var showSuccessDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    // Cartel de éxito (El mismo que usamos para crear)
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { /* Bloqueamos toque afuera */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_onboarding),
                    contentDescription = "Éxito",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
            },
            title = {
                Text(
                    text = "¡Corte guardado\ncorrectamente!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack() // Volvemos al historial al tocar el botón
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Volver al inicio", color = Color.White)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Corte", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar la foto si existe usando Coil
            if (corte.photoUri != null) {
                AsyncImage(
                    model = corte.photoUri.toUri(),
                    contentDescription = "Foto del corte",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing // Se activa solo si tocamos "Editar"
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numeroCorte,
                onValueChange = { if (it.all { char -> char.isDigit() }) numeroCorte = it },
                label = { Text("Número de corte") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = { if (it.all { char -> char.isDigit() }) cantidad = it },
                label = { Text("Cantidad de camisas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = datosAdicionales,
                onValueChange = { datosAdicionales = it },
                label = { Text("Datos adicionales") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = isEditing
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones inferiores dinámicos
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            // Restaurar valores originales y salir de edición
                            marca = corte.marca
                            numeroCorte = corte.numeroCorte
                            cantidad = corte.cantidadCamisas.toString()
                            datosAdicionales = corte.datosAdicionales
                            isEditing = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            // Validamos que no hayan dejado la marca, número o cantidad vacíos
                            if (marca.isBlank() || numeroCorte.isBlank() || cantidad.isBlank()) {
                                android.widget.Toast.makeText(
                                    context,
                                    "⚠️ La marca, el número y la cantidad son obligatorios",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val corteActualizado = corte.copy(
                                    marca = marca,
                                    numeroCorte = numeroCorte,
                                    cantidadCamisas = cantidad.toIntOrNull() ?: 0,
                                    datosAdicionales = datosAdicionales
                                )
                                viewModel.insert(corteActualizado)
                                isEditing = false
                                showSuccessDialog = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            } else {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Editar", color = Color.White)
                }
            }
        }
    }
}