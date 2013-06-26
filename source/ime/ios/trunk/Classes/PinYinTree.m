
#import "PinYinTree.h"

@implementation PinYinTree

static TreeNodePtr rootNode;
static char resultArray[7];

+ (void)initTree{
	rootNode = malloc(sizeof(struct TreeNode));
	rootNode->next = nil;
	rootNode->children = nil;
	rootNode->c = 0;
	rootNode->ended = NO;
	
	NSString *filePath = [[NSBundle mainBundle] pathForResource:@"pylist" ofType:@"txt"];
	FILE* fd = fopen([filePath cStringUsingEncoding:1], "r");
	char buffer[8];
	if (fd != nil){
		while (fgets(buffer, 8, fd) != 0){
			[PinYinTree addNode:buffer];
		}
		fclose(fd);
	}else {
		NSLog(@"Open pylist.txt failed!\n");
	}
}

+ (void)releaseTree{
	[PinYinTree freeNode:rootNode];
}

+ (void)freeNode:(TreeNodePtr)node{
	TreeNodePtr nextNode, tempNode;
	nextNode = node->next;
	while (nextNode != nil) {
		tempNode = nextNode->next;
		[PinYinTree freeNode:nextNode];
		nextNode = tempNode;
	}
	if (node->children != nil) {
		[PinYinTree freeNode:node->children];
	}
	free(node);
}

+ (void)addNode:(char*)pinyin{
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
			tempNode->ended = NO;
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
				tempNode->ended = NO;
			}
		}
		
		currentNode = tempNode;
		i++;
	}
	currentNode->ended = YES;
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
	TreeNodePtr node = [PinYinTree getLastNode:pinyin];
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
	TreeNodePtr node = [PinYinTree getLastNode:pinyin];
	return node->ended;
}

@end