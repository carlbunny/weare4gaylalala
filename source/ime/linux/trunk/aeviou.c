#include <gtk/gtk.h>
#include <gtk/gtkimmodule.h>
#include <glib/gi18n.h>
#include <math.h>
#include "global.h"
#include "keyboard.h"
#include "fcitx.h"
#include "NodePool.h"
#include <string.h>
#include "pyds.h"

#define W_WIDTH 800
#define W_HEIGHT 260

#define CLICK_THRESH 16
#define H_CONFIRM_THRESH 1

static gint running = 0;
static gint expose_event(GtkWidget *widget, GdkEventExpose *event);
static gint button_press_event(GtkWidget *widget, GdkEventButton *event);
static gint button_release_event(GtkWidget *widget, GdkEventButton *event);
static gint motion_notify_event(GtkWidget *widget, GdkEventMotion *event);
static gint configure_event(GtkWidget *widget, GdkEventConfigure *event);
void destroy(GtkWidget *widget, GdkEvent *event, gpointer data);

void initialize(GtkWidget *widget);
void add_input(char c);
void paint(GtkWidget *widget);
void paint_func_key(GtkWidget *widget, GdkGC *gc, int offset_x, int i, int j);
void refresh_cur_hex(gint x, gint y);
void get_hex(GdkPoint* points, gint direction);
int get_key(GdkPoint* p, int x, int y);
int get_key2(GdkPoint* p, int x, int y);
char recognize_key(int x, int y, int r);
int get_key_pos(GdkPoint* p, int x, int y);
int get_record_inx(int x,int y);

void change_kb(int row, int col, char c);
void alter_input2(char c);

void clear_to_lowercase(void);
void clear_to_uppercase(void);
void clear_to_num(void);

void get_Chinese_word(void);
void next_page(void);
void pre_page(void);
void commit_hz(int inx);

void clear_fcitx_result(void);
void clear_input_all(void);
int get_surrounding_key(GdkPoint* p, int d, int x, int y);
void finish_one(void);
void copy_input_string(void);
void refresh_mode(void);
void refresh_cur_pos(int x, int y);
void paint_key_frame(GdkDrawable *drawable, GdkGC *gc, gboolean filled, gint x,
		gint y, gint width, gint height);
void draw_line(GdkDrawable *drawable, GdkGC *gc, gint x1, gint y1, gint x2,
		gint y2);

void generate_path(int x, int y);
void motion_act(int x,int y);

void alter_tobe_input(char c);
int get_distance(int x, int y, char c);
int main_entry(int argc, char *argv[]);

//gtk im interface exports here
void im_module_init(GTypeModule *module);
void im_module_exit(void);
void im_module_list(const GtkIMContextInfo ***contexts, int *n_contexts);
GtkIMContext * im_module_create(const gchar *context_id);
void gtk_aeviou_im_context_register_type(GTypeModule* type_module);
static GType gtk_im_aeviou_im_context_type = 0;
struct _GtkAeviouImContextClass {
	GtkIMContextClass parent_class;
};
struct _GtkAeviouImContext {
	GtkIMContext parent;
};
typedef struct _GtkAeviouImContextClass GtkAeviouImContextClass;
typedef struct _GtkAeviouImContext GtkAeviouImContext;
static GObjectClass* gtk_aeviou_im_context_parent_class = NULL;
static GtkAeviouImContext *Aeviou_context;
GType gtk_aeviou_im_context_get_type(void) G_GNUC_CONST;

GtkWidget *window;
GtkWidget *label;
GtkWidget *vbox;

PangoFontDescription*font_desc;
GdkPixmap *pixmap = NULL;
GdkRectangle update_rect = { 0, 0, W_WIDTH,W_HEIGHT };
char *wait_pylist;
gint mouse_pressed = FALSE;
gint input_method = 1;
char fcitx_result[MAX_FCITX][200];
int phrase_size = 0;
char last_input = '\0';
GdkPoint p_record[MAX_PY_INPUT]= { { -1, -1 } };
GdkPoint p_tap = {-1,-1};
char input_chars[MAX_PY_INPUT]= { '\0' };
char input_all[MAX_PY_INPUT]={'\0'};
char text_area[MAX_TEXT_SIZE*2+1]={'\0'};
int input_all_inx = 0;
int input_index = 0;
struct hex_key cur_hex = { '\0', 0, 0 };

GdkPoint press_pos;

#define CONTEXT_ID "aeviou"

/** NOTE: Change the default language from "" to "*" to enable this input method by default for all locales.
 */
static const GtkIMContextInfo info = { CONTEXT_ID, /* ID */
N_("Aeviou"), /* Human readable name */
"aeviou-gtk-input-method", /* Translation domain. Defined in configure.ac */
"", /* Dir for bindtextdomain (not strictly needed for "gtk+"). Defined in the Makefile.am */
"" /* Languages for which this module is the default */
};

static const GtkIMContextInfo *info_list[] = { &info };

void im_module_init(GTypeModule *module) {
	gtk_aeviou_im_context_register_type(module);
}

void im_module_exit(void) {
}

void im_module_list(const GtkIMContextInfo ***contexts, int *n_contexts) {
	*contexts = info_list;
	*n_contexts = G_N_ELEMENTS(info_list);
}

GtkIMContext *
im_module_create(const gchar *context_id) {
	if (strcmp(context_id, CONTEXT_ID) == 0) {
		GtkIMContext* imcontext = GTK_IM_CONTEXT(g_object_new(
				gtk_aeviou_im_context_get_type(), NULL));
		return imcontext;
	} else
		return NULL;
}
static void gtk_aeviou_im_context_class_init(GtkAeviouImContextClass *klass) {
	GtkIMContextClass* im_context_class;

	/* Set this so we can use it later: */
	gtk_aeviou_im_context_parent_class = g_type_class_peek_parent(klass);

	/* Specify our vfunc implementations: */
	im_context_class = GTK_IM_CONTEXT_CLASS(klass);
}
static void gtk_aeviou_im_context_init(GtkAeviouImContext *self) {
	Aeviou_context = self;
	if (!running) {
		running = 1;
		printf("called main\n");
		//main_entry(0, NULL);
		// !! change to main_entry to enable shared library
	}
}

GType gtk_aeviou_im_context_get_type(void) {
	g_assert(gtk_im_aeviou_im_context_type != 0);
	return gtk_im_aeviou_im_context_type;
}

void gtk_aeviou_im_context_register_type(GTypeModule* type_module) {
	if (gtk_im_aeviou_im_context_type == 0) {
		static const GTypeInfo im_context_Aeviou_info = {
				sizeof(GtkAeviouImContextClass), (GBaseInitFunc)NULL,
				(GBaseFinalizeFunc)NULL,
				(GClassInitFunc) gtk_aeviou_im_context_class_init, NULL, NULL,
				sizeof(GtkAeviouImContext), 0,
				(GInstanceInitFunc) gtk_aeviou_im_context_init, 0,};

			gtk_im_aeviou_im_context_type = g_type_module_register_type(
					type_module, GTK_TYPE_IM_CONTEXT, "GtkAeviouImContext",
					&im_context_Aeviou_info, 0);
		}
	}

int main(int argc, char *argv[]) {
	gtk_init(&argc, &argv);

	keyboard = gtk_drawing_area_new();
	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	label = gtk_label_new("");
	gtk_label_set_line_wrap (label, TRUE);
	vbox = gtk_vbox_new(FALSE,5);

	//gtk_text_set_editable(text,TRUE);
	//   gtk_window_set_decorated(GTK_WINDOW(window),FALSE);
	g_signal_connect(G_OBJECT(window), "destroy", G_CALLBACK(destroy), NULL);

	font_desc = pango_font_description_from_string("Song");
	pango_font_description_set_size(font_desc, 15 * PANGO_SCALE);
	gtk_widget_modify_font(keyboard, font_desc);
	gtk_widget_modify_font(label, font_desc);

	gtk_drawing_area_size((GtkDrawingArea *) keyboard, W_WIDTH,W_HEIGHT);
	gtk_widget_set_size_request(label,W_WIDTH,440-W_HEIGHT);
	g_signal_connect(G_OBJECT(keyboard), "expose_event", G_CALLBACK(
			expose_event), NULL);
	gtk_signal_connect(GTK_OBJECT(keyboard), "configure_event",
			(GtkSignalFunc) configure_event, NULL);
	gtk_signal_connect(GTK_OBJECT(keyboard), "motion_notify_event",
			(GtkSignalFunc) motion_notify_event, NULL);
	gtk_signal_connect(GTK_OBJECT(keyboard), "button_press_event",
			(GtkSignalFunc) button_press_event, NULL);
	gtk_signal_connect(GTK_OBJECT(keyboard), "button_release_event",
			(GtkSignalFunc) button_release_event, NULL);

	gtk_widget_set_events(keyboard, GDK_EXPOSURE_MASK | GDK_LEAVE_NOTIFY_MASK
			| GDK_BUTTON_PRESS_MASK | GDK_BUTTON_RELEASE_MASK
			| GDK_POINTER_MOTION_MASK | GDK_POINTER_MOTION_HINT_MASK);

	gtk_box_pack_end(vbox,keyboard,TRUE,TRUE,5);
	gtk_box_pack_end(vbox,label,TRUE,TRUE,5);
	gtk_container_add(GTK_CONTAINER(window), vbox);

	gtk_widget_show(keyboard);
	gtk_widget_show(label);
	gtk_widget_show(vbox);
	gtk_widget_show(window);

	gtk_main();

	//	pango_font_description_free (font_desc);

	return 0;
}

static gint expose_event(GtkWidget *widget, GdkEventExpose *event) {

	gdk_draw_pixmap(widget->window, widget->style->fg_gc[GTK_WIDGET_STATE(
			widget)], pixmap, event->area.x, event->area.y, event->area.x,
			event->area.y, event->area.width, event->area.height);

	return TRUE;
}

void paint(GtkWidget *widget) {
	int i, j, n, offset_x,offset_y;
	char line[ONE_LINE_SIZE*2+1];
	GdkGC *gc = gc_default;
	gdk_draw_rectangle(pixmap, gc_white, TRUE, 0, 0, W_WIDTH,W_HEIGHT);

	PangoLayout *layout = gtk_widget_create_pango_layout(widget, input_all);
	gdk_draw_layout(pixmap, gc, 0, 4.5 * HEIGHT, layout);
	for (i = 0; i < ROW; i++) {
		offset_x = row_indent[i];
		for (j = 0; j < COL; j++) {
			if (keys[i][j].valid) {

				if (keys[i][j].mode == UNPRESSED_MODE) {
					//white, impossible successors
					gc = gc_default;
				} else if (keys[i][j].mode == PRESSED_MODE) {
					//default color(black), next successors
					gc = gc_white;
					paint_key_frame(pixmap, gc_green, TRUE, offset_x, i
							* HEIGHT, keys[i][j].width, HEIGHT);
					paint_key_frame(pixmap, gc_grey, FALSE, offset_x, i
							* HEIGHT, keys[i][j].width, HEIGHT);
				} else if (keys[i][j].mode == WAITING_MODE) {
					//grey, possible successors, but not next ones
					gc = gc_white;
					paint_key_frame(pixmap, gc_grey, TRUE, offset_x,
							i * HEIGHT, keys[i][j].width, HEIGHT);
					paint_key_frame(pixmap, gc, FALSE, offset_x, i * HEIGHT,
							keys[i][j].width, HEIGHT);
				}
				//draw key-frames around keys
				//only rows between 1 and 3 may not have been drawn
				if (i >= 1 && i <= 3) {
					paint_key_frame(pixmap, gc_grey, FALSE, offset_x, i
							* HEIGHT, keys[i][j].width, HEIGHT);
				}
				// draw function keys
				if (keys[i][j].command != COM_INPUT && keys[i][j].tobe_input
						== '\0') {
					paint_func_key(widget, gc, offset_x, i, j);
				}
				// draw letters on keys
				layout = gtk_widget_create_pango_layout(widget,
						&keys[i][j].tobe_input);
				pango_layout_set_width(layout, keys[i][j].width);
				pango_layout_set_alignment(layout, PANGO_ALIGN_CENTER);
				gdk_draw_layout(pixmap, gc, offset_x + keys[i][j].width / 2, i
						* HEIGHT + HEIGHT / 4, layout);
				offset_x += keys[i][j].width;
			}
		}
	}
	//draw Chinese character
	offset_x = HZ_WIDTH;
	for (i = 0; i < phrase_size; i++) {
		layout = gtk_widget_create_pango_layout(widget, fcitx_result[i]);
		gdk_draw_layout(pixmap, gc, offset_x, HZ_OFFSET_Y,layout);
		offset_x += HZ_WIDTH*(strlen(fcitx_result[i])/2+1);
	}
	//draw page up, page down
	layout = gtk_widget_create_pango_layout(widget, "pre");
	gdk_draw_layout(pixmap, gc, W_WIDTH-HZ_WIDTH*8, HZ_OFFSET_Y/2,layout);
	layout = gtk_widget_create_pango_layout(widget, "next");
	gdk_draw_layout(pixmap, gc, W_WIDTH-HZ_WIDTH*4, HZ_OFFSET_Y/2,layout);

	GdkPoint p = { -1, -1 };
	GdkPoint p2 = { -1, -1 };

	//draw tap point in non-py mode
	if(p_tap.x >0){
		get_key_pos(&p, p_tap.x, p_tap.y);
		paint_key_frame(pixmap, gc_bright, TRUE, p.x, p.y, WIDTH,HEIGHT);
		layout = gtk_widget_create_pango_layout(widget,
							&keys[p_tap.y][p_tap.x].tobe_input);
		pango_layout_set_width(layout,
				keys[p_tap.y][p_tap.x].width);
		pango_layout_set_alignment(layout, PANGO_ALIGN_CENTER);
		gdk_draw_layout(pixmap, gc_white, p.x
				+ keys[p_tap.y][p_tap.x].width / 2, p.y
				+ HEIGHT / 4, layout);
	}

	//draw input keys with different color and input trace
	if (cur_hex.name != '\0') {
		int i;
		for (i = 0; i < input_index; i++) {
			get_key_pos(&p, p_record[i].x, p_record[i].y);
			paint_key_frame(pixmap, gc_bright, TRUE, p.x, p.y, WIDTH,HEIGHT);
			layout = gtk_widget_create_pango_layout(widget,
					&keys[p_record[i].y][p_record[i].x].tobe_input);
			pango_layout_set_width(layout,
					keys[p_record[i].y][p_record[i].x].width);
			pango_layout_set_alignment(layout, PANGO_ALIGN_CENTER);
			gdk_draw_layout(pixmap, gc_white, p.x
					+ keys[p_record[i].y][p_record[i].x].width / 2, p.y
					+ HEIGHT / 4, layout);
		}
		for (i = 0; i < input_index - 1; i++) {
			get_key_pos(&p, p_record[i].x, p_record[i].y);
			get_key_pos(&p2, p_record[i + 1].x, p_record[i + 1].y);
			draw_line(pixmap, gc_line, p.x + WIDTH / 2, p.y + HEIGHT / 2, p2.x
					+ WIDTH / 2, p2.y + HEIGHT / 2);
		}
	}
	gtk_widget_draw(widget, &update_rect);

}

void draw_line(GdkDrawable *drawable, GdkGC *gc, gint x1, gint y1, gint x2,
		gint y2) {
	gdk_draw_line(drawable, gc,
			(x1 * line_trimming + x2) / (line_trimming + 1), (y1
					* line_trimming + y2) / (line_trimming + 1), (x2
					* line_trimming + x1) / (line_trimming + 1), (y2
					* line_trimming + y1) / (line_trimming + 1));
}

void paint_func_key(GtkWidget *widget, GdkGC *gc, int offset_x, int i, int j) {
	PangoLayout *layout;

	if ((input_mode == INPUT_MODE_CH && keys[i][j].command == COM_CH)
			|| (input_mode == INPUT_MODE_EN_L && keys[i][j].command == COM_EN)
			|| (input_mode == INPUT_MODE_EN_U && keys[i][j].command == COM_EN)
			|| (input_mode == INPUT_MODE_NUM && keys[i][j].command == COM_NUM)
			|| (is_upper && keys[i][j].command == COM_UPPER)) {

		paint_key_frame(pixmap, gc_yellow, TRUE, offset_x, i * HEIGHT,
				keys[i][j].width, HEIGHT);
		paint_key_frame(pixmap, gc_default, FALSE, offset_x, i * HEIGHT,
				keys[i][j].width, HEIGHT);
	}

	switch (keys[i][j].command) {
	case COM_CH:
		layout = gtk_widget_create_pango_layout(widget, "CH");
		break;
	case COM_EN:
		layout = gtk_widget_create_pango_layout(widget, "EN");
		break;
	case COM_NUM:
		layout = gtk_widget_create_pango_layout(widget, "NUM");
		break;
	case COM_UPPER:
		layout = gtk_widget_create_pango_layout(widget, "CAP");
		break;

	}
	pango_layout_set_width(layout, keys[i][j].width);
	pango_layout_set_alignment(layout, PANGO_ALIGN_CENTER);
	gdk_draw_layout(pixmap, gc, offset_x + keys[i][j].width / 2, i * HEIGHT
			+ HEIGHT / 4, layout);
}

void paint_key_frame(GdkDrawable *drawable, GdkGC *gc, gboolean filled, gint x,
		gint y, gint width, gint height) {
	if (trim_key_frame) {
		x++;
		y++;
		width -= 5;
		height -= 5;
	}

	// less factor for flatter hexagons. use 0.0f for rectangles.
	gint difference = (gint)(hexagon_frame_factor * height);
	GdkPoint points[6];
	GdkPoint point;
	point.x = x;
	point.y = y + difference;
	points[0] = point;

	point.y = y + height - difference;
	points[5] = point;

	point.x = x + width / 2;
	point.y = y - difference;
	points[1] = point;

	point.y = y + height + difference;
	points[4] = point;

	point.x = x + width;
	point.y = y + difference;
	points[2] = point;

	point.y = y + height - difference;
	points[3] = point;

	gdk_draw_polygon(drawable, gc, filled, points, 6);
}

static gint button_press_event(GtkWidget *widget, GdkEventButton *event) {
	int x, y;
	char not_py[2];
	GdkPoint p;
	mouse_pressed = TRUE;
	finish_one();
	if (event->button == 1 && pixmap != NULL) {
		x = event->x;
		y = event->y;
		refresh_cur_hex(x, y);
		refresh_cur_pos(x, y);
		if(y<HEIGHT || y>HEIGHT*4){
			press_pos.x = x;
			press_pos.y = y;
		}

		switch (cur_hex.command) {
		case COM_INPUT:
			switch (input_mode) {
			case INPUT_MODE_CH:
				if (cur_hex.name != '\0') {
					if(cur_hex.name == ',' || cur_hex.name == '.'
						|| cur_hex.name == '\"' || cur_hex.name == ' '){
						if(BUILD_SO){
							//TO DO emit im signal
						}else{
							if(cur_hex.name != '\0'){
								not_py[0] = cur_hex.name;
								not_py[1] = '\0';
								strcat(text_area,not_py);
								gtk_label_set_text(label,text_area);
								get_key(&p, x,y);
								p_tap.x = p.x;
								p_tap.y = p.y;
							}
						}
						break;
					}
					alter_tobe_input(cur_hex.name);
					key_info_index = 0;
					key_info_set[key_info_index].c = cur_hex.name;
					key_info_set[key_info_index].min_distance = get_distance(x,
							y, cur_hex.name);
					key_info_set[key_info_index].count = 1;
					key_info_set[key_info_index].is_valid = 1;
					get_key(&key_info_set[key_info_index].p, x, y);
					add_input(cur_hex.name);
				}
				break;
			case INPUT_MODE_EN_L:
			case INPUT_MODE_EN_U:
			case INPUT_MODE_NUM:
				if(BUILD_SO){
					//TO DO emit im signal
				}else{
					if(cur_hex.name != '\0'){
						not_py[0] = cur_hex.name;
						not_py[1] = '\0';
						strcat(text_area,not_py);
						gtk_label_set_text(label,text_area);
						get_key(&p, x,y);
						p_tap.x = p.x;
						p_tap.y = p.y;
					}
				}
				break;
			}
			break;
		case COM_CH:
			clear_to_lowercase();
			input_mode = INPUT_MODE_CH;
			break;
		case COM_EN:
			if (is_upper) {
				clear_to_uppercase();
				input_mode = INPUT_MODE_EN_U;
			} else {
				clear_to_lowercase();
				input_mode = INPUT_MODE_EN_L;
			}
			break;
		case COM_NUM:
			clear_to_num();
			input_mode = INPUT_MODE_NUM;
			break;
		case COM_UPPER:
			is_upper = !is_upper;
			if (input_mode != INPUT_MODE_EN_L && input_mode != INPUT_MODE_EN_U) {
				break;
			}
			if (is_upper) {
				clear_to_uppercase();
				input_mode = INPUT_MODE_EN_U;
			} else {
				clear_to_lowercase();
				input_mode = INPUT_MODE_EN_L;
			}
			break;

		}

		paint(widget);
		printf("current key:%c\n", cur_hex.name);
	}
	return TRUE;
}

static gint button_release_event(GtkWidget *widget, GdkEventButton *event) {
	int i,x,y,offset_x,inx,wd,hit;
	mouse_pressed = FALSE;

	x = event->x;
	y = event->y;

	p_tap.x = -1;
	p_tap.y = -1;

	switch (input_mode) {
	case INPUT_MODE_CH:
		if(input_index > 0 && cur_hex.name != '\0'){
				copy_input_string();
				input(input_all);
				get_Chinese_word();
				if(phrase_size == 0){
					clear_input_all();
				}
			}
		// im stuff here
		if (abs(x-press_pos.x)+abs(y-press_pos.y)<CLICK_THRESH) {	//build for .so
			offset_x = HZ_WIDTH;
			hit = 0;
			for (i = 0; i < phrase_size; i++) {
				wd = HZ_WIDTH*(strlen(fcitx_result[i])/2+1);
				if((x>offset_x && x<offset_x+wd) && y<HEIGHT){
					inx = i;
					hit = 1;
					break;
				}
				offset_x += wd;
			}
			if(hit == 0){
				if((x>W_WIDTH-HZ_WIDTH*4 && x<W_WIDTH) && y<HEIGHT){
					printf("page down\n");
					next_page();
				}else if((x>W_WIDTH-HZ_WIDTH*8 && x<W_WIDTH-HZ_WIDTH*4) && y<HEIGHT){
					printf("page up\n");
					pre_page();
				}
				paint(widget);
				return;
			}
			commit_hz(inx);

		}else if((x-press_pos.x)>H_CONFIRM_THRESH && y>HEIGHT*4 && press_pos.y>HEIGHT*4){
			commit_hz(0);
		}else if((press_pos.x-x)>H_CONFIRM_THRESH && y>HEIGHT*4 && press_pos.y>HEIGHT*4){
			clear_fcitx_result();
			clear_input_all();
		}
		finish_one();
		clear_to_lowercase();
		break;
	case INPUT_MODE_EN_L:
	case INPUT_MODE_EN_U:
	case INPUT_MODE_NUM:

		break;

	}
	paint(widget);

	return TRUE;
}

static gint motion_notify_event(GtkWidget *widget, GdkEventMotion *event) {
	int x, y;
	char c;
	GdkModifierType state;

	if (event->is_hint) {
		//	  printf("is_hint\n");
		gdk_window_get_pointer(event->window, &x, &y, &state);
	} else {
		x = event->x;
		y = event->y;
		state = event->state;
	}
	refresh_cur_pos(x, y);
	//  printf("state & mask%d\n",state & GDK_BUTTON1_MASK);
	if (state & GDK_BUTTON1_MASK && pixmap != NULL) {
		/*
		 input = recognize_key(x, y, RECOGNIZE_R);
		 if (input != '\0') {
		 add_input(input);
		 }*/

		c = recognize_key(x, y, RECOGNIZE_R);
		if(c!='\0'){
			add_input(c);
		}
		//generate_path(x, y);
		paint(widget);
	}

	return TRUE;
}

int get_distance(int x, int y, char c) {
	GdkPoint p = { -1, -1 };
	get_key(&p, x, y);
	get_key_pos(&p, p.x, p.y);
	p.x += WIDTH >> 1;
	p.y += HEIGHT >> 1;
	return abs(x - p.x) + abs(y - p.y);
}

void generate_path(int x, int y) {
	// x and y are mouse position, create a most probable path of inputs.
	char input, postfix[20];
	int i, j;
	input = recognize_key(x, y, RECOGNIZE_R);
	if (input != '\0') {
		// add input expanded here.
		if (input == key_info_set[key_info_index].c) {
			// same old input character.
			for (i = 0; i <= key_info_index; i++) {
				key_info_set[i].is_valid = 1;
			}
			key_info_set[key_info_index].count++;
			if (key_info_set[key_info_index].min_distance > get_distance(x, y,
					input)) {
				key_info_set[key_info_index].min_distance = get_distance(x, y,
						input);
			}
			key_info_set[key_info_index].is_valid = 1;
			printf("key_info_set[%d]: c:%c; count%d; min_d: %d; is_valid:%d\n",
					key_info_index, key_info_set[key_info_index].c,
					key_info_set[key_info_index].count,
					key_info_set[key_info_index].min_distance,
					key_info_set[key_info_index].is_valid);
		} else if (input == key_info_set[key_info_index - 1].c) {
			// roll back one character
			GdkPoint p, p_surrounding;
			get_key_pos(&p, key_info_set[key_info_index].p.x,
					key_info_set[key_info_index].p.y);
			for (i = 0; i < 7; i++) {
				if (get_surrounding_key(&p_surrounding, i, p.x + WIDTH / 2, p.y
						+ HEIGHT / 2) > 0
						&& keys[p_surrounding.y][p_surrounding.x].mode
								!= UNPRESSED_MODE) {
					keys[p_surrounding.y][p_surrounding.x].mode = WAITING_MODE;
				}
			}
			key_info_index--;
		} else if (input == key_info_set[0].c) {
			// we have come around to the root
			key_info_index = 0;
			for (i = 0; i < COL; i++)
				for (j = 0; j < ROW; j++) {
					if (keys[j][i].mode != UNPRESSED_MODE && keys[j][i].mode
							!= ROOT_MODE) {
						keys[j][i].mode = WAITING_MODE;
					}
				}
		} else {
			// new char comes in
			for (i = 0; i <= key_info_index; i++) {
				key_info_set[i].is_valid = 1;
			}
			key_info_index++;
			if (key_info_index < MAX_KEY_INFO_INDEX) {
				key_info_set[key_info_index].min_distance = get_distance(x, y,
						input);
			}
			key_info_set[key_info_index].c = input;
			key_info_set[key_info_index].count = 1;
			key_info_set[key_info_index].is_valid = 1;
			get_key(&key_info_set[key_info_index].p, x, y);
			printf("key_info_set[%d]: c:%c; count%d; min_d: %d; is_valid:%d\n",
					key_info_index, key_info_set[key_info_index].c,
					key_info_set[key_info_index].count,
					key_info_set[key_info_index].min_distance,
					key_info_set[key_info_index].is_valid);

			for (i = 0; i < COL; i++)
				for (j = 0; j < ROW; j++) {
					if (keys[j][i].mode != UNPRESSED_MODE && keys[j][i].mode
							!= ROOT_MODE) {
						keys[j][i].mode = WAITING_MODE;
					}
				}
		}

		// assume the last character of input is the most significant one.
		// the pinyin tree must end with a leaf of the last input char.
		i = 0;
		input_index = 0;
		while (i <= key_info_index) {
			if (key_info_set[i].is_valid != 0)
				input_chars[input_index++] = key_info_set[i++].c;
			else
				i++;
		}
		input_chars[input_index] = '\0';

		if (findNextChar(input_chars, postfix) != -1) {
			// valid tree
			i = 0;
			input_index = 0;
			while (i <= key_info_index) {
				if (key_info_set[i].is_valid != 0){
					p_record[input_index++] = key_info_set[i++].p;

				}
				else
					i++;
			}
			input_index--;
			refresh_mode();
			input_index++;
			printf("input:%s\n", input_chars);
		} else {
			printf("invalid tree:%s\n", input_chars);
			// not so good
			// try to make a correct tree.
			while (findNextChar(input_chars, postfix) == -1) {
				unsigned min = (unsigned) -1, invalid_index = 0;
				// search one key with max min_distance and mark it as invalid
				for (i = 1; i < key_info_index; i++) {
					if (key_info_set[i].count < min && key_info_set[i].is_valid
							== 1) {
						min = key_info_set[i].count;
						invalid_index = i;
					}
				}

				printf("valid key_info set: ");
				for (i = 0; i <= key_info_index; i++) {
					if (key_info_set[i].is_valid)
						printf("%c", key_info_set[i].c);
				}
				printf("\n");

				if (invalid_index == 0) {
					// this means we are out of chars and the tree is still not valid
					// then we have doubt if the last char matters the most
					for (i = 1; i < key_info_index; i++) {
						key_info_set[i].is_valid = 1;
					}
					key_info_set[key_info_index].is_valid = 0;
					break;
				}

				key_info_set[invalid_index].is_valid = 0;

				// refresh input_chars
				i = 0;
				input_index = 0;
				while (i <= key_info_index) {
					if (key_info_set[i].is_valid != 0)
						input_chars[input_index++] = key_info_set[i++].c;
					else
						i++;
				}
				input_chars[input_index] = '\0';
				printf("input_chars after adjustment:%s\n", input_chars);
				// refresh p_record
				i = 0;
				input_index = 0;
				while (i <= key_info_index) {
					if (key_info_set[i].is_valid != 0)
						p_record[input_index++] = key_info_set[i++].p;
					else
						i++;
				}
				input_index--;
				refresh_mode();
				input_index++;
			}
			printf("input_chars:%s\n", input_chars);
		}
	}
}

static gint configure_event(GtkWidget *widget, GdkEventConfigure *event) {
	if (pixmap)
		gdk_pixmap_unref(pixmap);
	pixmap = gdk_pixmap_new(widget->window, widget->allocation.width,
			widget->allocation.height, -1);
	initialize(widget);
	paint(widget);
	return TRUE;
}

void initialize(GtkWidget *widget) {
	gc_grey = gdk_gc_new(widget->window);
	gc_bright = gdk_gc_new(widget->window);
	gc_line = gdk_gc_new(widget->window);
	gc_yellow = gdk_gc_new(widget->window);
	gc_green = gdk_gc_new(widget->window);

	gc_default = widget->style->black_gc;
	gc_white = widget->style->white_gc;

	gdk_gc_set_rgb_fg_color(gc_yellow, &color_yellow);
	gdk_gc_set_rgb_fg_color(gc_grey, &color_def);
	gdk_gc_set_rgb_fg_color(gc_bright, &color_bright);
	gdk_gc_set_rgb_fg_color(gc_line, &color_line);
	gdk_gc_set_rgb_fg_color(gc_green, &color_green);
	gdk_gc_set_line_attributes(gc_line, line_width, line_style, cap_style,
			join_style);

	loadTree();

}

void refresh_cur_pos(int x, int y) {
	cur_pos.x = x;
	cur_pos.y = y;
}

void add_input(char c) {
	int record_index,i,j;
	GdkPoint p = { -1, -1 };
	GdkPoint p2 = { -1, -1};
	GdkPoint p3 = { -1, -1};
	if (c == last_input) {
		return;
	}
	last_input = c;
	get_key(&p, cur_pos.x, cur_pos.y);

	record_index = get_record_inx(p.x,p.y);
	if(record_index >= 0){
		for(i=record_index+1;i<input_index+1;i++){
			GdkPoint last_p = p_record[i];
			get_key_pos(&p2, last_p.x, last_p.y);
			for (j = 0; j < 7; j++) {
				if (get_surrounding_key(&p3, j, p2.x + WIDTH / 2, p2.y)
						&& keys[p3.y][p3.x].mode != UNPRESSED_MODE) {
					keys[p3.y][p3.x].mode = WAITING_MODE;
				}
			}
		}
		input_index = record_index;
		input_chars[input_index + 1] = '\0';
		refresh_mode();
		input_index++;
	}else{
		input_index = input_index >= MAX_PY_INPUT-1 ? 0 : input_index;

		input_chars[input_index] = c;

		printf("%d %d %d\n",cur_pos.x,cur_pos.y,get_key(&p, cur_pos.x, cur_pos.y));
		if (input_index == 0) {
			keys[p.y][p.x].mode = ROOT_MODE;
		}

		printf("add_input: p.x:%d ,p.y:%d ,index:%d\n", p.x, p.y, input_index);
		p_record[input_index].x = p.x;
		p_record[input_index].y = p.y;
		input_chars[input_index + 1] = '\0';
		refresh_mode();
		input_index++;

		printf("input:%c\n", c);
	}
}

void refresh_mode() {
	GdkPoint p = { -1, -1 };
	GdkPoint p2 = { -1, -1 };
	char postfix[10];
	int i, j;
	if (input_index > 0) {
		GdkPoint last_p = p_record[input_index - 1];
		//		printf("last p row: %d, col: %d\n",last_p.y,last_p.x);
		get_key_pos(&p2, last_p.x, last_p.y);
		for (i = 0; i < 7; i++) {
			if (get_surrounding_key(&p, i, p2.x + WIDTH / 2, p2.y) > 0
					&& keys[p.y][p.x].mode != UNPRESSED_MODE) {
				//				printf("row: %d, col: %d\n",p.y,p.x);
				keys[p.y][p.x].mode = WAITING_MODE;
			}
		}
	}

	findNextChar(input_chars, postfix);
	//	for(i=0;i<strlen(postfix);i++){
	//		printf("%c ",postfix[i]);
	//	}

	GdkPoint cur_p = p_record[input_index];
	get_key_pos(&p2, cur_p.x, cur_p.y);
	for (i = 1; i < 7; i++) {
		get_surrounding_key(&p, i, p2.x + WIDTH / 2, p2.y);
		for (j = 0; j < strlen(postfix); j++) {
			if (keys[p.y][p.x].tobe_input == postfix[j] && keys[p.y][p.x].mode
					!= UNPRESSED_MODE) {
				keys[p.y][p.x].mode = PRESSED_MODE;
			}
		}
	}
}

void get_Chinese_word(void) {
	//go to the back end and fetch Chinese words according to pinyin
	int i, j ,h;
	clear_fcitx_result();
	for (i = 0; i < MAX_FCITX; i++) {
		h = i % ONE_PAGE_WORD;
		if (h == 0 && i != 0) {
			pageDown();
		}
		getWord(h, fcitx_result[i]);
		if(fcitx_result[i][0] == '\0'){
			break;
		}
		//strcpy(fcitx_result[i][0],result_word);
		//fcitx_result[i][3] = '\0';
		//		printf("%s\n",fcitx_result[i]);
	}
	phrase_size = i;
}

void destroy(GtkWidget *widget, GdkEvent *event, gpointer data) {
	gtk_main_quit();

}

//when the mouse is pressed, figure out which key is down
void refresh_cur_hex(gint x, gint y) {
	GdkPoint p = { -1, -1 };
	if (get_key(&p, x, y) > 0) {
		cur_hex.command = keys[p.y][p.x].command;
		cur_hex.name = keys[p.y][p.x].tobe_input;
		//convert from array position to center position
		//cur_hex always denotes the current pressed key,
		//x and y are always the center of the key.

		cur_hex.x = (p.x + 0.5) * WIDTH + row_indent[p.y];
		cur_hex.y = (p.y + 0.5) * HEIGHT;

		return;
	}
	cur_hex.name = '\0';
}

//get array position of a key from any mouse position
//if no key is picked up, return -1
int get_key(GdkPoint* p, int x, int y) {
	int h = y / HEIGHT;
	int i, cx, cy, H;
	if (h < 0 || h > 4) {
		return '\0';
	}
	if (h > 0) {
		h--;
	}
	H = h + 3;
	if (H > 5)
		H = 5;
	for (; h < H; h++) {
		int offset = row_indent[h];
		for (i = 0; i < COL; i++) {
			if (x < offset + keys[h][i].width && x > offset) {
				cx = offset + (keys[h][i].width >> 1);
				cy = h * HEIGHT + (HEIGHT >> 1);
				gfloat slope = (HEIGHT * 4 * hexagon_frame_factor) / WIDTH;
				gfloat top = (0.5 + hexagon_frame_factor) * HEIGHT;
				if (slope * abs(x - cx) + abs(y - cy) <= top) {
					p->x = i;
					p->y = h;
					return 1;
				}
			}
			offset += keys[h][i].width;
		}
	}
	return -1;
}

char recognize_key(int x, int y, int r) {
	int h = y / HEIGHT;
	int i, cx, cy, H;
	GdkPoint p;
	get_key(&p, x, y);
	if (h < 0 || h > 4) {
		return '\0';
	}
	if (h > 0) {
		h--;
	}
	H = h + 3;
	if (H > 5)
		H = 5;
	for (; h < H; h++) {
		int offset = row_indent[h];
		for (i = 0; i < COL; i++) {
			if (x < offset + keys[h][i].width && x > offset) {
				cx = offset + (keys[h][i].width >> 1);
				cy = h * HEIGHT + (HEIGHT >> 1);
				gfloat slope = (HEIGHT * 4 * hexagon_frame_factor) / WIDTH;
				gfloat top = (0.5 + hexagon_frame_factor) * HEIGHT;
				if (slope * abs(x - cx) + abs(y - cy) < top + SENSITIVITY) {
					//if (sqrt((x - cx) * (x - cx) - (y - cy) * (y - cy)) < r) {
					if (keys[h][i].mode == PRESSED_MODE
							|| get_record_inx(p.x,p.y) >=0) {
						return keys[h][i].tobe_input;
					}/*else {
						for (k=0;k<input_index;i++){
							if (p_record[k] == keys[h][i].tobe_input){
								return keys[h][i].tobe_input
							}
						}
					}*/
				}
			}
			offset += keys[h][i].width;
		}
	}
	return '\0';
}

//return the position of left-up corner
int get_key_pos(GdkPoint* p, int x, int y) {
	int px = x * WIDTH + row_indent[y];
	int py = y * HEIGHT;
	p->x = px;
	p->y = py;
	return 1;
	/*if (get_key(p, px, py) > 0) {
	 p->x = px;
	 p->y = py;
	 return 1;
	 }
	 return -1;*/
}

void alter_tobe_input(char c) {
	int x0 = 0;
	int x1 = 1;
	int x2 = 0;
	int x3 = 1;
	int x4 = 1;

	switch (c) {
	case 'a': {
		change_kb(1, x1 + 1, 'i');
		change_kb(2, x2 + 2, 'n');
		change_kb(2, x2 + 3, 'g');
		change_kb(3, x3 + 1, 'o');
		break;
	}
	case 'b': {
		change_kb(1, x1 + 4, 'o');
		change_kb(1, x1 + 5, 'e');
		change_kb(2, x2 + 3, 'g');
		change_kb(2, x2 + 4, 'n');
		change_kb(2, x2 + 5, 'a');
		change_kb(2, x2 + 6, 'i');
		change_kb(2, x2 + 7, 'n');
		change_kb(2, x2 + 8, 'g');
		change_kb(3, x3 + 3, 'i');
		change_kb(3, x3 + 4, 'e');
		change_kb(3, x3 + 6, 'o');
		change_kb(4, x4 + 5, 'u');
		break;
	}
	case 'c': {
		change_kb(1, x1 + 1, 'g');
		change_kb(1, x1 + 2, 'n');
		change_kb(1, x1 + 3, 'i');
		change_kb(1, x1 + 4, 'o');
		change_kb(1, x1 + 5, 'n');
		change_kb(1, x1 + 6, 'g');
		change_kb(2, x2 + 2, 'i');
		change_kb(2, x2 + 3, 'e');
		change_kb(2, x2 + 4, 'h');
		change_kb(2, x2 + 5, 'u');
		change_kb(2, x2 + 6, 'i');
		change_kb(3, x3 + 1, 'a');
		change_kb(3, x3 + 2, 'u');
		change_kb(3, x3 + 4, 'a');
		change_kb(3, x3 + 5, 'n');
		change_kb(3, x3 + 6, 'g');
		change_kb(4, x4, 'g');
		change_kb(4, x4 + 1, 'n');
		change_kb(4, x4 + 2, 'o');
		change_kb(4, x4 + 3, 'i');
		change_kb(4, x4 + 4, 'o');
		break;
	}
	case 'd': {
		change_kb(1, x1, 'g');
		change_kb(1, x1 + 1, 'n');
		change_kb(1, x1 + 2, 'o');
		change_kb(1, x1 + 3, 'a');
		change_kb(1, x1 + 4, 'n');
		change_kb(1, x1 + 5, 'g');
		change_kb(2, x2 + 1, 'a');
		change_kb(2, x2 + 2, 'u');
		change_kb(2, x2 + 4, 'i');
		change_kb(2, x2 + 5, 'u');
		change_kb(3, x3 + 1, 'i');
		change_kb(3, x3 + 3, 'e');
		change_kb(3, x3 + 4, 'n');
		change_kb(3, x3 + 5, 'g');
		break;
	}
	case 'e': {
		change_kb(1, x1 + 3, 'r');
		change_kb(2, x2 + 2, 'i');
		change_kb(2, x2 + 3, 'n');
		break;
	}
	case 'f': {
		change_kb(1, x1 + 1, 'g');
		change_kb(1, x1 + 2, 'n');
		change_kb(1, x1 + 3, 'a');
		change_kb(1, x1 + 4, 'o');
		change_kb(2, x2 + 2, 'i');
		change_kb(2, x2 + 3, 'e');
		change_kb(2, x2 + 5, 'u');
		break;
	}
	case 'g': {
		change_kb(0, x0 + 4, 'n');
		change_kb(1, x1 + 3, 'i');
		change_kb(1, x1 + 4, 'u');
		change_kb(1, x1 + 5, 'o');
		change_kb(1, x1 + 6, 'n');
		change_kb(1, x1 + 7, 'g');
		change_kb(2, x2 + 2, 'g');
		change_kb(2, x2 + 3, 'n');
		change_kb(2, x2 + 4, 'a');
		change_kb(2, x2 + 6, 'e');
		change_kb(2, x2 + 7, 'i');
		change_kb(3, x3 + 3, 'o');
		break;
	}
	case 'h': {
		change_kb(1, x1 + 5, 'e');
		change_kb(1, x1 + 6, 'i');
		change_kb(2, x2 + 4, 'g');
		change_kb(2, x2 + 5, 'n');
		change_kb(2, x2 + 7, 'a');
		change_kb(2, x2 + 8, 'o');
		change_kb(3, x3 + 5, 'o');
		change_kb(3, x3 + 6, 'u');
		change_kb(3, x3 + 7, 'n');
		change_kb(3, x3 + 8, 'g');
		change_kb(4, x4 + 5, 'i');
		break;
	}
	case 'j': {
		change_kb(0, x0 + 6, 'n');
		change_kb(0, x0 + 7, 'e');
		change_kb(0, x0 + 8, 'o');
		change_kb(1, x1 + 5, 'a');
		change_kb(1, x1 + 6, 'u');
		change_kb(1, x1 + 7, 'i');
		change_kb(1, x1 + 8, 'n');
		change_kb(1, x1 + 9, 'g');
		change_kb(2, x2 + 8, 'a');
		change_kb(2, x2 + 9, 'o');
		break;
	}
	case 'k': {
		change_kb(1, x1 + 6, 'i');
		change_kb(1, x1 + 7, 'a');
		change_kb(1, x1 + 8, 'o');
		change_kb(1, x1 + 9, 'n');
		change_kb(1, x1 + 10, 'g');
		change_kb(2, x2 + 6, 'g');
		change_kb(2, x2 + 7, 'n');
		change_kb(2, x2 + 9, 'u');
		change_kb(2, x2 + 10, 'a');
		change_kb(3, x3 + 7, 'e');
		change_kb(3, x3 + 9, 'i');
		break;
	}
	case 'l': {
		change_kb(0, x0 + 9, 'e');
		change_kb(0, x0 + 10, 'a');
		change_kb(1, x1 + 6, 'g');
		change_kb(1, x1 + 7, 'n');
		change_kb(1, x1 + 8, 'o');
		change_kb(1, x1 + 9, 'u');
		change_kb(1, x1 + 10, 'n');
		change_kb(2, x2 + 8, 'a');
		change_kb(2, x2 + 10, 'v');
		change_kb(3, x3 + 7, 'u');
		change_kb(3, x3 + 8, 'i');
		change_kb(3, x3 + 9, 'e');
		change_kb(4, x4 + 7, 'g');
		change_kb(4, x4 + 8, 'n');
		break;
	}
	case 'm': {
		change_kb(1, x1 + 8, 'u');
		change_kb(2, x2 + 5, 'g');
		change_kb(2, x2 + 6, 'n');
		change_kb(2, x2 + 7, 'a');
		change_kb(2, x2 + 8, 'i');
		change_kb(2, x2 + 9, 'n');
		change_kb(3, x3 + 6, 'o');
		change_kb(3, x3 + 8, 'e');
		change_kb(3, x3 + 9, 'g');
		change_kb(4, x4 + 6, 'u');
		break;
	}
	case 'n': {
		change_kb(1, x1 + 6, 'o');
		change_kb(1, x1 + 7, 'u');
		change_kb(2, x2 + 4, 'g');
		change_kb(2, x2 + 5, 'n');
		change_kb(2, x2 + 6, 'a');
		change_kb(2, x2 + 7, 'i');
		change_kb(2, x2 + 8, 'n');
		change_kb(2, x2 + 9, 'g');
		change_kb(3, x3 + 4, 'e');
		change_kb(3, x3 + 5, 'u');
		change_kb(3, x3 + 7, 'e');
		change_kb(4, x4 + 3, 'g');
		change_kb(4, x4 + 4, 'n');
		change_kb(4, x4 + 5, 'o');
		change_kb(4, x4 + 6, 'v');
		break;
	}
	case 'o': {
		change_kb(1, x1 + 7, 'u');
		break;
	}
	case 'p': {
		change_kb(0, x0 + 8, 'n');
		change_kb(0, x0 + 9, 'a');
		change_kb(0, x0 + 10, 'o');
		change_kb(1, x1 + 7, 'g');
		change_kb(1, x1 + 8, 'i');
		change_kb(1, x1 + 10, 'u');
		change_kb(2, x2 + 8, 'n');
		change_kb(2, x2 + 9, 'e');
		break;
	}
	case 'q': {
		change_kb(0, x0 + 1, 'e');
		change_kb(0, x0 + 2, 'a');
		change_kb(1, x1 + 1, 'i');
		change_kb(1, x1 + 2, 'n');
		change_kb(1, x1 + 3, 'g');
		change_kb(2, x2, 'n');
		change_kb(2, x2 + 1, 'u');
		change_kb(2, x2 + 2, 'o');
		change_kb(3, x3, 'a');
		change_kb(3, x3 + 1, 'e');
		break;
	}
	case 'r': {
		change_kb(0, x0 + 4, 'i');
		change_kb(1, x1 + 2, 'e');
		change_kb(1, x1 + 4, 'u');
		change_kb(1, x1 + 5, 'a');
		change_kb(2, x2 + 1, 'g');
		change_kb(2, x2 + 2, 'n');
		change_kb(2, x2 + 3, 'a');
		change_kb(2, x2 + 4, 'o');
		change_kb(2, x2 + 5, 'n');
		change_kb(2, x2 + 6, 'g');
		break;
	}
	case 's': {
		change_kb(1, x1, 'o');
		change_kb(1, x1 + 1, 'i');
		change_kb(1, x1 + 2, 'o');
		change_kb(1, x1 + 3, 'n');
		change_kb(1, x1 + 4, 'g');
		change_kb(2, x2, 'g');
		change_kb(2, x2 + 1, 'a');
		change_kb(2, x2 + 3, 'u');
		change_kb(2, x2 + 4, 'i');
		change_kb(3, x3, 'n');
		change_kb(3, x3 + 1, 'e');
		change_kb(3, x3 + 2, 'h');
		change_kb(3, x3 + 3, 'a');
		change_kb(3, x3 + 4, 'n');
		change_kb(3, x3 + 5, 'g');
		change_kb(4, x4 + 1, 'i');
		change_kb(4, x4 + 2, 'o');
		change_kb(4, x4 + 3, 'u');
		break;
	}
	case 't': {
		change_kb(0, x0 + 4, 'e');
		change_kb(0, x0 + 5, 'n');
		change_kb(0, x0 + 6, 'g');
		change_kb(1, x1 + 2, 'o');
		change_kb(1, x1 + 3, 'a');
		change_kb(1, x1 + 5, 'o');
		change_kb(2, x2 + 2, 'g');
		change_kb(2, x2 + 3, 'n');
		change_kb(2, x2 + 4, 'i');
		change_kb(2, x2 + 5, 'u');
		change_kb(2, x2 + 6, 'a');
		change_kb(3, x3 + 3, 'e');
		change_kb(3, x3 + 5, 'n');
		break;
	}
	case 'w': {
		change_kb(1, x1, 'o');
		change_kb(1, x1 + 2, 'e');
		change_kb(1, x1 + 3, 'i');
		change_kb(2, x2 + 1, 'u');
		change_kb(2, x2 + 2, 'a');
		change_kb(2, x2 + 3, 'n');
		change_kb(2, x2 + 4, 'g');
		change_kb(3, x3 + 1, 'i');
		break;
	}
	case 'x': {
		change_kb(1, x1 + 1, 'e');
		change_kb(1, x1 + 2, 'n');
		change_kb(1, x1 + 3, 'g');
		change_kb(2, x2 + 1, 'a');
		change_kb(2, x2 + 2, 'i');
		change_kb(2, x2 + 3, 'u');
		change_kb(2, x2 + 4, 'a');
		change_kb(2, x2 + 5, 'n');
		change_kb(3, x3 - 1, 'g');
		change_kb(3, x3, 'n');
		change_kb(3, x3 + 1, 'o');
		change_kb(3, x3 + 3, 'e');
		break;
	}
	case 'y': {
		change_kb(0, x0 + 6, 'e');
		change_kb(1, x1 + 4, 'i');
		change_kb(1, x1 + 6, 'u');
		change_kb(1, x1 + 7, 'a');
		change_kb(2, x2 + 3, 'g');
		change_kb(2, x2 + 4, 'n');
		change_kb(2, x2 + 5, 'a');
		change_kb(2, x2 + 6, 'o');
		change_kb(2, x2 + 7, 'n');
		change_kb(2, x2 + 8, 'g');
		break;
	}
	case 'z': {
		change_kb(1, x1, 'a');
		change_kb(1, x1 + 1, 'i');
		change_kb(1, x1 + 2, 'a');
		change_kb(1, x1 + 3, 'n');
		change_kb(1, x1 + 4, 'g');
		change_kb(2, x2, 'n');
		change_kb(2, x2 + 1, 'u');
		change_kb(2, x2 + 2, 'h');
		change_kb(2, x2 + 3, 'o');
		change_kb(2, x2 + 4, 'u');
		change_kb(3, x3 - 1, 'g');
		change_kb(3, x3, 'o');
		change_kb(3, x3 + 2, 'e');
		change_kb(3, x3 + 3, 'n');
		change_kb(3, x3 + 4, 'g');
		change_kb(4, x4 - 1, 'n');
		change_kb(4, x4, 'a');
		change_kb(4, x4 + 1, 'i');
		break;
	}

	}

}

void change_kb(int row, int col, char c) {
	keys[row][col].tobe_input = c;
	keys[row][col].mode = WAITING_MODE;
}

int get_surrounding_key(GdkPoint* p, int direction, int cx, int cy) {
	if (direction < 0 || direction > 6) {
		return -1;
	}
	switch (direction) {
	case 0:
		return get_key(p, cx, cy);
	case 1:
		return get_key(p, cx - WIDTH / 2, cy - HEIGHT);
	case 2:
		return get_key(p, cx + WIDTH / 2, cy - HEIGHT);
	case 3:
		return get_key(p, cx + WIDTH, cy);
	case 4:
		return get_key(p, cx + WIDTH / 2, cy + HEIGHT);
	case 5:
		return get_key(p, cx - WIDTH / 2, cy + HEIGHT);
	case 6:
		return get_key(p, cx - WIDTH, cy);
	}
	return -1;
}

void clear_fcitx_result(void) {
	int i;
	for (i = 0; i < MAX_FCITX; i++) {
		fcitx_result[i][0] = '\0';
		phrase_size = 0;
	}
}

void clear_to_lowercase(void) {
	int i, j;
	for (i = 0; i < ROW; i++) {
		for (j = 0; j < COL; j++) {
			keys[i][j].tobe_input = keys[i][j].input_lowercase;
			keys[i][j].mode = UNPRESSED_MODE;
		}
	}
}

void clear_to_uppercase(void) {
	int i, j;
	for (i = 0; i < ROW; i++) {
		for (j = 0; j < COL; j++) {
			keys[i][j].tobe_input = keys[i][j].input_uppercase;
			keys[i][j].mode = UNPRESSED_MODE;
		}
	}
}

void clear_to_num(void) {
	int i, j;
	for (i = 0; i < ROW; i++) {
		for (j = 0; j < COL; j++) {
			keys[i][j].tobe_input = keys[i][j].input_num;
			keys[i][j].mode = UNPRESSED_MODE;
		}
	}
}

void finish_one(void) {
	cur_hex.name = '\0';
	last_input = '\0';
	input_index = 0;
	input_chars[0] = '\0';
}

void copy_input_string(void) {
	int i=0;
	while(input_chars[i] != '\0'){
		input_all[input_all_inx++] = input_chars[i++];
	}
	input_all[input_all_inx] = '\0';
}

void clear_input_all(void) {
	int i;
	for(i=0;i<input_all_inx;i++){
		input_all[i] = '\0';
	}
	input_all_inx = 0;
}

void next_page(void) {
	if(phrase_size == 0){
		return;
	}
	if(phrase_size<MAX_FCITX){
		return;
	}
	pageDown();
	get_Chinese_word();
}
void pre_page(void) {
	int i;
	if(phrase_size == 0){
		return;
	}
	int n = MAX_FCITX/ONE_PAGE_WORD;
	for(i=0;i<n;i++){
		pageUp();
	}
	get_Chinese_word();
}

void commit_hz(int inx){
	if(BUILD_SO){
		g_signal_emit_by_name(Aeviou_context, "commit", fcitx_result[inx]);
	}else{
		strcat(text_area,fcitx_result[inx]);
		gtk_label_set_text(label,text_area);
	}
	clear_fcitx_result();
	clear_input_all();
	press_pos.x = -1;
	press_pos.y = -1;
	phrase_size = 0;
}

int get_record_inx(int x, int y){
	int i;
	for(i=0;i<input_index;i++){
		if(p_record[i].x == x && p_record[i].y == y){
			return i;
		}
	}
	return -1;
}
