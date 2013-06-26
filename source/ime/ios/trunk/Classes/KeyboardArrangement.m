
#import <UIKit/UIKit.h>
#import "KeyboardArrangement.h"

@implementation KeyboardArrangement


+ (void)initKeyboard:(DynamicKey **)keys{
	for (int i = 0; i <= 10; i++){
		[keys[i] init:(char)i :kHiddenKey :0];
	}
	for (int i = 11; i <= 20; i++) {
		[keys[i] init:(char)i :kNormalKey :0];
	}
	for (int i = 22; i <= 30; i++) {
		[keys[i] init:(char)i :kNormalKey :0];
	}
	for (int i = 33; i <= 39; i++) {
		[keys[i] init:(char)i :kNormalKey :0];
	}
	[KeyboardArrangement setLetterKeyboard:keys:NO];
	[KeyboardArrangement persistChars:keys];
	[keys[21] init:21 :kCapKey :-IMAGE_CHAR_CAP];
	[keys[31] init:31 :kEnterKey :-IMAGE_CHAR_TAB];
	[keys[32] init:32 :kShiftKey :-IMAGE_CHAR_SHIFT];	
	[keys[40] init:40 :kSymbolKey :','];
	[keys[41] init:41 :kSymbolKey :'.'];
	[keys[42] init:42 :kSetupKey :-IMAGE_CHAR_SETUP];
	[keys[43] init:43 :kEnterKey :-IMAGE_CHAR_ENTER];
	[keys[44] init:44 :kEnKey :-IMAGE_CHAR_HALF];
	[keys[45] init:45 :kEnKey :-IMAGE_CHAR_EN];
	[keys[46] init:46 :kBlankKey :0];
	[keys[47] init:47 :kBlankKey :0];
	[keys[48] init:48 :kBlankKey :0];
	[keys[49] init:49 :kCapKey :-IMAGE_CHAR_NUMBER];
	[keys[50] init:50 :kCapKey :-IMAGE_CHAR_SPECIAL];
	[keys[51] init:51 :kDeleteKey :-IMAGE_CHAR_DEL];
	[keys[52] init:52 :kDeleteKey :-IMAGE_CHAR_BACKSPACE];
	
}

+ (void)setLetterKeyboard:(DynamicKey **)keys: (BOOL)upper{
	keys[11].currentChar = 'q';
	keys[12].currentChar = 'w';
	keys[13].currentChar = 'e';
	keys[14].currentChar = 'r';
	keys[15].currentChar = 't';
	keys[16].currentChar = 'y';
	keys[17].currentChar = 'u';
	keys[18].currentChar = 'i';
	keys[19].currentChar = 'o';
	keys[20].currentChar = 'p';
	keys[22].currentChar = 'a';
	keys[23].currentChar = 's';
	keys[24].currentChar = 'd';
	keys[25].currentChar = 'f';
	keys[26].currentChar = 'g';
	keys[27].currentChar = 'h';
	keys[28].currentChar = 'j';
	keys[29].currentChar = 'k';
	keys[30].currentChar = 'l';
	keys[33].currentChar = 'z';
	keys[34].currentChar = 'x';
	keys[35].currentChar = 'c';
	keys[36].currentChar = 'v';
	keys[37].currentChar = 'b';
	keys[38].currentChar = 'n';
	keys[39].currentChar = 'm';
	if (upper) {
		char offset = 'A' - 'a';
		for (int i = 11; i < 40; i++) {
			if (keys[i].type == kNormalKey) {
				keys[i].currentChar += offset;
			}
		}
	}
}

+ (void)changeKeyboard:(DynamicKey **)keys pressedKeyId:(char)keyId{
	switch (keyId) {
		case 11: /*q*/
			[keys[1] changeCurrentChar:'e'];
			[keys[2] changeCurrentChar:'a'];
			[keys[12] changeCurrentChar:'i'];
			[keys[13] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'g'];
			[keys[21] changeCurrentChar:'n'];
			[keys[22] changeCurrentChar:'u'];
			[keys[23] changeCurrentChar:'o'];
			[keys[32] changeCurrentChar:'a'];
			[keys[33] changeCurrentChar:'e'];
			break;
		case 12: /*w*/
			[keys[11] changeCurrentChar:'o'];
			[keys[13] changeCurrentChar:'e'];
			[keys[14] changeCurrentChar:'i'];
			[keys[22] changeCurrentChar:'u'];
			[keys[23] changeCurrentChar:'a'];
			[keys[24] changeCurrentChar:'n'];
			[keys[25] changeCurrentChar:'g'];
			[keys[34] changeCurrentChar:'i'];
			break;
		case 13: /*e*/
			[keys[14] changeCurrentChar:'r'];
			[keys[23] changeCurrentChar:'i'];
			[keys[24] changeCurrentChar:'n'];
			break;
		case 14: /*r*/
			[keys[4] changeCurrentChar:'i'];
			[keys[13] changeCurrentChar:'e'];
			[keys[15] changeCurrentChar:'u'];
			[keys[16] changeCurrentChar:'a'];
			[keys[22] changeCurrentChar:'g'];
			[keys[23] changeCurrentChar:'n'];
			[keys[24] changeCurrentChar:'a'];
			[keys[25] changeCurrentChar:'o'];
			[keys[26] changeCurrentChar:'n'];
			[keys[27] changeCurrentChar:'g'];
			break;
		case 15: /*t*/
			[keys[5] changeCurrentChar:'e'];
			[keys[6] changeCurrentChar:'n'];
			[keys[7] changeCurrentChar:'g'];
			[keys[13] changeCurrentChar:'o'];
			[keys[14] changeCurrentChar:'a'];
			[keys[16] changeCurrentChar:'o'];
			[keys[23] changeCurrentChar:'g'];
			[keys[24] changeCurrentChar:'n'];
			[keys[25] changeCurrentChar:'i'];
			[keys[26] changeCurrentChar:'u'];
			[keys[27] changeCurrentChar:'a'];
			[keys[35] changeCurrentChar:'e'];
			[keys[37] changeCurrentChar:'n'];
			break;
		case 16: /*y*/
			[keys[7] :'e'];
			[keys[15] changeCurrentChar:'i'];
			[keys[17] changeCurrentChar:'u'];
			[keys[18] changeCurrentChar:'a'];
			[keys[24] changeCurrentChar:'g'];
			[keys[25] changeCurrentChar:'n'];
			[keys[26] changeCurrentChar:'a'];
			[keys[27] changeCurrentChar:'o'];
			[keys[28] changeCurrentChar:'n'];
			[keys[29] changeCurrentChar:'g'];
			break;
		case 19: /*o*/
			[keys[18] changeCurrentChar:'u'];
			break;
		case 20: /*p*/
			[keys[8] changeCurrentChar:'n'];
			[keys[9] changeCurrentChar:'e'];
			[keys[10] changeCurrentChar:'u'];
			[keys[18] changeCurrentChar:'g'];
			[keys[19] changeCurrentChar:'i'];
			[keys[29] changeCurrentChar:'n'];
			[keys[30] changeCurrentChar:'a'];
			[keys[31] changeCurrentChar:'o'];
			[keys[41] changeCurrentChar:'u'];
			break;
		case 22: /*a*/
			[keys[12] changeCurrentChar:'i'];
			[keys[23] changeCurrentChar:'n'];
			[keys[24] changeCurrentChar:'g'];
			[keys[33] changeCurrentChar:'o'];
			break;
		case 23: /*s*/
			[keys[11] changeCurrentChar:'o'];
			[keys[12] changeCurrentChar:'i'];
			[keys[13] changeCurrentChar:'o'];
			[keys[14] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'g'];
			[keys[21] changeCurrentChar:'g'];
			[keys[22] changeCurrentChar:'a'];
			[keys[24] changeCurrentChar:'u'];
			[keys[25] changeCurrentChar:'i'];
			[keys[32] changeCurrentChar:'n'];
			[keys[33] changeCurrentChar:'e'];
			[keys[34] changeCurrentChar:'h'];
			[keys[35] changeCurrentChar:'a'];
			[keys[36] changeCurrentChar:'n'];
			[keys[37] changeCurrentChar:'g'];
			[keys[44] changeCurrentChar:'i'];
			[keys[45] changeCurrentChar:'o'];
			[keys[46] changeCurrentChar:'u'];
			break;
		case 24: /*d*/
			[keys[11] changeCurrentChar:'g'];
			[keys[12] changeCurrentChar:'n'];
			[keys[13] changeCurrentChar:'o'];
			[keys[14] changeCurrentChar:'a'];
			[keys[15] changeCurrentChar:'n'];
			[keys[16] changeCurrentChar:'g'];
			[keys[22] changeCurrentChar:'a'];
			[keys[23] changeCurrentChar:'u'];
			[keys[25] changeCurrentChar:'i'];
			[keys[26] changeCurrentChar:'u'];
			[keys[33] changeCurrentChar:'i'];
			[keys[35] changeCurrentChar:'e'];
			[keys[36] changeCurrentChar:'n'];
			[keys[37] changeCurrentChar:'g'];	
			break;
		case 25: /*f*/
			[keys[12] changeCurrentChar:'g'];
			[keys[13] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'a'];
			[keys[15] changeCurrentChar:'o'];
			[keys[23] changeCurrentChar:'i'];
			[keys[24] changeCurrentChar:'e'];
			[keys[26] changeCurrentChar:'u'];		
			break;
		case 26: /*g*/
			[keys[5] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'i'];
			[keys[15] changeCurrentChar:'u'];
			[keys[16] changeCurrentChar:'o'];
			[keys[17] changeCurrentChar:'n'];
			[keys[18] changeCurrentChar:'g'];
			[keys[23] changeCurrentChar:'g'];
			[keys[24] changeCurrentChar:'n'];
			[keys[25] changeCurrentChar:'a'];
			[keys[27] changeCurrentChar:'e'];
			[keys[28] changeCurrentChar:'i'];
			[keys[35] changeCurrentChar:'o'];
			break;
		case 27: /*h*/
			[keys[16] changeCurrentChar:'e'];
			[keys[17] changeCurrentChar:'i'];
			[keys[25] changeCurrentChar:'g'];
			[keys[26] changeCurrentChar:'n'];
			[keys[28] changeCurrentChar:'a'];
			[keys[29] changeCurrentChar:'o'];
			[keys[36] changeCurrentChar:'o'];
			[keys[37] changeCurrentChar:'u'];
			[keys[38] changeCurrentChar:'n'];
			[keys[39] changeCurrentChar:'g'];
			[keys[47] changeCurrentChar:'i'];
			break;
		case 28: /*j*/
			[keys[7] changeCurrentChar:'n'];
			[keys[8] changeCurrentChar:'e'];
			[keys[9] changeCurrentChar:'o'];
			[keys[16] changeCurrentChar:'a'];
			[keys[17] changeCurrentChar:'u'];
			[keys[18] changeCurrentChar:'i'];
			[keys[19] changeCurrentChar:'n'];
			[keys[20] changeCurrentChar:'g'];
			[keys[29] changeCurrentChar:'a'];
			[keys[30] changeCurrentChar:'o'];
			break;
		case 29: /*k*/
			[keys[16] changeCurrentChar:'g'];
			[keys[17] changeCurrentChar:'n'];
			[keys[18] changeCurrentChar:'o'];
			[keys[19] changeCurrentChar:'a'];
			[keys[20] changeCurrentChar:'i'];
			[keys[27] changeCurrentChar:'a'];
			[keys[28] changeCurrentChar:'u'];
			[keys[30] changeCurrentChar:'n'];
			[keys[31] changeCurrentChar:'g'];
			[keys[37] changeCurrentChar:'i'];
			[keys[39] changeCurrentChar:'e'];
			[keys[40] changeCurrentChar:'i'];
			break;
		case 30: /*l*/
			[keys[8] changeCurrentChar:'g'];
			[keys[9] changeCurrentChar:'n'];
			[keys[18] changeCurrentChar:'e'];
			[keys[19] changeCurrentChar:'u'];
			[keys[20] changeCurrentChar:'o'];
			[keys[28] changeCurrentChar:'o'];
			[keys[29] changeCurrentChar:'a'];
			[keys[31] changeCurrentChar:'v'];
			[keys[38] changeCurrentChar:'g'];
			[keys[39] changeCurrentChar:'n'];
			[keys[40] changeCurrentChar:'i'];
			[keys[41] changeCurrentChar:'e'];
			[keys[50] changeCurrentChar:'u'];
			[keys[51] changeCurrentChar:'n'];
			[keys[52] changeCurrentChar:'g'];
			break;
		case 33: /*z*/
			[keys[2] changeCurrentChar:'u'];
			[keys[11] changeCurrentChar:'g'];
			[keys[12] changeCurrentChar:'i'];
			[keys[13] changeCurrentChar:'o'];
			[keys[14] changeCurrentChar:'n'];
			[keys[15] changeCurrentChar:'g'];
			[keys[21] changeCurrentChar:'n'];
			[keys[22] changeCurrentChar:'e'];
			[keys[23] changeCurrentChar:'h'];
			[keys[24] changeCurrentChar:'a'];
			[keys[32] changeCurrentChar:'a'];
			[keys[34] changeCurrentChar:'u'];
			[keys[35] changeCurrentChar:'i'];
			[keys[42] changeCurrentChar:'o'];
			[keys[43] changeCurrentChar:'i'];
			[keys[44] changeCurrentChar:'o'];
			[keys[45] changeCurrentChar:'n'];
			[keys[46] changeCurrentChar:'g'];
			break;
		case 34: /*x*/
			[keys[12] changeCurrentChar:'e'];
			[keys[13] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'g'];
			[keys[22] changeCurrentChar:'a'];
			[keys[23] changeCurrentChar:'i'];
			[keys[24] changeCurrentChar:'u'];
			[keys[25] changeCurrentChar:'a'];
			[keys[26] changeCurrentChar:'n'];
			[keys[32] changeCurrentChar:'n'];
			[keys[33] changeCurrentChar:'o'];
			[keys[35] changeCurrentChar:'e'];
			[keys[43] changeCurrentChar:'g'];
			break;
		case 35: /*c*/
			[keys[12] changeCurrentChar:'g'];
			[keys[13] changeCurrentChar:'n'];
			[keys[14] changeCurrentChar:'i'];
			[keys[15] changeCurrentChar:'o'];
			[keys[16] changeCurrentChar:'n'];
			[keys[17] changeCurrentChar:'g'];
			[keys[23] changeCurrentChar:'i'];
			[keys[24] changeCurrentChar:'e'];
			[keys[25] changeCurrentChar:'h'];
			[keys[26] changeCurrentChar:'u'];
			[keys[27] changeCurrentChar:'i'];
			[keys[33] changeCurrentChar:'a'];
			[keys[34] changeCurrentChar:'u'];
			[keys[36] changeCurrentChar:'a'];
			[keys[37] changeCurrentChar:'n'];
			[keys[38] changeCurrentChar:'g'];
			[keys[43] changeCurrentChar:'g'];
			[keys[44] changeCurrentChar:'n'];
			[keys[45] changeCurrentChar:'o'];
			[keys[46] changeCurrentChar:'i'];
			[keys[47] changeCurrentChar:'o'];
			break;
		case 37: /*b*/
			[keys[15] changeCurrentChar:'o'];
			[keys[16] changeCurrentChar:'e'];
			[keys[24] changeCurrentChar:'g'];
			[keys[25] changeCurrentChar:'n'];
			[keys[26] changeCurrentChar:'a'];
			[keys[27] changeCurrentChar:'i'];
			[keys[28] changeCurrentChar:'n'];
			[keys[29] changeCurrentChar:'g'];
			[keys[35] changeCurrentChar:'i'];
			[keys[36] changeCurrentChar:'e'];
			[keys[38] changeCurrentChar:'o'];
			[keys[48] changeCurrentChar:'u'];
			break;
		case 38: /*n*/
			[keys[17] changeCurrentChar:'o'];
			[keys[18] changeCurrentChar:'u'];
			[keys[25] changeCurrentChar:'g'];
			[keys[26] changeCurrentChar:'n'];
			[keys[27] changeCurrentChar:'a'];
			[keys[28] changeCurrentChar:'i'];
			[keys[29] changeCurrentChar:'n'];
			[keys[30] changeCurrentChar:'g'];
			[keys[36] changeCurrentChar:'e'];
			[keys[37] changeCurrentChar:'u'];
			[keys[39] changeCurrentChar:'e'];
			[keys[46] changeCurrentChar:'g'];
			[keys[47] changeCurrentChar:'n'];
			[keys[48] changeCurrentChar:'o'];
			[keys[49] changeCurrentChar:'v'];
			break;
		case 39: /*m*/
			[keys[19] changeCurrentChar:'u'];
			[keys[26] changeCurrentChar:'g'];
			[keys[27] changeCurrentChar:'n'];
			[keys[28] changeCurrentChar:'a'];
			[keys[29] changeCurrentChar:'i'];
			[keys[30] changeCurrentChar:'n'];
			[keys[38] changeCurrentChar:'o'];
			[keys[40] changeCurrentChar:'e'];
			[keys[41] changeCurrentChar:'g'];
			[keys[49] changeCurrentChar:'u'];
			break;
		default:
			break;
	}
}

+ (void)setNumberKeyboard:(DynamicKey **)keys{
	keys[11].currentChar = '1';
	keys[12].currentChar = '2';
	keys[13].currentChar = '3';
	keys[14].currentChar = '(';
	keys[15].currentChar = ')';
	keys[16].currentChar = '+';
	keys[17].currentChar = '-';
	keys[18].currentChar = '*';
	keys[19].currentChar = '/';
	keys[20].currentChar = '=';
	keys[22].currentChar = '4';
	keys[23].currentChar = '5';
	keys[24].currentChar = '6';
	keys[25].currentChar = ':';
	keys[26].currentChar = ';';
	keys[27].currentChar = '"';
	keys[28].currentChar = '\'';
	keys[29].currentChar = '?';
	keys[30].currentChar = '!';
	keys[33].currentChar = '7';
	keys[34].currentChar = '8';
	keys[35].currentChar = '9';
	keys[36].currentChar = '0';
	keys[37].currentChar = '<';
	keys[38].currentChar = '>';
	keys[39].currentChar = '~';
}

+ (void)setSpecialKeyboard:(DynamicKey **)keys{
	keys[11].currentChar = '`';
	keys[12].currentChar = '!';
	keys[13].currentChar = '@';
	keys[14].currentChar = '#';
	keys[15].currentChar = '$';
	keys[16].currentChar = '%';
	keys[17].currentChar = '^';
	keys[18].currentChar = '&';
	keys[19].currentChar = '*';
	keys[20].currentChar = '_';
	keys[22].currentChar = '{';
	keys[23].currentChar = '}';
	keys[24].currentChar = '[';
	keys[25].currentChar = ']';
	keys[26].currentChar = '<';
	keys[27].currentChar = '>';
	keys[28].currentChar = '|';
	keys[29].currentChar = '\\';
	keys[30].currentChar = '~';
	keys[33].currentChar = 'z';
	keys[34].currentChar = 'x';
	keys[35].currentChar = 'c';
	keys[36].currentChar = 'v';
	keys[37].currentChar = 'b';
	keys[38].currentChar = 'n';
	keys[39].currentChar = 'm';
}

+ (void)persistChars:(DynamicKey **)keys{
	for (int i = 11; i < 40; i++) {
		if (keys[i].type == kNormalKey) {
			keys[i].originalChar = keys[i].currentChar;
			keys[i].currentChar = '\0';
		}
	}
}

@end