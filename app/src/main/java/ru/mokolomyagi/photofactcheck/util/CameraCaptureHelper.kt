package ru.mokolomyagi.photofactcheck.util

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mokolomyagi.photofactcheck.datastore.UserPreferencesRepository
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraCaptureHelper(
    private val context: Context,
    private val repository: UserPreferencesRepository
) {

    private fun getLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)

        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null) return location
            }
        }
        return null
    }

    private fun addWatermarkToBitmap(src: Bitmap, text: String): Bitmap {
        val result = src.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        val textSize = 40f * 3  // Увеличение размера шрифта в 3 раза

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 6f
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val lines = text.split("\n")
        val lineHeight = textSize + 10f
        var y = result.height - lineHeight * lines.size

        for (line in lines) {
            canvas.drawText(line, 20f, y, strokePaint)
            canvas.drawText(line, 20f, y, textPaint)
            y += lineHeight
        }

        return result
    }

    suspend fun getUserFormat(): GeoFormatConverter.Format {
        val value = repository.coordFormatFlow.first()
        return GeoFormatConverter.Format.fromValue(value)
    }

    fun saveToCache(file: File): File {
        val cacheDir = context.cacheDir
        val cacheFile = File(cacheDir, file.name)
        file.copyTo(cacheFile, overwrite = true)
        return cacheFile
    }

    fun takePicture(imageCapture: ImageCapture, onComplete: (File?) -> Unit) {
        val outputFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val bitmap = BitmapFactory.decodeFile(outputFile.absolutePath)
                            val location = getLocation()

                            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                .format(Date())

                            val format = getUserFormat()
                            val locationText = if (location != null)
                                GeoFormatConverter.formatLocation(location, format)
                            else "Location: Unknown"

                            val watermarkText = "$timestamp\n$locationText"
                            val watermarkedBitmap = addWatermarkToBitmap(bitmap, watermarkText)

                            FileOutputStream(outputFile).use {
                                watermarkedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
                            }

                            val exif = ExifInterface(outputFile.absolutePath)
                            exif.setAttribute(ExifInterface.TAG_DATETIME, timestamp)
                            if (location != null) exif.setGpsInfo(location)
                            exif.saveAttributes()

                            withContext(Dispatchers.Main) {
                                onComplete(outputFile)
                            }
                        } catch (e: Exception) {
                            Log.e("CameraCaptureHelper", "Error processing photo", e)
                            withContext(Dispatchers.Main) {
                                onComplete(null)
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraCaptureHelper", "Capture failed", exception)
                    onComplete(null)
                }
            }
        )
    }
}