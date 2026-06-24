# scrodo-pdf-thumbnail

Génère une miniature JPEG de n'importe quelle page d'un PDF.  
Fonctionne sur **Android** (PdfRenderer) et **iOS** (PDFKit).

## Installation

```bash
npm install scrodo-pdf-thumbnail
# ou
yarn add scrodo-pdf-thumbnail
```

### iOS
```bash
cd ios && pod install
```

### Android
Aucune étape supplémentaire (autolink).

---

## Utilisation

```typescript
import ScrodoPdfThumbnail from 'scrodo-pdf-thumbnail';

const { uri, width, height } = await ScrodoPdfThumbnail.generate(
  pdfUri,   // string : URI du PDF (file://, content://, chemin absolu)
  1,        // number : numéro de page (commence à 1)
  80        // number : qualité JPEG 0-100
);
```

---

## Structure des fichiers

```
scrodo-pdf-thumbnail/
├── src/
│   ├── index.js          ← point d'entrée JS
│   └── index.d.ts        ← types TypeScript
├── ios/
│   ├── ScrodoPdfThumbnail.m      ← bridge ObjC (obligatoire pour Swift)
│   └── ScrodoPdfThumbnail.swift  ← implémentation PDFKit
├── android/
│   └── src/main/java/com/scrodo/pdfthumbnail/
│       ├── ScrodoPdfThumbnailModule.kt   ← implémentation PdfRenderer
│       └── ScrodoPdfThumbnailPackage.kt  ← enregistrement du module
├── scrodo-pdf-thumbnail.podspec
├── react-native.config.js
└── package.json
```

---

## Pourquoi un fichier `.m` sur iOS ?

Swift ne peut pas être exposé directement à React Native.  
Le fichier `ScrodoPdfThumbnail.m` (Objective-C) fait le pont entre Swift et le bridge JS de React Native.  
Sans lui, le module n'est pas trouvé → erreur "module natif non lié".
