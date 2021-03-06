//
//  AeviouView.m
//  Aeviou
//
//  Created by donghl on 10-7-16.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "AeviouView.h"
#import "KeyImage.h"
#import "PinYinTree.h"


@implementation AeviouView

- (id)initWithCoder:(NSCoder *)coder {
	if ((self = [super initWithCoder:coder] )) {
        [KeyImage initImages];
		[PinYinTree initTree];
		keyboard = [[Keyboard alloc] init];
	}
	return self;
}

- (id)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
    }
    return self;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
	UITouch *touch = [touches anyObject];
	if ([keyboard touchesBegan:[touch locationInView:self]]){
		[self setNeedsDisplay];
	}
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
	UITouch *touch = [touches anyObject];
	if ([keyboard touchesMoved:[touch locationInView:self]];)
		[self setNeedsDisplay];
	}
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
	UITouch *touch = [touches anyObject];
	if ([keyboard touchesEnded:[touch locationInView:self]];)
		[self setNeedsDisplay];
	}
}

- (void)drawRect:(CGRect)rect{
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	[keyboard draw:context];
}

- (void)dealloc {
	[KeyImage releaseImages];
	[PinYinTree releaseTree];
    [super dealloc];
}


@end
