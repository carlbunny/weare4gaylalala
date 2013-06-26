
#import "DynamicKey.h"
#import "StatusHandler/AbstractStatusHandler.h"
#import "StatusHandler/ChStatusHandler.h"
#import "StatusHandler/EnStatusHandler.h"
#import "StatusHandler/NumStatusHandler.h"
#import "StatusHandler/SpecStatusHandler.h"

@interface Keyboard:NSObject{
	DynamicKey*             keys[53];
	AbstractStatusHandler*  statusHandler;
	ChStatusHandler*        chStatusHandler;
	EnStatusHandler*        enStatusHandler;
	NumStatusHandler*       numStatusHandler;
	SpecStatusHandler*      specStatusHandler;
	CGGradientRef     gradient;
}

- (Keyboard *)init;
- (void)dealloc;
- (void)draw: (CGContextRef)context;
- (void)drawPath: (CGContextRef)context;
- (BOOL)touchesBegan:(CGPoint)point;
- (BOOL)touchesMoved:(CGPoint)point;
- (BOOL)touchesEnded:(CGPoint)point;

@end
