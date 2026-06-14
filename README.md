# scrodo-pdf-thumbnail

Module React Native natif pour générer un thumbnail JPEG de n'importe quelle page d'un PDF.

- **Android** : utilise `PdfRenderer` (API Android native, aucune dépendance externe)
- **iOS** : utilise `PDFKit` (framework Apple natif, aucune dépendance externe)
- Compatible **Expo SDK 56+**, **React Native 0.85+**
- Zéro conflit de dépendances

## Installation

```bash
npm install github:TON_USERNAME/scrodo-pdf-thumbnail
```

Puis rebuild l'app (obligatoire pour les modules natifs) :

```bash
eas build --platform android
# ou
npx expo run:android
```

## Utilisation

```typescript
import ScrodoPdfThumbnail from 'scrodo-pdf-thumbnail';

const result = await ScrodoPdfThumbnail.generate(pdfUri, 1, 80);
// result.uri    → chemin vers le JPEG généré
// result.width  → largeur en pixels
// result.height → hauteur en pixels
```

### Paramètres

| Paramètre | Type | Défaut | Description |
|-----------|------|--------|-------------|
| `pdfUri` | `string` | — | URI du PDF (`file://`, `content://`, chemin absolu) |
| `page` | `number` | `1` | Numéro de page (base 1) |
| `quality` | `number` | `80` | Qualité JPEG (0-100) |

## Exemple complet

```typescript
import ScrodoPdfThumbnail from 'scrodo-pdf-thumbnail';

export default async function pdfToImage(pdfUri: string) {
  try {
    const { uri, width, height } = await ScrodoPdfThumbnail.generate(pdfUri, 1, 80);
    return {
      succes: true,
      error: null,
      message: "Image générée",
      data: { page0: uri, doc: [{ uri, width, height }] }
    };
  } catch (e) {
    return {
      succes: false,
      error: e,
      message: "Impossible de générer l'aperçu.",
      data: { page0: null, doc: null }
    };
  }
}
```
