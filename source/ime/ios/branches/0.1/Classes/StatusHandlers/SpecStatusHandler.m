
#import "SpecStatusHandler.h"

@implementation SpecStatusHandler

- (SpecStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	[self load];
	currentStatus = kSpecStatus;
	return self;
}

- (void)load{
	[KeyboardArrangement setSpecialKeyboard:keys];
	[KeyboardArrangement persistChars:keys];
	nextStatus = kSpecStatus;
	[self resetFunctionKey];
	keys[50].status = kPressedStatus;
}

- (void)pressSpecStatus{
	[lastKey resetKey];
}

- (void)pressCap{
	[lastKey resetKey];
}

- (void)pressFull{
	[lastKey resetKey];
}

- (void)pressShift{
	[lastKey resetKey];
}

@end
