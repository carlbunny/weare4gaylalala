
#import "DynamicKey.h"
#import "../StatusHandlers/AbstractStatusHandler.h"
#import "../StatusHandlers/ChStatusHandler.h"
#import "../StatusHandlers/EnStatusHandler.h"
#import "../StatusHandlers/NumStatusHandler.h"
#import "../StatusHandlers/SpecStatusHandler.h"
#import "../CandidateContext/CandidateView.h"

@interface Keyboard:NSObject{
	DynamicKey*             keys[53];
	AbstractStatusHandler*  statusHandler;
	ChStatusHandler*        chStatusHandler;
	EnStatusHandler*        enStatusHandler;
	NumStatusHandler*       numStatusHandler;
	SpecStatusHandler*      specStatusHandler;
	CandidateView*			candidate;
	BOOL					touchBeganInCandidate;
	CGGradientRef           gradient;
}

- (Keyboard *)init;
- (void)dealloc;
- (void)draw: (CGContextRef)context;
- (BOOL)touchesBegan:(CGPoint)point;
- (BOOL)touchesMoved:(CGPoint)point;
- (BOOL)touchesEnded:(CGPoint)point;

@end
