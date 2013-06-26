#include <gtk/gtk.h>

#ifndef GLOBAL_H
#define GLOBAL_H

#define INPUT_MODE_CH 1
#define INPUT_MODE_EN_L 2
#define INPUT_MODE_EN_U 3
#define INPUT_MODE_NUM 4

#define BUILD_SO 0

#define MAX_TEXT_SIZE 200
#define ONE_LINE_SIZE 10

extern GdkGC *gc_grey;
extern GdkGC *gc_bright;
extern GdkGC *gc_default;
extern GdkGC *gc_white;
extern GdkGC *gc_line;
extern GdkGC *gc_yellow;
extern GdkGC *gc_green;


extern GtkWidget *keyboard;

extern GdkColor color_def;
extern GdkColor color_bright;
extern GdkColor color_line;
extern GdkColor color_yellow;
extern GdkColor color_green;

extern GdkPoint cur_pos;
extern int trim_key_frame;

extern gfloat hexagon_frame_factor;

extern gint SENSITIVITY;// sensitivity for cell bound detection and cell mutual exclusion

extern gint input_mode;
extern gint is_upper;
extern gint line_width;
extern GdkLineStyle line_style;
extern GdkCapStyle cap_style;
extern GdkJoinStyle join_style;
extern gint line_trimming;

typedef struct {
	char c;
	gint min_distance;
	gint count;
	gint is_valid;
	GdkPoint p;
}key_info;

#define MAX_KEY_INFO_INDEX 20
extern gint key_info_index;
key_info key_info_set[MAX_KEY_INFO_INDEX];
#endif
