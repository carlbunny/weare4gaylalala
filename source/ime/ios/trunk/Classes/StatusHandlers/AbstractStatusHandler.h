
#import <Foundation/Foundation.h>
#import "../DynamicKey.h"

@interface AbstractStatusHandler : NSObject {
	DynamicKey    *lastKey;
	DynamicKey    **keys;
	BOOL          capON;
	BOOL          fullON;
	BOOL          shiftON;
	InputStatus   nextStatus;
	InputStatus   currentStatus;
}

@property InputStatus nextStatus;
@property InputStatus currentStatus;

- (AbstractStatusHandler *)init:(DynamicKey**)pKeys;
- (void)load;

- (void)pressChStatus;
- (void)pressEnStatus;
- (void)pressNumStatus;
- (void)pressSpecStatus;

- (void)pressEnter;
- (void)pressCap;
- (void)pressTab;
- (void)pressDel;
- (void)pressBackspace;
- (void)pressShift;
- (void)pressFull;
- (void)pressSetup;
- (void)pressNormalKey:(DynamicKey*)key;

- (BOOL)keyBegan:(DynamicKey*)key;
- (BOOL)keyMoved:(DynamicKey*)key;
- (BOOL)keyEnded:(DynamicKey*)key;

@end
