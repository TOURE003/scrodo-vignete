#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ScrodoPdfThumbnailModule, NSObject)

RCT_EXTERN_METHOD(
  generate:(NSString *)pdfUri
  page:(NSInteger)page
  quality:(NSInteger)quality
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
