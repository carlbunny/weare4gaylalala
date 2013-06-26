
#import <UIKit/UIKit.h>
#import "Constant.h"

@interface KeyImage:NSObject{
	UIImage* images[IMAGE_TOTAL_SIZE];
}

+ (void)initImages;
+ (UIImage*) getNormalImage;
+ (UIImage*) getNextImage;
+ (UIImage*) getPressedImage;
+ (UIImage*) getSelectedImage;
+ (UIImage*) getPossibleImage;
+ (UIImage*) getBlankNormalImage;
+ (UIImage*) getBlankPressedImage;
+ (UIImage*) getImages:(int)index;
+ (void)releaseImages;
@end