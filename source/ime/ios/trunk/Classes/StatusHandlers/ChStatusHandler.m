
#import "ChStatusHandler.h"
#import "../PinYinTree.h"

@implementation ChStatusHandler

- (EnStatusHandler *)init:(DynamicKey**)pKeys{
	self = [super init:pKeys];
	pathIndex = 0;
	moving = NO;
	currentStatus = kChStatus;
	[self load];
	return self;
}

- (void)load{
	[KeyboardArrangement setLetterKeyboard:keys :capON];
	nextStatus = kChStatus;
}

- (void)pressChStatus{
	//Do Nothing;
}

- (void)pressEnter{
	//TODO::
}

- (void)pressCap{
	//TODO::
}

- (void)pressTab{
	//TODO::
}

- (void)pressDel{
	//TODO::
}

- (void)pressBackspace{
	//TODO::
}

- (void)pressShift{
	//TODO::
}

- (void)pressFull{
	//TODO::
}

- (void)pressSetup{
	//TODO::
}

- (void)pressNormalKey:(DynamicKey*)key{
	//TODO::
}

- (void)addToPath: (DynamicKey*) key{
	key.status = kSelectedStatus;
	pathKeyId[pathIndex] = key.keyId;
	pathChar[pathIndex] = key.currentChar;
	pathChar[pathIndex + 1] = 0;
	char* nextChar = [PinYinTree getNexts:pathChar];
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
			return [super keyBegan:key];
			break;
	}
	return YES;
}

- (BOOL)keyMoved:(DynamicKey*)key{
	if (lastKey == key){
		return NO;
	}
	
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
			return [super keyMoved:key];
		{
	}else{
		return [super keyMoved:key];
	}
	return YES;
}

- (BOOL)keyEnded:(DynamicKey*)key{
	if (moving){
		for (int i = 0; i < 53; i++){
			[keys[i] resetKey];
			keys[21].status = capON?kPressedStatus:kNormalStatus;
		}
		pathIndex = 0;
		moving = NO;
	}else{
		pathIndex = 0;
		return [super keyEnded:key];
	}
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
