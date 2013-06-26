
#import "Constants.h"
#import <UIKit/UIKit.h>

@interface DynamicKey:NSObject{
	char          keyId;
	CGPoint       pos;
	KeyStatus     status;
	KeyType       type;
	char          originalChar;
	char          currentChar;
	char          neighbours[6];
	BOOL          changed;
}

@property KeyStatus status;
@property char originalChar;
@property char currentChar;
@property BOOL changed;
@property (readonly) KeyType type;
@property (readonly) char keyId;

- (DynamicKey *)init:(char) pKeyId :(KeyType)pType :(char)pOriginalChar;
- (void)dealloc;
- (void)changeCurrentChar:(char)newChar;
- (void)calculatePos;
- (void)calculateNeighbours;
- (void)drawText:(CGContextRef) context;
- (char *)getNeighbours;
- (void)draw:(CGContextRef) context;
- (void)resetKey;
- (CGPoint)getCenter;

@end
