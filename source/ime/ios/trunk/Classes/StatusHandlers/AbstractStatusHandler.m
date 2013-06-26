
#import "AbstractStatusHandler.h"

@implementation AbstractStatusHandler

@synthesize nextStatus;
@synthesize currentStatus;

- (AbstractStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init];
	keys = pKeys;
	capON = NO;
	fullON = NO;
	shiftON = NO;
	return self;
}

- (void)load{
	//Abstract function, no implementation
}

- (void)pressChStatus{
	nextStatus = kChStatus;
}

- (void)pressEnStatus{
	nextStatus = kEnStatus;
}

- (void)pressNumStatus{
	nextStatus = kNumStatus;
}

- (void)pressSpecStatus{
	nextStatus = kSpecStatus;
}

- (void)pressEnter{
	
}

- (void)pressCap{
	capON = !capON;
	keys[21].status = capON?kPressedStatus:kNormalStatus;
}

- (void)pressTab{

}

- (void)pressDel{

}

- (void)pressBackspace{

}

- (void)pressShift{
	shiftON = !shiftON;
	keys[32].status = capON?kPressedStatus:kNormalStatus;
}

- (void)pressSetup{

}

- (void)pressFull{
	fullON = !fullON;
	keys[44].originalChar = fullON?IMAGE_CHAR_FULL:IMAGE_CHAR_HALF;
}

- (void)pressNormalKey:(DynamicKey*)key{

}

- (BOOL)keyBegan:(DynamicKey*)key{
	lastKey = key;
	if (lastKey.type == kBlankKey) {
		keys[46].status = kPressedStatus;
		lastKey = keys[46];
	}else {
		lastKey.status = kSelectedStatus;
	}
	
	return YES;
}

- (BOOL)keyMoved:(DynamicKey*)key{
	if (key == lastKey){
		return NO;
	}
	if (lastKey != nil) {
		[lastKey resetKey];
	}
	lastKey = key;
	if (lastKey.type == kBlankKey) {
		keys[46].status = kPressedStatus;
		lastKey = keys[46];
	}else {
		lastKey.status = kSelectedStatus;
	}
	
	return YES;
}

- (BOOL)keyEnded:(DynamicKey*)key{
	if (lastKey != key){
		[self keyMoved:key];
	}

	if (lastKey.type == kNormalKey ||
		lastKey.type == kDotKey ||
		lastKey.type == kBlankKey){
		pressNormalKey(lastKey);
	}else{
		switch (lastKey.keyId){
			case 21: /*Cap*/
				[self pressCap];
				break;
			case 31: /*Enter*/
				[self pressEnter];
				break;
			case 32: /*Shift*/
				[self pressShift];
				break;
			case 42: /*Setup*/
				[self pressSetup];
				break;
			case 43: /*Tab*/
				[self pressTab];
				break;
			case 44: /*FullKey*/
				[self pressFull];
				break;
			case 45: /*EnKey*/
				[self pressEnStatus];
				break;
			case 49: /*NumKey*/
				[self pressNumStatus];
				break;
			case 50: /*SpecialKey*/
				[self pressSpecStatus];
				break;
			case 51: /*DeleteKey*/
				[self pressDelete];
				break;
			case 52: /*BackspaceKey*/
				[self pressBackspace];
				break;
		}
	}
	
	if (shiftON){
		[self pressShift];
	}
	
	[lastKey resetKey];
	lastKey = nil;
	return YES;
}

@end
