
#import "WordRoot.h"

/*
 File format:
 ==============repeat===============
 
 int         rootAddress
 
 =============end repeat============
 */
static int* rootFile;

int wordRootGetRoot(short int pinyin){
	return rootFile[pinyin];
}
