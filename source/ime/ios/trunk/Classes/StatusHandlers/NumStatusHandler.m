
#import "NumStatusHandler.h"

@implementation NumStatusHandler

- (EnStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	[self load];
	currentStatus = kNumStatus;
	return self;
}

- (void)load{
	[KeyboardArrangement setNumberKeyboard:keys];
	nextStatus = kNumStatus;
}

- (void)pressNumStatus{
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
