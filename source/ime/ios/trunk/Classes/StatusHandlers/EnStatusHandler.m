
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
	nextStatus = kEnStatus;
}

- (void)pressEnStatus{
	//Do Nothing;
}

- (void)pressCap{
	[super pressCap];
	[KeyboardArrangement setLetterKeyboard:keys :capON];
}

- (void)pressFull{
	//Do Nothing;
}

- (void)pressShift{
	[super pressShift];
	[KeyboardArrangement setLetterKeyboard:keys :!capON];
}

@end
