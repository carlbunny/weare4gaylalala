
#import "ChStatusHandler.h"
#import "../PinyinContext/PinyinTree.h"

@implementation ChStatusHandler

- (ChStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	pathIndex = 0;
	moving = NO;
	currentStatus = kChStatus;
	[self load];
	return self;
}

- (void)load{
	[KeyboardArrangement setLetterKeyboard:keys :capON];
	[KeyboardArrangement persistChars:keys];
	keys[45].originalChar = -IMAGE_CHAR_CH;
	nextStatus = kChStatus;
	[self resetFunctionKey];
	keys[45].status = kPressedStatus;
}

- (void)pressEnter{
	[lastKey resetKey];
}

- (void)pressCap{
	[lastKey resetKey];
}

- (void)pressTab{
	[lastKey resetKey];
}

- (void)pressDel{
	[lastKey resetKey];
}

- (void)pressBackspace{
	[lastKey resetKey];
}

- (void)pressShift{
	[lastKey resetKey];
}

- (void)pressFull{
	fullON = !fullON;
	keys[44].originalChar = fullON?-IMAGE_CHAR_FULL:-IMAGE_CHAR_HALF;
	[lastKey resetKey];
}

- (void)pressNormalKey:(DynamicKey*)key{
	[lastKey resetKey];
}

- (void)addToPath: (DynamicKey*) key{
	key.status = kSelectedStatus;
	pathKeyId[pathIndex] = key.keyId;
	pathChar[pathIndex] = key.currentChar;
	pathChar[pathIndex + 1] = 0;
	char* nextChar = [PinyinTree getNexts:pathChar];
	char* neighbours = [key getNeighbours];
	for (int i = 0; i < 6; i++) {
		if (neighbours[i] == NEIGHBOURS_END) {
			break;
		}
		DynamicKey* neighbour = keys[neighbours[i]];
		int j = 0;
		while (nextChar[j] != 0) {
			if (neighbour.status == kPossibleStatus &&
				nextChar[j] == neighbour.currentChar) {
				neighbour.status = kNextStatus;
				break;
			}
			j++;
		}
	}
	pathIndex++;
}

- (void)removeFromPath:(int)index{
	for (int i = index; i < pathIndex; i++) {
		keys[pathKeyId[i]].status = kPossibleStatus;
		[self resetNeighbours:keys[pathKeyId[i]]];
	}
	pathIndex = index;
	[self addToPath:keys[pathKeyId[index]]];
}

- (void)resetNeighbours: (DynamicKey *)key{
	char* neighbours = [key getNeighbours];
	for (int i = 0; i < 6; i++) {
		if (neighbours[i] == NEIGHBOURS_END) {
			break;
		}
		DynamicKey* neighbour = keys[neighbours[i]];
		if (neighbour.status == kNextStatus) {
			neighbour.status = kPossibleStatus;
		}
	}
}


- (BOOL)keyBegan:(DynamicKey*)key{
	switch (key.type) {
		case kNormalKey:
			[KeyboardArrangement changeKeyboard:keys pressedKeyId:key.keyId];
			key.currentChar = key.originalChar;
			moving = YES;
			[self addToPath:key];
			lastKey = key;
			break;
		default:
			if (key.type == kBlankKey) {
				key = keys[46];
			}
			return [super keyBegan:key];
			break;
	}
	return YES;
}

- (BOOL)keyMoved:(DynamicKey*)key{
	if (lastKey == key){
		return NO;
	}
	
	lastKey = key;
	if (moving){
		if (key.status == kNextStatus) {
			[self resetNeighbours:keys[pathKeyId[pathIndex - 1]]];
			[self addToPath:key];
		}else if(key.status == kSelectedStatus){
			for (int i = 0; i < pathIndex - 1; i++) {
				if (key.keyId == pathKeyId[i]) {
					[self removeFromPath:i];
				}
			}
		}else{
			return NO;
		}
	}else{
		if (key.type == kBlankKey) {
			key = keys[46];
		}
		return [super keyMoved:key];
	}
	return YES;
}

- (BOOL)keyEnded:(DynamicKey*)key{
	if (key == nil){
		key = lastKey;
	}
	if (lastKey == nil) {
		return NO;
	}
	if (moving){
		for (int i = 0; i < 53; i++){
			[keys[i] resetKey];
			keys[21].status = capON?kPressedStatus:kNormalStatus;
		}
		pathIndex = 0;
		moving = NO;
		[self resetFunctionKey];
		keys[45].status = kPressedStatus;
	}else{
		pathIndex = 0;
		return [super keyEnded:key];
	}
	return YES;
}

- (void)drawPath:(CGContextRef)context{
	CGContextSetRGBStrokeColor(context, 0.9, 0.9, 0.0, 1.0);
	CGContextSetLineWidth(context, 4.0);
	
	for (int i = 1; i < pathIndex; i++){
		DynamicKey* key1 = keys[pathKeyId[i - 1]];
		DynamicKey* key2 = keys[pathKeyId[i]];
		CGContextMoveToPoint(context, [key1 getCenter].x, [key1 getCenter].y);
		CGContextAddLineToPoint(context, [key2 getCenter].x, [key2 getCenter].y);
		CGContextStrokePath(context);
	}
}

@end
