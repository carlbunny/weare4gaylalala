
#import <Foundation/Foundation.h>
#import "../Constants.h"
#import "../PinyinContext/PinyinContext.h"


@interface CandidateView : NSObject {
	CGGradientRef           gradient;
	PinyinContext*			pinyinContext;
	UIFont*					pinyinFont;
	UIFont*					sentenceFont;
	UIFont*					candidateFont;
	CGPoint					candidateStartPoint;
	CGPoint					beginPoint;
	CGPoint					candidateCurrentPoint;
}

- (CandidateView *)init;
- (void)dealloc;

- (void)draw: (CGContextRef)context;
- (void)changeOritation;
- (void)addPinyin: (char*) pinyin;
- (void)removePinyin;
- (void)pushEnter;
- (void)pushDelete;
- (void)setMohu: (int)sheng: (int)yun;
- (BOOL)touchesBegan:(CGPoint)point;
- (BOOL)touchesMoved:(CGPoint)point;
- (BOOL)touchesEnded:(CGPoint)point;
- (BOOL)inCandidateRect:(CGPoint)point;
- (CGRect)getCandidateRect;

@end
