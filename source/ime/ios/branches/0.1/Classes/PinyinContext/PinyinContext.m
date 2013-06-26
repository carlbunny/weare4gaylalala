
#import "PinyinContext.h"


@implementation PinyinContext

- (PinyinContext *)init{
	self = [super init];
	
	return self;
}

- (void)dealloc{
	[super dealloc];
}

- (void)addPinyin:(char* )pinyin{
	
}

- (void)removePinyin{
	
}

- (void)chooseCandidate:(int)index{
	
}

- (void)clearContext{
	return;
}

- (void)correctWord:(int)index{
	return;
}

- (NSString *)getCandidate:(int)index{
	if (index > 10) return nil;
	return @"测试";
}

- (int)getCorrectPosistion{
	return 0;
}

- (NSString *)getPinyin{
	return @"ce shi pin yin shu ru fa";
}

- (NSString *)getSentence{
	return @"测试拼音输入法";
}

- (int)getSize{
	return 7;
}

@end
