#ifndef _PYDADASTRUCTURE_H_
#define _PYDADASTRUCTURE_H_

#include <stdlib.h>

#define APPLICATION_NUM 12
#define ALPHABET_NUM 26
#define LOWCASE_ASC2_BOUND 97

typedef struct
{
	char *str;
	size_t ssize;
}leaf;
/*a double linked node has a char value and a htable
if it has some children nodes.*/
typedef struct
{
	char value;
	size_t isize;//size of indices,i.e number of children
	void **indices;//indices for children node iteration,sorted ascendingly
	size_t lfnum;//total number of all the leaves of this node's children nodes
    void **hash_table;//hash_table[ALPHABET_NUM]
	void *next, *prev;//double linked node forward/back

	int hasleaves;//whether has leaves
	size_t lsize;//number of leaves
	leaf *leaves;//leaves
}node;
//double linked list
typedef struct
{
	size_t nsize;//size of list
	node *head;//head node without value
	node *cptr;//current pointer
}nlist;
//tree
typedef struct
{
	node *root;//root node
	//int cstr_length;//current string's length, '\0' unincluded
	//char *cur_str;//current string
	node *cptr;//cptr: current pointer
	nlist track_list;//list maintains the track of forward/back for current string
}tree;

//global varible
extern tree* app_list[APPLICATION_NUM];

//functions
//void init_node(node *n, const char **data, int *off_s, int off_c, int flag);// 1:create; 0:continue; -1:end
void create_node(node **n);
void init_node(node **n, const char *str);
void init_tree(tree **t);//initialization
void reset_tree(tree *t);//clear out the tree's current state
void distroy_node(node *n);
void distroy_tree(tree *t);//deinitialization
int forward(tree *t, char key);//single-step forward
void backward(tree *t);//single-step backward
void get_list(tree *t, char **list, int *size);
void get_sub_tree(tree *t, tree *out_t);//construct a new tree based on the current node
void str_list_dfs(const node *ptr, char **out_list, size_t *csize, size_t *count);//deep find search for leaves
void get_str_list(const tree *t, char **out_list, size_t *size, size_t *csize);//get the current string list implicitly
int get_str_list2(tree *t, char *in_str, size_t insize, char **out_list, size_t *size, size_t *csize);//get the string list explicitly for the input string

#endif
