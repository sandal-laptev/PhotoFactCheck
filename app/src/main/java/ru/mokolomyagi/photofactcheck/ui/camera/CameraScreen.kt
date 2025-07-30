package ru.mokolomyagi.photofactcheck.ui.camera

import android.Manifest
import android.net.Uri
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import ru.mokolomyagi.photofactcheck.datastore.UserPreferencesRepository
import ru.mokolomyagi.photofactcheck.util.CameraCaptureHelper

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = remember { UserPreferencesRepository(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraHelper = remember { CameraCaptureHelper(
        context.applicationContext, repository
    ) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var permissionsGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        permissionsGranted = perms.all { it.value }
        if (permissionsGranted) {
            startCamera(context, lifecycleOwner, onImageCaptureReady = {
                imageCapture = it
            })
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { previewView ->
                if (permissionsGranted) {
                    startCamera(context, lifecycleOwner, previewView) {
                        imageCapture = it
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            imageCapture?.let { capture ->
                cameraHelper.takePicture(capture) { file ->
                    file?.let {
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                        imageUri = uri
                    }
                }
            }
        }) {
            Text("Сделать снимок")
        }

        imageUri?.let { uri ->
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Фото",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clickable { showDialog = true }
            )
        }
    }

    if (showDialog && imageUri != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Fullscreen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    CameraScreen()
}