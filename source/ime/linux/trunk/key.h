#include <gtk/gtk.h>

#define NAME_LEN 5

struct key{
	char input_lowercase;
	char input_uppercase;
	char input_num;
	char tobe_input;
	char end;

	gint width;
	gint mode;
	gint valid;
	gint command;
};

struct hex_key{
	char name;
	gint x;
	gint y;
	gint command;
};
