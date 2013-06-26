
#import <UIKit/UIKit.h>
#import "Constants.h"

@interface KeyImage:NSObject{
	
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