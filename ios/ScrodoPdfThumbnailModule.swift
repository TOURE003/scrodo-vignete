import Foundation
import PDFKit
import UIKit

@objc(ScrodoPdfThumbnailModule)
class ScrodoPdfThumbnailModule: NSObject {

  @objc static func requiresMainQueueSetup() -> Bool { return false }

  @objc func generate(
    _ pdfUri: String,
    page: Int,
    quality: Int,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    DispatchQueue.global(qos: .userInitiated).async {
      do {
        let url = self.resolveURL(pdfUri)
        guard let document = PDFDocument(url: url) else {
          reject("OPEN_ERROR", "Impossible d'ouvrir le PDF: \(pdfUri)", nil)
          return
        }

        let pageIndex = max(0, page - 1)
        guard pageIndex < document.pageCount,
              let pdfPage = document.page(at: pageIndex) else {
          reject("PAGE_OUT_OF_RANGE", "Page \(page) hors limites (\(document.pageCount) pages)", nil)
          return
        }

        let pageRect = pdfPage.bounds(for: .mediaBox)
        let scale: CGFloat = 2.0
        let width = pageRect.width * scale
        let height = pageRect.height * scale

        let renderer = UIGraphicsImageRenderer(size: CGSize(width: width, height: height))
        let image = renderer.image { ctx in
          UIColor.white.set()
          ctx.fill(CGRect(x: 0, y: 0, width: width, height: height))
          ctx.cgContext.translateBy(x: 0, y: height)
          ctx.cgContext.scaleBy(x: scale, y: -scale)
          pdfPage.draw(with: .mediaBox, to: ctx.cgContext)
        }

        let qualityClamped = Double(max(0, min(100, quality))) / 100.0
        guard let jpegData = image.jpegData(compressionQuality: qualityClamped) else {
          reject("ENCODE_ERROR", "Impossible d'encoder l'image en JPEG", nil)
          return
        }

        let cacheDir = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first!
        let outputURL = cacheDir.appendingPathComponent("pdf_thumb_\(Int(Date().timeIntervalSince1970 * 1000)).jpg")
        try jpegData.write(to: outputURL)

        resolve([
          "uri": outputURL.absoluteString,
          "width": Int(width),
          "height": Int(height)
        ])
      } catch {
        reject("GENERATE_ERROR", error.localizedDescription, error)
      }
    }
  }

  private func resolveURL(_ uri: String) -> URL {
    if uri.hasPrefix("file://") {
      return URL(string: uri)!
    } else if uri.hasPrefix("/") {
      return URL(fileURLWithPath: uri)
    } else {
      return URL(string: uri)!
    }
  }
}
