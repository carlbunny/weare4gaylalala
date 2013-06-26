
#import <Foundation/Foundation.h>
#import "../KeyboardContext/DynamicKey.h"
#import "../KeyboardContext/KeyboardArrangement.h"

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

- (id)init:(DynamicKey**)pKeys;
- (void)load;
- (void)resetFunctionKey;

- (void)pressEnChStatus;
- (void)pressNumStatus;
- (void)pressSpecStatus;

- (void)pressEnter;
- (void)pressCap;
- (void)pressTab;
- (void)pressDelete;
- (void)pressBackspace;
- (void)pressShift;
- (void)pressFull;
- (void)pressSetup;
- (void)pressNormalKey:(DynamicKey*)key;
- (void)pressBlank;

- (BOOL)keyBegan:(DynamicKey*)key;
- (BOOL)keyMoved:(DynamicKey*)key;
- (BOOL)keyEnded:(DynamicKey*)key;

@end
