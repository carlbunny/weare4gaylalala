#include "stdafx.h"
#include <stdlib.h>
#include <string.h>

#include "NodePool.h"

#define POOLMAX 1000
#define INNAME "pylist.txt"
#define OUTNAME "/usr/lib/gtk-2.0/2.10.0/immodules/pylist.bin" //jiong!!
#define REVERSENAME "pylist_reverse.bin"
tNode * nodePool;
tNode * reversePool;
int poolLen;
int root_index;

void init() {
	nodePool = (tNode*) malloc(sizeof(tNode) * POOLMAX);
	poolLen = 0;
	root_index = mallocNode('@'); /*root*/
}

int mallocNode(char c) {
	memset(&nodePool[poolLen], 0, sizeof(tNode));
	nodePool[poolLen].a = c;
	return poolLen++;
}

tNode* getNode(int index) {
	return &nodePool[index];
}

tNode* getReverseNode(int index) {
	return &reversePool[index];
}

void cleanNode(int index) {
	memset(&nodePool[index], 0, sizeof(tNode));
}

void buildTree() {
	FILE *fin = fopen(INNAME, "r");
	char one_line[10];
	int buff_size = 10;
	while (fgets(one_line, buff_size, fin) != 0) {
		buildNode(one_line);
	};
}

void saveTree() {
	FILE *fout = fopen(OUTNAME, "wb+");
	fwrite(&poolLen, sizeof(int), 1, fout);
	fwrite(nodePool, sizeof(tNode), poolLen, fout);
	fclose(fout);
}

void loadTree() {
	FILE *fin = fopen(OUTNAME, "rb");
	fread(&poolLen, sizeof(int), 1, fin);
	nodePool = (tNode*) malloc(sizeof(tNode) * poolLen);
	fread(nodePool, sizeof(tNode), poolLen, fin);
	fclose(fin);
}

void loadReverseTree() {
	FILE *fin = fopen(REVERSENAME, "rb");
	fread(&poolLen, sizeof(int), 1, fin);
	reversePool = (tNode*) malloc(sizeof(tNode) * poolLen);
	fread(reversePool, sizeof(tNode), poolLen, fin);
	fclose(fin);
}

void buildNode(char* one_line) {
	unsigned int i, c_index;
	tNode* current = getNode(root_index);
	for (i = 0; i < strlen(one_line) && one_line[i] != 10; i++) { /*windows can`t recognize 0x0A as new line*/
		if ((c_index = current->ptr[one_line[i] - 'a']) == 0) {
			c_index = mallocNode(one_line[i]);
			current->ptr[one_line[i] - 'a'] = c_index;
		}
		current = getNode(c_index);
	}
}

void printTree() {
	int i;
	for (i = 0; i < poolLen; i++) {
		printf("%c ", getNode(i)->a);
	}
}

int findNextChar(char* prefix, char * cArr) {
	unsigned int i, k, c_index;
	tNode* current = getNode(root_index);
	for (i = 0; i < strlen(prefix); i++) {
		if ((c_index = current->ptr[prefix[i] - 'a']) == 0) {
			printf("illegal py\n");
			return -1;
		}
		current = getNode(c_index);
	}
	for (i = 0, k = 0; i < 26; i++) {
		if (current->ptr[i] != 0) {
			cArr[k++] = i + 'a';
		}
	}
	cArr[k] = '\0';
	return 0;
}

int findNextReverseChar(char* prefix, char * cArr) {
	unsigned int i, k, c_index;
	tNode* current = getReverseNode(root_index);
	for (i = 0; i < strlen(prefix); i++) {
		if ((c_index = current->ptr[prefix[i] - 'a']) == 0) {
			printf("illegal py\n");
			return -1;
		}
		current = getReverseNode(c_index);
	}
	for (i = 0, k = 0; i < 26; i++) {
		if (current->ptr[i] != 0) {
			cArr[k++] = i + 'a';
		}
	}
	cArr[k] = '\0';
	return 0;
}
