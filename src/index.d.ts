export interface ThumbnailResult {
  /** URI file:// vers l'image JPEG générée dans le cache */
  uri: string;
  /** Largeur en pixels */
  width: number;
  /** Hauteur en pixels */
  height: number;
}

/**
 * Génère une miniature JPEG d'une page d'un fichier PDF.
 *
 * @param pdfUri  URI du fichier PDF (file://, content://, ou chemin absolu)
 * @param page    Numéro de page (commence à 1)
 * @param quality Qualité JPEG de 0 à 100
 */
export function generate(pdfUri: string, page: number, quality: number): Promise<ThumbnailResult>;

declare const ScrodoPdfThumbnail: {
  generate(pdfUri: string, page: number, quality: number): Promise<ThumbnailResult>;
};

export default ScrodoPdfThumbnail;
