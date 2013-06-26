#include "global.h"

GdkGC *gc_grey;
GdkGC *gc_bright;
GdkGC *gc_default;
GdkGC *gc_white;
GdkGC *gc_line;
GdkGC *gc_yellow;
GdkGC *gc_green;

GdkPoint cur_pos;

GtkWidget *keyboard;

GdkColor color_def = { 0, 30000, 30000, 30000 };
GdkColor color_bright = { 0, 65000, 2000, 2000 };
GdkColor color_green = {0,0,65535,0};
GdkColor color_line = {0, 65535, 65535 , 65535};
GdkColor color_yellow = {0,65535,65535,0};

int trim_key_frame = TRUE;

gfloat hexagon_frame_factor = 0.15f;

gint SENSITIVITY = -3;

gint input_mode = INPUT_MODE_CH;
gint is_upper = FALSE;
gint line_width = 3;
GdkLineStyle line_style = GDK_LINE_SOLID;
GdkCapStyle cap_style = GDK_CAP_ROUND;
GdkJoinStyle join_style = GDK_JOIN_ROUND;
gint line_trimming = 4;
gint key_info_index = 0;
