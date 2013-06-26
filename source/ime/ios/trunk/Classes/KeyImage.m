
#import "KeyImage.h"

@implementation KeyImage

static UIImage* keyNormalImage;
static UIImage* keyNextImage;
static UIImage* keyPressedImage;
static UIImage* keySelectedImage;
static UIImage* keyPossibleImage;
static UIImage* keyBlankNormalImage;
static UIImage* keyBlankPressedImage;

+ (void)initImages{
	keyNormalImage = [UIImage imageNamed:@"Key-Normal.png"];
	keyNextImage = [UIImage imageNamed:@"Key-Next.png"];	
	keyPressedImage = [UIImage imageNamed:@"Key-Pressed.png"];
	keySelectedImage = [UIImage imageNamed:@"Key-Selected.png"];
	keyPossibleImage = [UIImage imageNamed:@"Key-Possible.png"];
	keyBlankNormalImage = [UIImage imageNamed:@"Blank-Normal.png"];
	keyBlankPressedImage = [UIImage imageNamed:@"Blank-Pressed.png"];
	
	images[0] = [UIImage imageNamed:@"char-backspace.png"];
	images[1] = [UIImage imageNamed:@"char-cap.png"];
	images[2] = [UIImage imageNamed:@"char-ch.png"];
	images[3] = [UIImage imageNamed:@"char-del.png"];
	images[4] = [UIImage imageNamed:@"char-en.png"];
	images[5] = [UIImage imageNamed:@"char-enter.png"];
	images[6] = [UIImage imageNamed:@"char-full.png"];
	images[7] = [UIImage imageNamed:@"char-half.png"];
	images[8] = [UIImage imageNamed:@"char-number.png"];
	images[9] = [UIImage imageNamed:@"char-setup.png"];
	images[10] = [UIImage imageNamed:@"char-shift.png"];
	images[11] = [UIImage imageNamed:@"char-special.png"];
	images[12] = [UIImage imageNamed:@"char-tab.png"];
}

+ (UIImage*) getNormalImage{
	return keyNormalImage;
}

+ (UIImage*) getNextImage{
	return keyNextImage;
}

+ (UIImage*) getPressedImage{
	return keyPressedImage;
}

+ (UIImage*) getSelectedImage{
	return keySelectedImage;
}

+ (UIImage*) getPossibleImage{
	return keyPossibleImage;
}

+ (UIImage*) getBlankNormalImage{
	return keyBlankNormalImage;
}

+ (UIImage*) getBlankPressedImage{
	return keyBlankPressedImage;
}

+ (void)releaseImages{
	[keyNormalImage release];
	[keyNextImage release];
	[keyPressedImage release];
	[keySelectedImage release];
	[keyPossibleImage release];
	[keyBlankNormalImage release];
	[keyBlankPressedImage release];
	for (int i = 0; i < IMAGE_TOTAL_SIZE; i++){
	[images[i] release];
	}
}

+ (UIImage*) getImages:(int)index{
	if (index < 0) index = -index;
	if (index >= IMAGE_TOTAL_SIZE || index == 0){
		return nil;
	}
	return images[index - 1];
}

@end