
#import "WordNode.h"

/*
 File format:
 ==============repeat===============

 short int    pinyin           2
 int          wordAddress      4
 short int    childrenSize     2
 int          childrenAddress  4
 
 =============end repeat============
*/

unsigned char* nodeFile;

const int WORD_NODE_SIZE = 12;

int wordNodeGetChild(int current, short int pinyin){
	short int childrenSize;
	int childrenAddress;
	memcpy(&childrenSize, &nodeFile[6], 2);
	memcpy(&childrenAddress, &nodeFile[8], 4);
	
	int i = 0;
	int j = childrenSize;
	int m = 0;
	int address;
	short int value;
	
	while (i <= j) {
		m = (i + j) / 2;
		address = childrenAddress + m * WORD_NODE_SIZE;
		memcpy(&value, &nodeFile[address], 2);
		if (pinyin == value) {
			return address;
		}else if (pinyin > value) {
			i = m + 1;
		}else{
			j = m - 1;
		}
	}
	
	return -1;
}

int wordNodeGetAddress(int current){
	int address;
	memcpy(&address, &nodeFile[2], 4);
	return address;
}