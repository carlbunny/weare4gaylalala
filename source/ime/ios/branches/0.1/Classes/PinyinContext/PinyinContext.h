
#import <Foundation/Foundation.h>


@interface PinyinContext : NSObject {
	
}
- (PinyinContext*)init;
- (void)dealloc;

- (void)addPinyin:(char* )pinyin;
- (void)removePinyin;
- (void)chooseCandidate:(int)index;
- (void)clearContext;
- (void)correctWord:(int)index;
- (NSString *)getCandidate:(int)index;
- (int)getCorrectPosistion;
- (NSString *)getPinyin;
- (NSString *)getSentence;
- (int)getSize;

@end
