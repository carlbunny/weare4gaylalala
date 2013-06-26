
#import "SpecStatusHandler.h"

@implementation SpecStatusHandler

- (SpecStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	[self load];
	currentStatus = kSpecStatus;
	return self;
}

- (void)load{
	[KeyboardArrangement setLetterKeyboard:keys :capON];
	nextStatus = kEnStatus;
}

- (void)pressSpecStatus{
	//Do Nothing;
}

- (void)pressCap{
	//Do Nothing;
}

- (void)pressFull{
	//Do Nothing;
}

- (void)pressShift{
	//Do Nothing;
}

@end
