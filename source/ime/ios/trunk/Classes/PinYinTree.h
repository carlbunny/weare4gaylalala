
typedef struct TreeNode{
	char            c;
	BOOL            ended;
	struct TreeNode *next;
	struct TreeNode *children;
}*TreeNodePtr;

@interface PinYinTree:NSObject{

};

+ (void)initTree;
+ (void)releaseTree;

+ (void)freeNode:(TreeNodePtr)node;
+ (TreeNodePtr)getLastNode:(char*)pinyin;
+ (void)addNode:(char*)pinyin;
+ (char*)getNexts:(char*)pinyin;
+ (BOOL)isPinYin:(char*)pinyin;

@end;