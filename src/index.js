import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  "scrodo-pdf-thumbnail: module natif non lié. " +
  "Rebuilde l'app après installation.";

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

export interface ThumbnailResult {
  uri: string;
  width: number;
  height: number;
}

/**
 * Génère un thumbnail JPEG de la première page d'un PDF
 * @param pdfUri - URI du fichier PDF (file://, content://)
 * @param page   - Numéro de page, base 1 (défaut: 1)
 * @param quality - Qualité JPEG 0-100 (défaut: 80)
 */
export async function generate(
  pdfUri: string,
  page: number = 1,
  quality: number = 80
): Promise<ThumbnailResult> {
  return ScrodoPdfThumbnail.generate(pdfUri, page, quality);
}

export default { generate };
