export interface ThumbnailResult {
  uri: string;
  width: number;
  height: number;
}

export function generate(
  pdfUri: string,
  page?: number,
  quality?: number
): Promise<ThumbnailResult>;

declare const ScrodoPdfThumbnail: {
  generate: (pdfUri: string, page?: number, quality?: number) => Promise<ThumbnailResult>;
};

export default ScrodoPdfThumbnail;