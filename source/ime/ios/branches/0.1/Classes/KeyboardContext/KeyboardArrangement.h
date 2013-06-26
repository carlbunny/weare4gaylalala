
#import "DynamicKey.h"

@interface KeyboardArrangement:NSObject{

}

+ (void)initKeyboard:(DynamicKey **)keys;
+ (void)changeKeyboard:(DynamicKey **)keys pressedKeyId:(char)keyId;
+ (void)setLetterKeyboard:(DynamicKey **)keys: (BOOL)upper;
+ (void)setNumberKeyboard:(DynamicKey **)keys;
+ (void)setSpecialKeyboard:(DynamicKey **)keys;
+ (void)persistChars:(DynamicKey **)keys;

@end