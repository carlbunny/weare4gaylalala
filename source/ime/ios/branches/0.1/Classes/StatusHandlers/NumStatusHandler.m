
#import "NumStatusHandler.h"

@implementation NumStatusHandler

- (NumStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	[self load];
	currentStatus = kNumStatus;
	return self;
}

- (void)load{
	[KeyboardArrangement setNumberKeyboard:keys];
	[KeyboardArrangement persistChars:keys];
	nextStatus = kNumStatus;
	[self resetFunctionKey];
	keys[49].status = kPressedStatus;
}

- (void)pressNumStatus{
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
