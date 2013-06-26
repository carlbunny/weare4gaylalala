
#import "EnStatusHandler.h"

@implementation EnStatusHandler

- (EnStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	[self load];
	currentStatus = kEnStatus;
	return self;
}

- (void)load{
	[KeyboardArrangement setLetterKeyboard:keys :capON];
	[KeyboardArrangement persistChars:keys];
	keys[45].originalChar = -IMAGE_CHAR_EN;
	nextStatus = kEnStatus;
	[self resetFunctionKey];
	keys[45].status = kPressedStatus;
}

- (void)pressCap{
	[super pressCap];
	[KeyboardArrangement setLetterKeyboard:keys :capON];
	[KeyboardArrangement persistChars:keys];
}

- (void)pressFull{
	[lastKey resetKey];
}

- (void)pressShift{
	shiftON = !shiftON;
	keys[32].status = shiftON?kPressedStatus:kNormalStatus;
	[KeyboardArrangement setLetterKeyboard:keys :!capON];
}

@end
