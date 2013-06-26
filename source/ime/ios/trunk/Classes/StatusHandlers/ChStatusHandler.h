
#import <Foundation/Foundation.h>
#import "AbstractStatusHandler.h"

@interface ChStatusHandler : AbstractStatusHandler {
	BOOL                moving;
	int                 pathKeyId[6];
	char                pathChar[7];
	int                 pathIndex;
}


- (ChStatusHandler *)init:(DynamicKey**)pKeys;

- (void)pressChStatus;

- (void)pressEnter;
- (void)pressCap;
- (void)pressTab;
- (void)pressDel;
- (void)pressBackspace;
- (void)pressShift;
- (void)pressFull;
- (void)pressSetup;
- (void)pressNormalKey:(DynamicKey*)key;

- (void)addToPath: (DynamicKey*) key;
- (void)removeFromPath: (int)index;
- (void)resetNeighbours: (DynamicKey *)key;

- (BOOL)keyBegan:(DynamicKey*)key;
- (BOOL)keyMoved:(DynamicKey*)key;
- (BOOL)keyEnded:(DynamicKey*)key;

- (void)drawPath:(CGContextRef)context;

@end
