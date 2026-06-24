//  ScrodoPdfThumbnail.m
//  Pont Objective-C obligatoire pour exposer le module Swift à React Native

#import <React/RCTBridgeModule.h>

// Le nom ici DOIT correspondre à @objc(ScrodoPdfThumbnail) dans le fichier Swift
@interface RCT_EXTERN_MODULE(ScrodoPdfThumbnail, NSObject)

RCT_EXTERN_METHOD(
  generate:(NSString *)pdfUri
  page:(NSInteger)page
  quality:(NSInteger)quality
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

@end
