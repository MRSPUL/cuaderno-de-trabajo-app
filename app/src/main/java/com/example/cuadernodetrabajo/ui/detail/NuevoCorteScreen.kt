package com.example.cuadernodetrabajo.ui.detail

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.cuadernodetrabajo.R
import com.example.cuadernodetrabajo.model.Corte
import com.example.cuadernodetrabajo.ui.CorteViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", cacheDir)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoCorteScreen(
    viewModel: CorteViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var marca by remember { mutableStateOf("") }
    var numeroCorte by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var datosAdicionales by remember { mutableStateOf("") }

    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = tempPhotoUri
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
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
                        onNavigateBack()
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
                title = { Text("Nuevo Corte", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
                    .clickable {
                        val file = context.createImageFile()
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                        tempPhotoUri = uri
                        cameraLauncher.launch(uri)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Foto del corte",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camara_onboarding),
                            contentDescription = "Cámara",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("Tocar para sacar foto", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = numeroCorte,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) numeroCorte = it
                },
                label = { Text("Número de corte") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) cantidad = it
                },
                label = { Text("Cantidad de camisas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = datosAdicionales,
                onValueChange = { datosAdicionales = it },
                label = { Text("Datos adicionales") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (photoUri == null || marca.isBlank() || numeroCorte.isBlank() || cantidad.isBlank()) {
                        // Mostramos el cartel de advertencia actualizado
                        android.widget.Toast.makeText(
                            context,
                            "Falta completar: Foto, Marca, Número o Cantidad",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // 2. Si todo está bien, guardamos en la base de datos
                        val nuevoCorte = Corte(
                            marca = marca,
                            numeroCorte = numeroCorte,
                            cantidadCamisas = cantidad.toIntOrNull() ?: 0,
                            datosAdicionales = datosAdicionales,
                            photoUri = photoUri.toString()
                        )
                        viewModel.insert(nuevoCorte)

                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}