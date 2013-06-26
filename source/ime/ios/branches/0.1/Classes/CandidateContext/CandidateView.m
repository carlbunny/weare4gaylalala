
#import "CandidateView.h"


@implementation CandidateView

- (CandidateView *)init{
	self = [super init];
	
	CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
	CGFloat colors[] =
	{
		218.0 / 255.0, 218.0 / 255.0, 218.0 / 255.0, 1.00,
		170.0 / 255.0, 170.0 / 255.0, 170.0 / 255.0, 1.00,
		218.0 / 255.0, 218.0 / 255.0, 218.0 / 255.0, 1.00
	};
	gradient = CGGradientCreateWithColorComponents(rgb, colors, NULL, sizeof(colors)/(sizeof(colors[0])*4));
	CGColorSpaceRelease(rgb);
	
	pinyinContext = [[PinyinContext alloc] init];
	pinyinFont = [UIFont systemFontOfSize:20.0f];
	sentenceFont = [UIFont systemFontOfSize:26.0f];
	candidateFont = [UIFont systemFontOfSize:26.0f];
	
	candidateStartPoint.x = KEY_LEFT_SPACING + 10;
	candidateStartPoint.y = GRADIENT_START - 36;
	candidateCurrentPoint = candidateStartPoint;
	
	return self;
}

- (void)dealloc{
	[pinyinContext release];
	[sentenceFont release];
	[candidateFont release];
	CGGradientRelease(gradient);
	[super dealloc];
}


- (void)changeOritation{
	candidateStartPoint.x = KEY_LEFT_SPACING + 10;
	candidateStartPoint.y = GRADIENT_START - 36;
	candidateCurrentPoint = candidateStartPoint;
}

- (void)draw: (CGContextRef)context{
	CGPoint start, end;
	start.x = end.x = 0;
	start.y = GRADIENT_START - CANDIDATE_HEIGHT - 1;
	end.y = GRADIENT_START - 1;
	CGContextDrawLinearGradient(context, gradient, start, end, 0);
	
	CGContextSetLineWidth(context, 1.0f);
	CGContextSetRGBStrokeColor(context, 0.2, 0.2, 0.2, 1.0);
	CGContextMoveToPoint(context, 0, end.y - 1);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - 1);
	CGContextMoveToPoint(context, 0, end.y - CANDIDATE_HEIGHT - 2);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - CANDIDATE_HEIGHT - 2);
	CGContextDrawPath(context, kCGPathStroke);
	
	CGContextSetRGBStrokeColor(context, 0.6, 0.6, 0.6, 1.0);
	CGContextMoveToPoint(context, 0, end.y - 40);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - 40);
	CGContextMoveToPoint(context, 0, end.y - 75);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - 75);
	CGContextDrawPath(context, kCGPathStroke);
	
	CGContextSetRGBStrokeColor(context, 0.9, 0.9, 0.9, 1.0);
	CGContextMoveToPoint(context, 0, end.y - 39);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - 39);
	CGContextMoveToPoint(context, 0, end.y - 74);
	CGContextAddLineToPoint(context, VIEW_WIDTH, end.y - 74);
	CGContextDrawPath(context, kCGPathStroke);
	
	NSString* sentence = [pinyinContext getSentence];
	NSString* pinyin = [pinyinContext getPinyin];
	
	CGPoint point;
	point.y = GRADIENT_START - CANDIDATE_HEIGHT;
	point.x = KEY_LEFT_SPACING + 10;
	
	CGContextSetRGBFillColor(context, 0.0, 0.0, 0.0, 1.0);
	[pinyin drawAtPoint:point withFont:pinyinFont];
	point.y += 25;
	[sentence drawAtPoint:point withFont:sentenceFont];
	
	NSString* candidate = [pinyinContext getCandidate:0];
	[candidate drawAtPoint:candidateCurrentPoint withFont:candidateFont];
	
}

- (void)addPinyin: (char*) pinyin{
	
}

- (void)removePinyin{
	
}

- (void)pushEnter{
	
}

- (void)pushDelete{
	
}

- (void)setMohu: (int)sheng: (int)yun{
	
}

- (BOOL)touchesBegan:(CGPoint)point{
	beginPoint = point;
	return NO;
}

- (BOOL)touchesMoved:(CGPoint)point{
	candidateCurrentPoint.x = candidateStartPoint.x + (point.x - beginPoint.x);
	
	return YES;
}

- (BOOL)touchesEnded:(CGPoint)point{
	candidateCurrentPoint = candidateStartPoint;
	return YES;
}

- (BOOL)inCandidateRect:(CGPoint)point{
	return point.y < GRADIENT_START && point.y > GRADIENT_START - 100;
}

- (CGRect)getCandidateRect{
	CGRect ret = CGRectMake(0, GRADIENT_START - CANDIDATE_HEIGHT, VIEW_WIDTH, CANDIDATE_HEIGHT);
	return ret;
}
@end
