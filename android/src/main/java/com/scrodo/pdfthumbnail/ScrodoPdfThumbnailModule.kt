package com.scrodo.pdfthumbnail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.facebook.react.bridge.*
import java.io.File
import java.io.FileOutputStream

class ScrodoPdfThumbnailModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String = "ScrodoPdfThumbnail"

  @ReactMethod
  fun generate(pdfUri: String, page: Int, quality: Int, promise: Promise) {
    Thread {
      try {
        val file = resolveFile(pdfUri)
        val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fd)

        val pageIndex = maxOf(0, page - 1)
        if (pageIndex >= renderer.pageCount) {
          renderer.close()
          fd.close()
          promise.reject("PAGE_OUT_OF_RANGE", "Page $page hors limites (${renderer.pageCount} pages)")
          return@Thread
        }

        val pdfPage = renderer.openPage(pageIndex)
        val scale = 2
        val width = pdfPage.width * scale
        val height = pdfPage.height * scale

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        pdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        pdfPage.close()
        renderer.close()
        fd.close()

        val qualityClamped = maxOf(0, minOf(100, quality))
        val cacheDir = reactApplicationContext.cacheDir
        val outputFile = File(cacheDir, "pdf_thumb_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, qualityClamped, out)
        out.flush()
        out.close()

        val result = WritableNativeMap().apply {
          putString("uri", "file://${outputFile.absolutePath}")
          putInt("width", width)
          putInt("height", height)
        }
        promise.resolve(result)
      } catch (e: Exception) {
        promise.reject("GENERATE_ERROR", e.message, e)
      }
    }.start()
  }

  private fun resolveFile(uri: String): File {
    return when {
      uri.startsWith("file://") -> File(Uri.parse(uri).path!!)
      uri.startsWith("/") -> File(uri)
      uri.startsWith("content://") -> {
        val inputStream = reactApplicationContext.contentResolver.openInputStream(Uri.parse(uri))
          ?: throw Exception("Impossible d'ouvrir: $uri")
        val tmpFile = File(reactApplicationContext.cacheDir, "pdf_input_${System.currentTimeMillis()}.pdf")
        tmpFile.outputStream().use { inputStream.copyTo(it) }
        tmpFile
      }
      else -> File(uri)
    }
  }
}
