#include "pyds.h"
#include "pydata.h"
#include <string.h>

//void init_node(node *n, const char **data, int *off_s, int off_c, int flag/*1:create; 0:continue; -1:end*/)
//{
//	const char *str;
//	char ch;
//	int has_next, len1, i, idx;
//	int *tmp_buffer;
//	node *child;
//
//	if(flag == 1 && (data[*off_s][off_c] != '\0' || ((*off_s)+1 != data_v1_size && n->value == data[(*off_s)+1][(off_c) -1] && data[*off_s][0] == data[(*off_s)+1][0])))
//	{
//		n->indices = (void**)malloc(ALPHABET_NUM*sizeof(void*));
//		for(i = 0; i < ALPHABET_NUM; i++)
//			n->indices[i] = NULL;
//		n->isize = 0;
//	}
//	has_next = 1;
//	while(has_next)
//	{
//		if(*off_s == data_v1_size)
//		{
//			ch = '\0';
//		}
//		else
//		{
//			str = data[*off_s];
//			ch = str[(off_c)++];
//		}
//		if(ch != '\0' && (*off_s == 0 || (*off_s) > 0 && data[(*off_s)-1][0] == data[*off_s][0]))
//		{
//			idx = -1;
//			for(i = 0; i < n->isize; i++)
//			{
//				if(((node*)n->indices[i])->value == ch)
//				{
//					idx = i;
//					break;
//				}
//			}
//			if(idx == -1)
//			{
//				child = (node*)malloc(sizeof(node));
//				child->value = ch;
//				n->indices[n->isize++] = (void*)child;
//				init_node(child, data, off_s, off_c, 1);
//				off_c--;
//			}
//			else
//			{
//				child = (node*)n->indices[idx];
//				init_node(child, data, off_s, off_c, 0);
//				off_c--;
//				(*off_s)--;
//			}
//		}
//		else
//		{
//			has_next = 0;
//			(*off_s)++;
//		}
//	}
//
//}

void create_node(node **n)
{
	int i;
	*n = (node*)malloc(sizeof(node));
	(*n)->indices = (void**)malloc(ALPHABET_NUM*sizeof(void*));
	for(i = 0; i < ALPHABET_NUM; i++)
		(*n)->indices[i] = NULL;
	(*n)->isize = 0;
}

void init_node(node **n, const char *str)
{
	int i, j, len, is_in;
	node *ptr, *tmp;
	len = strlen(str);
	ptr = (*n);
	for(i = 0; i < len; i++)
	{
		is_in = 0;
		for(j = 0; j < ptr->isize; j++)
		{
			tmp = (node*)ptr->indices[j];
			if(tmp->value == str[i])
			{
				is_in = 1;
				ptr = tmp;
				break;
			}
		}
		if(!is_in)
		{
			create_node(&tmp);
			tmp->value = str[i];
			ptr->indices[ptr->isize++] = (void*)tmp;
			ptr = tmp;
		}
	}
}

void init_tree(tree **t)
{

	int off_s;
	int off_c,flag, i;
	const char *str;
	char head;
	off_s = 0;
	off_c = 0;
	flag = 1;
	create_node(&((*t)->root));
	(*t)->cptr = (*t)->root;
	head = 0;
	for(i = 0; i < data_v1_size; i++)
	{
		str = data_v1[i];
		init_node(&((*t)->root), str);
	}
	(*t)->track_list.head = NULL;
	(*t)->track_list.cptr = (*t)->track_list.head;
	(*t)->track_list.nsize = 0;
}

void reset_tree(tree *t)
{
	t->cptr = t->root;
	t->track_list.head = NULL;
	t->track_list.cptr = t->track_list.head;
	t->track_list.nsize = 0;
}

void distroy_node(node *n)
{
	int i;

	if(n->isize == 0)
	{
		free(n->indices);
		free(n);
		n = NULL;
		return;
	}
	for(i = 0; i < n->isize; i++)
	{
		distroy_node((node*)n->indices[i]);
	}
	free(n->indices);
	free(n);
	n = NULL;
}

void distroy_tree(tree *t)
{
	distroy_node(t->root);
	t->track_list.cptr = t->track_list.head = t->cptr = t->root = NULL;
	free(t);
	t = NULL;
}

int forward(tree *t, char key)
{
	/*char *new_str;*/
	node *ptr;
	int i;

	for(i = 0; i < t->cptr->isize; i++)
	{
		ptr = (node*)t->cptr->indices[i];
		if(ptr->value == key)
			break;
		else
			ptr = NULL;
	}
	if(ptr == NULL)
		return 0;
	t->cptr = ptr;

	if(t->track_list.head == NULL)
	{
		t->track_list.head = ptr;
		t->track_list.cptr = ptr;
	}
	else
	{
		t->track_list.cptr->next = (void*)ptr;
		ptr->prev = (void*)t->track_list.cptr;
		t->track_list.cptr = ptr;
	}
	t->track_list.nsize++;

	return 1;
}

void backward(tree *t)
{
	/*char *new_str;*/
	node *ptr;

	/*new_str = (char*)malloc((t->cstr_length)*sizeof(char));
	strncpy(new_str, t->cur_str, t->cstr_length - 1);
	free(t->cur_str);
	t->cur_str = NULL;
	t->cstr_length--;
	new_str[t->cstr_length] = '\0';
	t->cur_str = new_str;*/
	if(t->track_list.cptr == t->track_list.head)
	{
		t->track_list.head = NULL;
		t->track_list.cptr = t->track_list.head;
		t->cptr = t->root;
		t->track_list.nsize--;
	}
	else
	{
		ptr = t->track_list.cptr;
		t->track_list.cptr = (node*)ptr->prev;
		t->track_list.cptr->next = NULL;
		ptr->prev = NULL;
		t->cptr = t->track_list.cptr;
		t->track_list.nsize--;
		if(t->cptr == t->track_list.head)
			t->cptr = t->root;
	}
}

void get_list(tree *t, char **list, int *size)
{
	node *ptr;
	int i;

	ptr = t->cptr;
	(*list) = (char*)malloc(ptr->isize*sizeof(char));
	*size = ptr->isize;
	for(i = 0; i < ptr->isize; i++)
	{
		(*list)[i] = ((node*)ptr->indices[i])->value;
	}
}

void get_sub_tree(tree *t, tree *out_t)
{
}

//void str_list_dfs(const node *ptr, char **out_list, size_t *csize,  size_t *count)
//{
//	size_t i;
//	node *child;
//	leaf *lf;
//
//	if(ptr->hasleaves)
//	{
//		for(i = 0; i < ptr->lsize; i++)
//		{
//			lf = &(ptr->leaves[i]);
//			out_list[*count] = (char*)malloc(lf->ssize*sizeof(char));
//			csize[*count] = lf->ssize;
//			(*count)++;
//		}
//	}
//	for(i = 0; i < ptr->isize; i++)
//	{
//		child = (node*)ptr->hash_table[ptr->indices[i]];
//		str_list_dfs(child, out_list, csize, count);
//	}
//}
//
//void get_str_list(const tree *t, char **out_list, size_t *size, size_t *csize)
//{
//	size_t count;
//	node *ptr;
//
//	out_list = (char**)malloc(t->cptr->lfnum*sizeof(char*));
//	count = 0;
//	ptr = t->cptr;
//	*size = ptr->lfnum;
//	csize = (size_t*)malloc(ptr->lfnum*sizeof(size_t));
//	while(count <= ptr->lfnum)
//	{
//		str_list_dfs(ptr, out_list, csize, &count);
//	}
//
//}
//
//int get_str_list2(tree *t, char *in_str, size_t insize, char **out_list, size_t *size, size_t *csize)
//{
//	size_t i, count;
//	char key;
//
//	for(i = 0; i < insize; i++)
//	{
//		if(!forward(t, in_str[i]))
//			return 0;
//	}
//	get_str_list(t, out_list, size, csize);
//	return 1;
//}
