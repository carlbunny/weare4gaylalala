#pragma once

typedef struct _tNode {
	char a;
	short ptr[26];
} tNode;

extern int poolLen;
void init();

int mallocNode(char c);

tNode* getNode(int index);
tNode* getReverseNode(int index);

void cleanNode(int index);

void buildTree();

void saveTree();

void loadTree();
void loadReverseTree();

void buildNode(char* one_line);

/*for test*/
void printTree();

/*the only function to call after call the loadTree*/
int findNextChar(char* prefix, char * cArr);
int findNextReverseChar(char* prefix, char * cArr);
