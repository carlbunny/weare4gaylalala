
#import "PinyinTree.h"

@implementation PinyinTree

static TreeNodePtr rootNode;
static char resultArray[7];

+ (void)initTree{
	rootNode = malloc(sizeof(struct TreeNode));
	rootNode->next = nil;
	rootNode->children = nil;
	rootNode->c = 0;
	rootNode->index = -1;
	
	NSString *filePath = [[NSBundle mainBundle] pathForResource:@"pylist" ofType:@"txt"];
	FILE* fd = fopen([filePath cStringUsingEncoding:1], "r");
	char buffer[8];
	if (fd != nil){
		short int i = 0;
		while (fgets(buffer, 8, fd) != 0){
			[PinyinTree addNode:buffer:i];
			i++;
		}
		fclose(fd);
	}else {
		NSLog(@"Open pylist.txt failed!\n");
	}
}

+ (void)releaseTree{
	[PinyinTree freeNode:rootNode];
}

+ (void)freeNode:(TreeNodePtr)node{
	TreeNodePtr nextNode, tempNode;
	nextNode = node->next;
	while (nextNode != nil) {
		tempNode = nextNode->next;
		[PinyinTree freeNode:nextNode];
		nextNode = tempNode;
	}
	if (node->children != nil) {
		[PinyinTree freeNode:node->children];
	}
	free(node);
}

+ (void)addNode:(char*)pinyin:(int)index{
	int i = 0;
	TreeNodePtr currentNode = rootNode;
	TreeNodePtr tempNode;
	while (pinyin[i] >= 'a' && pinyin[i] <= 'z') {
		char c = pinyin[i];
		if (currentNode->children == nil) {
			currentNode->children = malloc(sizeof(struct TreeNode));
			tempNode = currentNode->children;
			tempNode->next = nil;
			tempNode->children = nil;
			tempNode->c = c;
			tempNode->index = -1;
		}else {
			tempNode = currentNode->children;
			while (tempNode->next != nil) {
				if (tempNode->c == c) {
					break;
				}
				tempNode = tempNode->next;
			}
			if (tempNode->c != c){
				tempNode->next = malloc(sizeof(struct TreeNode));
				tempNode = tempNode->next;
				tempNode->next = nil;
				tempNode->children = nil;
				tempNode->c = c;
				tempNode->index = -1;
			}
		}
		
		currentNode = tempNode;
		i++;
	}
	currentNode->index = index;
}

+ (TreeNodePtr)getLastNode:(char*)pinyin{
	int i = 0;
	TreeNodePtr currentNode = rootNode;
	TreeNodePtr tempNode;
	if (pinyin[0] == 0){
		return nil;
	}
	while (pinyin[i] != 0) {
		char c = pinyin[i];
		if (currentNode->children == nil) {
			return nil;
		}else {
			tempNode = currentNode->children;
			while (tempNode->next != nil) {
				if (tempNode->c == c) {
					break;
				}
				tempNode = tempNode->next;
			}
			if (tempNode->c != c){
				return nil;
			}
		}
		
		currentNode = tempNode;
		i++;
	}
	return currentNode;
}

+ (char*)getNexts:(char*)pinyin{
	TreeNodePtr node = [PinyinTree getLastNode:pinyin];
	int index = 0;
	if (node == nil){
		resultArray[index] = '\0';
	}else {
		node = node->children;
		while (node != nil) {
			resultArray[index] = node->c;
			node = node->next;
			index++;
		}
		resultArray[index] = '\0';
	}
	return resultArray;
}

+ (BOOL)isPinYin:(char*)pinyin{
	TreeNodePtr node = [PinyinTree getLastNode:pinyin];
	if (node == nil) {
		return NO;
	}
	return node->index != -1;
}

+ (short int)getIndex:(char *)pinyin{
	TreeNodePtr node = [PinyinTree getLastNode:pinyin];
	if (node == nil) {
		return -1;
	}
	return node->index != -1;
}

@end