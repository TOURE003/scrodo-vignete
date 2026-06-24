import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  "scrodo-pdf-thumbnail: module natif non lié.\n" +
  "Rebuilde l'app après installation :\n" +
  (Platform.OS === 'ios'
    ? "  cd ios && pod install && cd ..\n  npx expo run:ios\n"
    : "  npx expo run:android\n");

const ScrodoPdfThumbnail = NativeModules.ScrodoPdfThumbnail
  ? NativeModules.ScrodoPdfThumbnail
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * Génère une miniature JPEG d'une page d'un fichier PDF.
 *
 * @param pdfUri  URI du fichier PDF (file://, content://, ou chemin absolu)
 * @param page    Numéro de page (commence à 1)
 * @param quality Qualité JPEG de 0 à 100
 * @returns       { uri: string, width: number, height: number }
 */
export function generate(pdfUri, page, quality) {
  return ScrodoPdfThumbnail.generate(pdfUri, page, quality);
}

export default ScrodoPdfThumbnail;
