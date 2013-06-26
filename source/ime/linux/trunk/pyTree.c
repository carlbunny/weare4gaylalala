// pyTree.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
  #include <stdlib.h>
#include <string.h>
#include "NodePool.h"

void createTreeToFile(){
	init();
	buildTree();
	saveTree();
}

void buildTreeFromFile(){
	loadTree();
	printf("nodeNum %d\n",poolLen);
}

void testTree(){
	int i;
	char postfix[10];
	buildTreeFromFile();
	findNextChar("shuang",postfix);
	for(i=0;i<strlen(postfix);i++){
		printf("%c ",postfix[i]);
	}

}

