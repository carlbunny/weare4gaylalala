
#import "KeyImage.h"
#import "DynamicKey.h"

@implementation DynamicKey

@synthesize status;
@synthesize originalChar;
@synthesize currentChar;
@synthesize changed;
@synthesize type;
@synthesize keyId;

- (DynamicKey *)init:(char) pKeyId :(KeyType)pType :(char)pOriginalChar{
	self = [super init];
	keyId = pKeyId;
	type = pType;
	originalChar = pOriginalChar;
	if (self) {
		[self resetKey];
		[self calculateNeighbours];
		[self calculatePos];
	}
	return self;
}

- (void)dealloc{
	[super dealloc];
}

- (void)calculatePos{
	int row, col;
	
	if (keyId < 11){
		row = 0;
		col = keyId;
	}else if (keyId < 21) {
		row = 1;
		col = keyId - 11;
	}else if (keyId < 32) {
		row = 2;
		col = keyId - 21;
	}else if (keyId < 42) {
		row = 3;
		col = keyId - 32;
	}else{
		row = 4;
		col = keyId	- 42;
	}
	
	CGFloat x = KEY_LEFT_SPACING + (col + (row % 2 == 0 ? 0 : 0.5))* (KEY_WIDTH + KEY_HORIZONTAL_SPACING);
	CGFloat y = KEY_TOP_SPACING + row * (KEY_HEIGHT + KEY_VERTICAL_SPACING);

	pos.x = x;
	pos.y = y;
}

- (void)calculateNeighbours{
	int index = 0;
	if (keyId != 0 && keyId != 11 && keyId != 21
		&& keyId != 32 && keyId != 42){
		neighbours[index] = keyId - 1;
		index++;
	}
	if (keyId > 10 && keyId != 21 && keyId != 42){
		neighbours[index] = keyId - 11;
		index++;
	}
	if (keyId > 10 && keyId != 31 && keyId != 52){
		neighbours[index] = keyId - 10;
		index++;
	}
	if (keyId != 10 && keyId != 20 && keyId != 31
		&& keyId != 41 && keyId != 52){
		neighbours[index] = keyId + 1;
		index++;
	}
	if (keyId < 42 && keyId != 31 && keyId != 10) {
		neighbours[index] = keyId + 11;
		index++;
	}
	if (keyId < 42 && keyId != 21 && keyId != 0) {
		neighbours[index] = keyId + 10;
		index++;
	}
	for (; index < 6; index++){
		neighbours[index] = NEIGHBOURS_END;
	}
}

- (char *)getNeighbours{
	return neighbours;
}

- (void)draw:(CGContextRef)context{
	UIImage* keyBackground;
	
	if (keyId == 46){ //first blank key
		if (status != kPressedStatus) {
			[[KeyImage getBlankNormalImage] drawAtPoint:pos];
		}else{
			[[KeyImage getBlankPressedImage] drawAtPoint:pos];
		}

	}
	
	switch (status) {
		case kNormalStatus:
			if (type == kHiddenKey) {
				return;
			}else if(type == kBlankKey) {
				keyBackground = nil;
			}else{
				keyBackground = [KeyImage getNormalImage];
			}
			break;
		case kPressedStatus:
			if(type == kBlankKey) {
				keyBackground = nil;
			}else{
				keyBackground = [KeyImage getPressedImage];
			}
			break;
		case kSelectedStatus:
			keyBackground = [KeyImage getSelectedImage];
			break;
		case kNextStatus:
			keyBackground = [KeyImage getNextImage];
			break;
		case kPossibleStatus:
			keyBackground = [KeyImage getPossibleImage];
			break;
		default:
			break;
	}
	
	if (keyBackground != nil) {
		[keyBackground drawAtPoint:pos];
	}
}

- (void)drawText:(CGContextRef) context{
	char c;
	
	if (currentChar != '\0') {
		c = currentChar;
	}else {
		c = originalChar;
	}
	if (c > 0) {
		CGContextSetRGBFillColor(context, 0.0, 0.0, 0.0, 1.0);
		CGContextSelectFont(context, "Helvetica", 30.0, kCGEncodingMacRoman);
		CGContextSetTextMatrix(context, CGAffineTransformMakeScale(1.0, -1.0));
		CGContextSetTextDrawingMode(context, kCGTextFill);
		char str[2];
		sprintf(str, "%c", c);
		CGContextShowTextAtPoint(context, pos.x + KEY_CHAR_LEFT_SPACING,
								 pos.y + KEY_CHAR_TOP_SPACING, str, 1);
	}else{
		UIImage* image = [UIIMAGE getImages:c];
		if (image != nil){
			[image drawAtPoint:pos];
		}
	}
}

- (void)changeCurrentChar:(char)newChar{
	currentChar = newChar;
	status = kPossibleStatus;
	changed = YES;
}

- (void)resetKey{
	currentChar = 0;
	status = kNormalStatus;
	changed = NO;
}

- (CGPoint)getCenter{
	CGPoint center = pos;
	center.x += KEY_WIDTH / 2;
	center.y += KEY_HEIGHT /2;
	return center;
}

@end