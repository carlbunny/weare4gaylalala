
typedef struct TreeNode{
	char            c;
	short int       index;
	struct TreeNode *next;
	struct TreeNode *children;
}*TreeNodePtr;

@interface PinyinTree:NSObject{

};

+ (void)initTree;
+ (void)releaseTree;

+ (void)freeNode:(TreeNodePtr)node;
+ (TreeNodePtr)getLastNode:(char*)pinyin;
+ (void)addNode:(char*)pinyin:(int)index;
+ (char*)getNexts:(char*)pinyin;
+ (BOOL)isPinYin:(char*)pinyin;
+ (short int)getIndex:(char*)pinyin;

@end;