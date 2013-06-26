
#import "Keyboard.h"
#import "KeyboardArrangement.h"
#import "KeyboardHelper.h"

@implementation Keyboard

- (Keyboard *)init{
	self = [super init];
	for (int i = 0; i < 53; i++) {
		keys[i] = [DynamicKey alloc];
	}
	
	[KeyboardArrangement initKeyboard:keys];
	
	chStatusHandler = [[ChStatusHandler alloc] init:keys];
	enStatusHandler = [[EnStatusHandler alloc] init:keys];
	numStatusHandler = [[NumStatusHandler alloc] init:keys];
	specStatusHandler = [[SpecStatusHandler alloc] init:keys];
	
	statusHandler = chStatusHandler;
	
	CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
	CGFloat colors[] =
	{
		168.0 / 255.0, 168.0 / 255.0, 168.0 / 255.0, 1.00,
		89.0 / 255.0, 89.0 / 255.0, 89.0 / 255.0, 1.00
	};
	gradient = CGGradientCreateWithColorComponents(rgb, colors, NULL, sizeof(colors)/(sizeof(colors[0])*4));
	CGColorSpaceRelease(rgb);
	
	return self;
}

- (void)dealloc{
	for (int i = 0; i < 53; i++) {
		[keys[i] release];
	}
	[chStatusHandler release];
	[enStatusHandler release];
	[numStatusHandler release];
	[specStatusHandler release];
	[CGGradientRelease(gradient);
	[super dealloc];
}

- (void)draw: (CGContextRef)context{
	CGPoint start, end;
	start.x = end.x = 0;
	start.y = GRADIENT_START;
	end.y = GRADIENT_END;
	CGContextDrawLinearGradient(context, gradient, start, end, 0);
	
	for (int i = 0; i < 53; i++){
		[keys[i] draw:context];
	}
	
	if (statusHandler.currentStatus == kChStatus){
		[(chStatusHandler)statusHandler drawPath:context];
	}
	

	for (int i = 0; i < 53; i++){
		[keys[i] drawText:context];
	}
}

- (BOOL)touchesBegan:(CGPoint)point {
	int index = [KeyboardHelper getKeyIdByPoint:point];
	if (index == -1) {
		return;
	}
	DynamicKey* key = keys[index];
	return [statusHandler keyBegan:key];
}

- (BOOL)touchesMoved:(CGPoint)point {
	int index = [KeyboardHelper getKeyIdByPoint:point];
	if (index == -1) {
		return;
	}
	DynamicKey* key = keys[index];
	return [statusHandler keyMoved:key];
}

- (BOOL)touchesEnded:(CGPoint)point {
	int index = [KeyboardHelper getKeyIdByPoint:point];
	if (index == -1) {
		return;
	}
	DynamicKey* key = keys[index];
	BOOL ret = [statusHandler keyMoved:key];
	if (statusHandler.currentStatus != statusHandler.nextStatus){
		switch (statusHandler.nextStatus){
			case kChStatus:
				statusHandler = chStatusHandler;
				break;
			case kEnStatus:
				statusHandler = enStatusHandler;
				break;
			case kNumStatus:
				statusHandler = numStatusHandler;
				break;
			case kSpecStatus:
				statusHandler = specStatusHandler;
				break;
		}
		[statusHandler load];
		ret = YES;
	}
	return ret;
}

@end

