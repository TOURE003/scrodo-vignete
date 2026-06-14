package com.scrodo.pdfthumbnail

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.facebook.react.bridge.*
import java.io.File
import java.io.FileOutputStream

class ScrodoPdfThumbnailModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "ScrodoPdfThumbnail"

    @ReactMethod
    fun generate(pdfUri: String, page: Int, quality: Int, promise: Promise) {
        try {
            val file = resolveFile(pdfUri)
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)

            val pageIndex = (page - 1).coerceAtLeast(0)
            if (pageIndex >= renderer.pageCount) {
                renderer.close()
                pfd.close()
                promise.reject("PAGE_OUT_OF_RANGE", "Page $page hors limites (${renderer.pageCount} pages)")
                return
            }

            val pdfPage = renderer.openPage(pageIndex)
            val width = pdfPage.width
            val height = pdfPage.height

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(Color.WHITE)
            pdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfPage.close()
            renderer.close()
            pfd.close()

            // Sauvegarder en JPEG dans le cache
            val cacheDir = reactContext.cacheDir
            val outputFile = File(cacheDir, "pdf_thumb_${System.currentTimeMillis()}.jpg")
            val fos = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(0, 100), fos)
            fos.flush()
            fos.close()
            bitmap.recycle()

            val result = Arguments.createMap().apply {
                putString("uri", "file://${outputFile.absolutePath}")
                putInt("width", width)
                putInt("height", height)
            }
            promise.resolve(result)
        } catch (e: Exception) {
            promise.reject("GENERATE_ERROR", e.message ?: "Erreur inconnue", e)
        }
    }

    private fun resolveFile(uri: String): File {
        return when {
            uri.startsWith("file://") -> File(uri.removePrefix("file://"))
            uri.startsWith("content://") -> {
                val contentUri = Uri.parse(uri)
                val inputStream = reactContext.contentResolver.openInputStream(contentUri)
                    ?: throw Exception("Impossible d'ouvrir le fichier: $uri")
                val tempFile = File(reactContext.cacheDir, "pdf_temp_${System.currentTimeMillis()}.pdf")
                tempFile.outputStream().use { output -> inputStream.copyTo(output) }
                inputStream.close()
                tempFile
            }
            else -> File(uri)
        }
    }
}
