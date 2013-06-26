
#import "AbstractStatusHandler.h"
#import "../SwitchViewController.h"

@implementation AbstractStatusHandler

@synthesize nextStatus;
@synthesize currentStatus;

- (id)init:(DynamicKey**)pKeys{
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

- (void)resetFunctionKey{
	keys[21].status = capON?kPressedStatus:kNormalStatus;
	keys[32].status = shiftON?kPressedStatus:kNormalStatus;
	keys[44].originalChar = fullON?-IMAGE_CHAR_FULL:-IMAGE_CHAR_HALF;
	
	keys[45].status = kNormalStatus;
	keys[49].status = kNormalStatus;
	keys[50].status = kNormalStatus;
}

- (void)pressEnChStatus{
	if (currentStatus == kEnStatus) {
		nextStatus = kChStatus;
	}else if (currentStatus == kChStatus) {
		nextStatus = kEnStatus;
	}else{
		if (keys[45].originalChar == -IMAGE_CHAR_EN) {
			nextStatus = kEnStatus;
		}else{
			nextStatus = kChStatus;
		}
	}
}

- (void)pressNumStatus{
	nextStatus = kNumStatus;
}

- (void)pressSpecStatus{
	nextStatus = kSpecStatus;
}

- (void)pressEnter{
	[lastKey resetKey];
}

- (void)pressCap{
	capON = !capON;
	keys[21].status = capON?kPressedStatus:kNormalStatus;
}

- (void)pressTab{
	[lastKey resetKey];
}

- (void)pressDelete{
	[lastKey resetKey];
}

- (void)pressBackspace{
	[lastKey resetKey];
}

- (void)pressShift{
	//do nothing
}

- (void)pressSetup{
	[gSwitchViewController swtichToSettingView];
	[lastKey resetKey];
}

- (void)pressFull{
	fullON = !fullON;
	keys[44].originalChar = fullON?IMAGE_CHAR_FULL:IMAGE_CHAR_HALF;
}

- (void)pressNormalKey:(DynamicKey*)key{
	[lastKey resetKey];
}

- (void)pressBlank{
	[keys[46] resetKey];
}

- (BOOL)keyBegan:(DynamicKey*)key{
	if (key.type == kHiddenKey) {
		return NO;
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

- (BOOL)keyMoved:(DynamicKey*)key{
	if (key.type == kHiddenKey) {
		return NO;
	}
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
	if (key == nil) {
		key = lastKey;
	}
	if (lastKey == nil) {
		return NO;
	}
	if (key.type == kHiddenKey) {
		return NO;
	}
	if (lastKey != key){
		[self keyMoved:key];
	}
	
	BOOL shiftFlag = shiftON;

	if (lastKey.type == kNormalKey ||
		lastKey.type == kDotKey ||
		lastKey.type == kBlankKey){
		[self pressNormalKey:lastKey];
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
				[self pressEnChStatus];
				break;
			case 46: case 47: case 48:
				[self pressBlank];
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
	
	if (shiftFlag) {
		shiftON = NO;
		keys[32].status = kNormalStatus;
		for (int i = 0; i < 53; i++) {
			if (keys[i].type == kNormalKey) {
				[keys[i] resetKey];
			}
		}
	}
	
	lastKey = nil;
	return YES;
}

@end
