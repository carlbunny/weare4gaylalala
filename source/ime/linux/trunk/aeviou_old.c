//#include <gtk/gtk.h>
//#include <math.h>
//#include "keyboard.h"
//#include "fcitx.h"
//#include "pyds.h"
//
//#define WAIT_LEN 6
//#define HEX_R 30
//#define W_WIDTH 800
//#define W_HEIGHT 300
//
//static gint
//expose_event (GtkWidget *widget, GdkEventExpose *event);
//static gint
//button_press_event (GtkWidget *widget, GdkEventButton *event);
//static gint
//button_release_event (GtkWidget *widget, GdkEventButton *event);
//static gint
//motion_notify_event (GtkWidget *widget, GdkEventMotion *event);
//static gint
//configure_event (GtkWidget *widget, GdkEventConfigure *event);
//void destroy( GtkWidget *widget,GdkEvent *event, gpointer   data );
//
//void add_input(char c,int x, int y);
//void paint (GtkWidget *widget);
//void refresh_cur_hex(gint x,gint y);
//void get_hex(GdkPoint* points, gint direction);
//int get_key(GdkPoint* p,int x, int y);
//int get_key2(GdkPoint* p,int x, int y);
//char recognize_key(int x,int y,int r);
//int get_key_pos(GdkPoint* p,int x, int y);
//int get_key_pos2(GdkPoint* p,int x, int y);
//int get_right_width(int index_x,int index_y);
//int get_pos_bykey(GdkPoint* p,const char* key);
//void get_center(GdkPoint* p,gint index);
//void change_kb(int row, int col, char c);
//void alter_input2(char c);
//void clear_input2(void);
//void get_fcitx_word(void);
//void clear_fcitx_result(void);
//int get_surrounding_key(GdkPoint* p,int d, int x, int y);
//void refresh_mode(int cx,int cy);
//
//GtkWidget *window;
//GtkWidget *keyboard;
//PangoFontDescription *font_desc;
//GdkPixmap *pixmap = NULL;
//GdkRectangle update_rect={0,0,W_WIDTH,W_HEIGHT};
//tree *pytree;
//int pytree_size;
//char *wait_pylist;
//gint mouse_pressed = FALSE;
//gint input_method=1;
//char fcitx_result[MAX_FCITX][20]={{'\0'}};
//char last_input ='\0';
//char* wait_list[WAIT_LEN]={"e","u","h   g","i   n","o","a"};
//GdkPoint p_record[MAX_PY_INPUT]={{-1,-1}};
//char input_chars[MAX_PY_INPUT]={'\0'};
//int input_index = 0;
//gint next_valid[WAIT_LEN]={TRUE};
//struct hex_key cur_hex={'\0',0,0};
//
//int main( int argc, char *argv[])
//{
//	gtk_init(&argc, &argv);
//	pytree = (tree*)malloc(sizeof(tree));
//	init_tree(&pytree);
//   window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
////   gtk_window_set_decorated(GTK_WINDOW(window),FALSE);
//   g_signal_connect (G_OBJECT (window), "destroy",
//   		      G_CALLBACK (destroy), NULL);
//
//   keyboard = gtk_drawing_area_new();
//   font_desc = pango_font_description_from_string("Song");
//	pango_font_description_set_size (font_desc, 15 * PANGO_SCALE);
//	gtk_widget_modify_font (keyboard, font_desc);
//
//	gtk_drawing_area_size(keyboard,W_WIDTH,W_HEIGHT);
//   g_signal_connect (G_OBJECT (keyboard), "expose_event",
//                        G_CALLBACK (expose_event), NULL);
//   gtk_signal_connect (GTK_OBJECT(keyboard),"configure_event",
// 		      (GtkSignalFunc) configure_event, NULL);
//   gtk_signal_connect (GTK_OBJECT (keyboard), "motion_notify_event",
// 		      (GtkSignalFunc) motion_notify_event, NULL);
//   gtk_signal_connect (GTK_OBJECT (keyboard), "button_press_event",
// 		      (GtkSignalFunc) button_press_event, NULL);
//   gtk_signal_connect (GTK_OBJECT (keyboard), "button_release_event",
//    		      (GtkSignalFunc) button_release_event, NULL);
//
//
//
//   gtk_widget_set_events (keyboard, GDK_EXPOSURE_MASK
// 			 | GDK_LEAVE_NOTIFY_MASK
// 			 | GDK_BUTTON_PRESS_MASK
// 			 | GDK_BUTTON_RELEASE_MASK
// 			 | GDK_POINTER_MOTION_MASK
// 			 | GDK_POINTER_MOTION_HINT_MASK);
//  gtk_container_add(GTK_CONTAINER(window),keyboard);
//  gtk_widget_show(keyboard);
//  gtk_widget_show(window);
//
//  gtk_main();
//
//  //	pango_font_description_free (font_desc);
//
//  return 0;
//}
//
//static gint
//expose_event (GtkWidget *widget, GdkEventExpose *event)
//{
//
//  gdk_draw_pixmap(widget->window,
//		  widget->style->fg_gc[GTK_WIDGET_STATE (widget)],
//		  pixmap,
//		  event->area.x, event->area.y,
//		  event->area.x, event->area.y,
//		  event->area.width, event->area.height);
//
////	paint(widget);
//  return TRUE;
//}
//
//void paint (GtkWidget *widget)
//{
//	int i,j,offset_x;
//	GdkGC *gc_grey = gdk_gc_new(widget->window);
//	GdkGC *gc_bright = gdk_gc_new(widget->window);
//	GdkColor color = {0,30000,30000,30000};
//	GdkColor color_bright={0,16000,20000,30000};
//	gdk_gc_set_rgb_fg_color(gc_grey,&color);
//	gdk_gc_set_rgb_fg_color(gc_bright,&color_bright);
//	GdkGC *gc_default = widget->style->black_gc;
//	GdkGC *gc_white = widget->style->white_gc;
//	GdkGC *gc = gc_default;
//	gdk_draw_rectangle (pixmap,gc_white,TRUE,0,0,W_WIDTH,W_HEIGHT);
//
//	PangoLayout *layout = gtk_widget_create_pango_layout(widget,input_chars);
//	gdk_draw_layout(pixmap,gc,0,4.5*HEIGHT,layout);
//	for(i=0;i<ROW;i++){
//		offset_x = row_indent[i];
//		for(j=0;j<COL;j++){
//			if(keys[i][j].valid){
//				if(keys[i][j].mode == UNPRESSED_MODE){
//					gc = gc_default;
//				}else if(keys[i][j].mode == PRESSED_MODE){
//					gdk_draw_rectangle(pixmap,gc_default,TRUE,offset_x,i*HEIGHT,keys[i][j].width,HEIGHT);
//					gc = gc_white;
//					gdk_draw_rectangle(pixmap,gc,FALSE,offset_x,i*HEIGHT,keys[i][j].width,HEIGHT);
//				}else{
//					gdk_draw_rectangle(pixmap,gc_grey,TRUE,offset_x,i*HEIGHT,keys[i][j].width,HEIGHT);
//					gc = gc_white;
//					gdk_draw_rectangle(pixmap,gc,FALSE,offset_x,i*HEIGHT,keys[i][j].width,HEIGHT);
//				}
//				if(i>=1 && i<=3){
//				gdk_draw_rectangle(pixmap,gc,FALSE,offset_x,i*HEIGHT,keys[i][j].width,HEIGHT);
//				}
//				layout = gtk_widget_create_pango_layout(widget,&keys[i][j].input2);
//				gdk_draw_layout(pixmap,gc,offset_x+keys[i][j].width*5/12,i*HEIGHT+HEIGHT/3,layout);
//				offset_x+=keys[i][j].width;
//			}
//		}
//	}
//	for(i=0;i<MAX_FCITX;i++){
//		layout = gtk_widget_create_pango_layout(widget,fcitx_result[i]);
//		gdk_draw_layout(pixmap,gc,i*HZ_WIDTH,HZ_OFFSET_Y,layout);
//	}
//
//	GdkPoint p={-1,-1};
//	GdkPoint p2={-1,-1};
//	if(cur_hex.name!='\0'){
////		GdkPoint ps[WAIT_LEN];
//		int i;
//		for(i=0;i<input_index;i++){
//			get_key_pos(&p,p_record[i].x,p_record[i].y);
//			gdk_draw_rectangle(pixmap,gc_bright,TRUE,p.x,p.y,WIDTH,HEIGHT);
//			layout = gtk_widget_create_pango_layout(widget,&keys[p_record[i].y][p_record[i].x].input2);
//			gdk_draw_layout(pixmap,gc_white,p.x+WIDTH*5/12,p.y+HEIGHT/3,layout);
//		}
//		for(i=0;i<input_index-1;i++){
//			get_key_pos(&p,p_record[i].x,p_record[i].y);
//			get_key_pos(&p2,p_record[i+1].x,p_record[i+1].y);
//			gdk_draw_line(pixmap,gc_default,p.x+WIDTH/2,p.y+HEIGHT/2,p2.x+WIDTH/2,p2.y+HEIGHT/2);
//		}
//	}
//	gtk_widget_draw (keyboard, &update_rect);
//
//}
//
//static gint
//button_press_event (GtkWidget *widget, GdkEventButton *event)
//{
//	mouse_pressed = TRUE;
//	if (event->button == 1 && pixmap != NULL){
//		refresh_cur_hex(event->x,event->y);
//		if(cur_hex.name!='\0'){
//			alter_input2(cur_hex.name);
//			if(cur_hex.name!=last_input){
//				add_input(cur_hex.name,cur_hex.x,cur_hex.y);
//				last_input = cur_hex.name;
//			}
//		}
//		paint(keyboard);
//		printf("current key:%c\n",cur_hex.name);
//	}
//  return TRUE;
//}
//
//static gint
//button_release_event (GtkWidget *widget, GdkEventButton *event)
//{
//	mouse_pressed = FALSE;
//	reset();
//	input(input_chars);
//	get_fcitx_word();
//	cur_hex.name='\0';
//	last_input = '\0';
//	input_index = 0;
//	input_chars[0]='\0';
//	clear_input2();
//	reset_tree(pytree);
////	clear_fcitx_result();
//	paint(widget);
//	return TRUE;
//}
//
//static gint
//motion_notify_event (GtkWidget *widget, GdkEventMotion *event)
//{
//	char input;
//  int x, y;
//  GdkModifierType state;
//
//  if (event->is_hint){
////	  printf("is_hint\n");
//    gdk_window_get_pointer (event->window, &x, &y, &state);
//  }
//  else
//    {
//      x = event->x;
//      y = event->y;
//      state = event->state;
//    }
//
////  printf("state & mask%d\n",state & GDK_BUTTON1_MASK);
//  if (state & GDK_BUTTON1_MASK && pixmap != NULL){
//	  input = recognize_key(x,y,RECOGNIZE_R);
//	  if(input!='\0'){
//		  GdkPoint p={-1,-1};
////		  get_key(&p,x,y);
////		  get_key_pos(&p,p.x,p.y);
//		  add_input(input,x,y);
//	  }
////	  cur_hex.x=x;
////	  cur_hex.y=y;
//	  paint(widget);
//  }
////    draw_brush (widget, x, y);
//
//  return TRUE;
//}
//
//static gint
//configure_event (GtkWidget *widget, GdkEventConfigure *event)
//{
//  if (pixmap)
//    gdk_pixmap_unref(pixmap);
//
//  printf("new pixmap\n");
//  pixmap = gdk_pixmap_new(widget->window,
//			  widget->allocation.width,
//			  widget->allocation.height,
//			  -1);
//  paint(keyboard);
//  return TRUE;
//}
//
//void add_input(char c,int cx,int cy){
//	GdkPoint p={-1,-1};
//	if(input_index >= MAX_PY_INPUT){
//		 input_index = 0;
//	}
//	input_chars[input_index] = c;
//	get_key(&p,cx,cy);
//	refresh_mode(cx,cy);
//	p_record[input_index].x=p.x;
//	p_record[input_index].y=p.y;
//	input_index++;
//	input_chars[input_index]='\0';
//	printf("input:%c\n",c);
//}
//
//void refresh_mode(int cx,int cy){
//	GdkPoint p={-1,-1};
//	GdkPoint p2={-1,-1};
//		char c;
//		int i,j;
//	if(input_index>0){
//		GdkPoint last_p = p_record[input_index-1];
////		printf("last p row: %d, col: %d\n",last_p.y,last_p.x);
//		get_key_pos(&p2,last_p.x,last_p.y);
//		for(i=0;i<7;i++){
//			if(get_surrounding_key(&p,i,p2.x+WIDTH/2,p2.y)>0 && keys[p.y][p.x].mode!=UNPRESSED_MODE){
////				printf("row: %d, col: %d\n",p.y,p.x);
//				keys[p.y][p.x].mode = WAITING_MODE;
//			}
//		}
//	}
//
////	printf("pytree\n");
//		c=input_chars[input_index];
////		printf("%c\n",c);
//		forward(pytree,c);
//	get_list(pytree, &wait_pylist, &pytree_size);
////	printf("pytree_size: %d\n",pytree_size);
//	for(i=1;i<7;i++){
//		get_surrounding_key(&p,i,cx,cy);
//		for(j=0;j<pytree_size;j++){
//			if(keys[p.y][p.x].input2 == wait_pylist[j] && keys[p.y][p.x].mode!=UNPRESSED_MODE){
////				printf("%c\n",wait_pylist[j]);
//				keys[p.y][p.x].mode = PRESSED_MODE;
//			}
//		}
//	}
//}
//
//void get_fcitx_word(void){
//
//	int i,h;
//	for(i=0;i<MAX_FCITX;i++){
//		h=i%5;
//		if(h == 0 && i!=0){
//			pageDown();
//		}
//		getWord(h,fcitx_result[i]);
//		fcitx_result[i][3]='\0';
////		printf("%s\n",fcitx_result[i]);
//	}
//}
//
//void destroy( GtkWidget *widget,GdkEvent *event, gpointer   data )
//{
//	gtk_main_quit();
//
//}
//
////when the mouse is pressed, figure out which key is down
//void refresh_cur_hex(gint x, gint y){
//	GdkPoint p={-1,-1};
//	if(get_key(&p,x,y)>0){
//		cur_hex.name=keys[p.y][p.x].input2;
//		cur_hex.x=(p.x+0.5)*WIDTH+row_indent[p.y];
//		cur_hex.y=(p.y+0.5)*HEIGHT;
//
//		return;
//	}
//	cur_hex.name='\0';
//}
//
//
//int get_key(GdkPoint* p,int x,int y){
//	int h = y/HEIGHT;
//	int i;
//	if(h<0 || h>4){return -1;}
//	int offset=row_indent[h];
//	for(i=0;i<COL;i++){
//			if(x<offset+keys[h][i].width){
//				p->x=i;
//				p->y=h;
////				p->x=offset+keys[h][i].width/2;
////				p->y=(h+0.5)*HEIGHT;
//				return 1;
//			}
//			offset+=keys[h][i].width;
//		}
//	return -1;
//}
//
//int get_key2(GdkPoint* p,int x,int y){
//	int h = y/HEIGHT;
//	int i;
//	if(h<1 || h>4){return -1;}
//	int offset=row_indent[h];
//	for(i=0;i<COL;i++){
//			if(x<offset+keys[h][i].width){
////				p->x=i;
////				p->y=h;
//				p->x=offset;
//				p->y=h*HEIGHT;
//				return 1;
//			}
//			offset+=keys[h][i].width;
//		}
//	return -1;
//}
//
//char recognize_key(int x,int y,int r){
//	int h = y/HEIGHT;
//	int i,cx,cy;
//	if(h<0 || h>4){return '\0';}
//	int offset=row_indent[h];
//	for(i=0;i<COL;i++){
//		if(x<offset+keys[h][i].width){
//			cx=offset+keys[h][i].width/2;
//			cy=(h+0.5)*HEIGHT;
//			if(sqrt((x-cx)*(x-cx)-(y-cy)*(y-cy))<r){
//				if(keys[h][i].input2 != last_input && keys[h][i].input2 != '\0' && keys[h][i].mode == PRESSED_MODE){
//					last_input = keys[h][i].input2;
//					return keys[h][i].input2;
//				}
//			}
//		}
//		offset+=keys[h][i].width;
//	}
//	return '\0';
//}
//
//int get_right_width(int index_x,int index_y){
//	if(index_y<0 || index_y>4){
//		return 0;
//	}
//	int w =0;
//	int i;
//	for(i=0;i<COL;i++){
//		w+=keys[index_y][index_x+i].width;
//	}
//	return w;
//}
//
//int get_key_pos(GdkPoint* p,int x,int y){
//	int px=x*WIDTH+row_indent[y];
//	int py=y*HEIGHT;
//	if(get_key(p,px,py)>0){
//	p->x=px;
//	p->y=py;
//		return 1;
//	}
//	return -1;
//}
//
//int get_key_pos2(GdkPoint* p,int x,int y){
//	int px=x;
//	int py=y;
//	if(get_key(p,px,py)>0){
////	p->x=px;
////	p->y=py;
//		return 1;
//	}
//	return -1;
//}
//
//int get_pos_bykey(GdkPoint* p,const char* key){
//	int i,j,cmp,offset;
//	for(i=0;i<ROW;i++){
//		offset = row_indent[i];
//		for(j=0;j<COL;j++){
//			cmp = strcmp(keys[i][j].name,key);
//			if(cmp == 0){
//				p->x=offset+keys[i][j].width/2;
//				p->y=(i+0.5)*HEIGHT;
//				return 1;
//			}
//			offset+=keys[i][j].width;
//		}
//	}
//	return -1;
//
//}
//
//void alter_input2(char c){
//	int x0=0;
//	int x1=1;
//	int x2=0;
//	int x3=1;
//	int x4=1;
//
//	switch(c){
//		case 'a':{
//			change_kb(1,x1+1,'i');
//			change_kb(2,x2+2,'n');
//			change_kb(2,x2+3,'g');
//			change_kb(3,x3+1,'o');
//			break;
//		}
//		case 'b':{
//			change_kb(1,x1+4,'o');
//			change_kb(1,x1+5,'e');
//			change_kb(2,x2+3,'g');
//			change_kb(2,x2+4,'n');
//			change_kb(2,x2+5,'a');
//			change_kb(2,x2+6,'i');
//			change_kb(2,x2+7,'n');
//			change_kb(2,x2+8,'g');
//			change_kb(3,x3+3,'i');
//			change_kb(3,x3+4,'e');
//			change_kb(3,x3+6,'o');
//			change_kb(4,x4+5,'u');
//			break;
//		}
//		case 'c':{
//			change_kb(1,x1+1,'g');
//			change_kb(1,x1+2,'n');
//			change_kb(1,x1+3,'i');
//			change_kb(1,x1+4,'o');
//			change_kb(1,x1+5,'n');
//			change_kb(1,x1+6,'g');
//			change_kb(2,x2+2,'i');
//			change_kb(2,x2+3,'e');
//			change_kb(2,x2+4,'h');
//			change_kb(2,x2+5,'u');
//			change_kb(2,x2+6,'i');
//			change_kb(3,x3+1,'a');
//			change_kb(3,x3+2,'u');
//			change_kb(3,x3+4,'a');
//			change_kb(3,x3+5,'n');
//			change_kb(3,x3+6,'g');
//			change_kb(4,x4,'g');
//			change_kb(4,x4+1,'n');
//			change_kb(4,x4+2,'o');
//			change_kb(4,x4+3,'i');
//			change_kb(4,x4+4,'o');
//			break;
//		}
//		case 'd':{
//			change_kb(1,x1,'g');
//			change_kb(1,x1+1,'n');
//			change_kb(1,x1+2,'o');
//			change_kb(1,x1+3,'a');
//			change_kb(1,x1+4,'n');
//			change_kb(1,x1+5,'g');
//			change_kb(2,x2+1,'a');
//			change_kb(2,x2+2,'u');
//			change_kb(2,x2+4,'i');
//			change_kb(2,x2+5,'u');
//			change_kb(3,x3+1,'i');
//			change_kb(3,x3+3,'e');
//			change_kb(3,x3+4,'n');
//			change_kb(3,x3+5,'g');
//			break;
//		}
//		case 'e':{
//			change_kb(1,x1+3,'r');
//			change_kb(2,x2+2,'i');
//			change_kb(2,x2+3,'n');
//			break;
//		}
//		case 'f':{
//			change_kb(1,x1+1,'g');
//			change_kb(1,x1+2,'n');
//			change_kb(1,x1+3,'a');
//			change_kb(1,x1+4,'o');
//			change_kb(2,x2+2,'i');
//			change_kb(2,x2+3,'e');
//			change_kb(2,x2+5,'u');
//			break;
//		}
//		case 'g':{
//			change_kb(0,x0+4,'n');
//			change_kb(1,x1+3,'i');
//			change_kb(1,x1+4,'u');
//			change_kb(1,x1+5,'o');
//			change_kb(1,x1+6,'n');
//			change_kb(1,x1+7,'g');
//			change_kb(2,x2+2,'g');
//			change_kb(2,x2+3,'n');
//			change_kb(2,x2+4,'a');
//			change_kb(2,x2+6,'e');
//			change_kb(2,x2+7,'i');
//			break;
//		}
//		case 'h':{
//			change_kb(1,x1+5,'e');
//			change_kb(1,x1+6,'i');
//			change_kb(2,x2+4,'g');
//			change_kb(2,x2+5,'n');
//			change_kb(2,x2+7,'a');
//			change_kb(2,x2+8,'o');
//			change_kb(3,x3+5,'o');
//			change_kb(3,x3+6,'u');
//			change_kb(3,x3+7,'n');
//			change_kb(3,x3+8,'g');
//			change_kb(4,x4+5,'i');
//			break;
//		}
//		case 'j':{
//			change_kb(0,x0+6,'n');
//			change_kb(0,x0+7,'e');
//			change_kb(0,x0+8,'o');
//			change_kb(1,x1+5,'a');
//			change_kb(1,x1+6,'u');
//			change_kb(1,x1+7,'i');
//			change_kb(1,x1+8,'n');
//			change_kb(1,x1+9,'g');
//			change_kb(2,x2+8,'a');
//			change_kb(2,x2+9,'o');
//			break;
//		}
//		case 'k':{
//			change_kb(1,x1+6,'i');
//			change_kb(1,x1+7,'a');
//			change_kb(1,x1+8,'o');
//			change_kb(1,x1+9,'n');
//			change_kb(1,x1+10,'g');
//			change_kb(2,x2+6,'g');
//			change_kb(2,x2+7,'n');
//			change_kb(2,x2+9,'u');
//			change_kb(2,x2+10,'a');
//			change_kb(3,x3+7,'e');
//			change_kb(3,x3+9,'i');
//			break;
//		}
//		case 'l':{
//			change_kb(0,x0+9,'e');
//			change_kb(0,x0+10,'a');
//			change_kb(1,x1+6,'g');
//			change_kb(1,x1+7,'n');
//			change_kb(1,x1+8,'o');
//			change_kb(1,x1+9,'u');
//			change_kb(1,x1+10,'n');
//			change_kb(2,x2+8,'a');
//			change_kb(2,x2+10,'v');
//			change_kb(3,x3+7,'u');
//			change_kb(3,x3+8,'i');
//			change_kb(3,x3+9,'e');
//			change_kb(4,x4+7,'g');
//			change_kb(4,x4+8,'n');
//			break;
//		}
//		case 'm':{
//			change_kb(1,x1+8,'u');
//			change_kb(2,x2+5,'g');
//			change_kb(2,x2+6,'n');
//			change_kb(2,x2+7,'a');
//			change_kb(2,x2+8,'i');
//			change_kb(2,x2+9,'n');
//			change_kb(3,x3+6,'o');
//			change_kb(3,x3+8,'e');
//			change_kb(3,x3+9,'g');
//			change_kb(4,x4+6,'u');
//			break;
//		}
//		case 'n':{
//			change_kb(1,x1+6,'o');
//			change_kb(1,x1+7,'u');
//			change_kb(2,x2+4,'g');
//			change_kb(2,x2+5,'n');
//			change_kb(2,x2+6,'a');
//			change_kb(2,x2+7,'i');
//			change_kb(2,x2+8,'n');
//			change_kb(2,x2+9,'g');
//			change_kb(3,x3+4,'e');
//			change_kb(3,x3+5,'u');
//			change_kb(3,x3+7,'e');
//			change_kb(4,x4+3,'g');
//			change_kb(4,x4+4,'n');
//			change_kb(4,x4+5,'o');
//			change_kb(4,x4+6,'v');
//			break;
//		}
//		case 'o':{
//			change_kb(1,x1+7,'u');
//			break;
//		}
//		case 'p':{
//			change_kb(0,x0+8,'n');
//			change_kb(0,x0+9,'a');
//			change_kb(0,x0+10,'o');
//			change_kb(1,x1+7,'g');
//			change_kb(1,x1+8,'i');
//			change_kb(1,x1+10,'u');
//			change_kb(2,x2+8,'n');
//			change_kb(2,x2+9,'e');
//			break;
//		}
//		case 'q':{
//			change_kb(0,x0+1,'e');
//			change_kb(0,x0+2,'a');
//			change_kb(1,x1+1,'i');
//			change_kb(1,x1+2,'n');
//			change_kb(1,x1+3,'g');
//			change_kb(2,x2,'n');
//			change_kb(2,x2+1,'u');
//			change_kb(2,x2+2,'o');
//			change_kb(3,x3,'a');
//			change_kb(3,x3+1,'e');
//			break;
//		}
//		case 'r':{
//			change_kb(0,x0+4,'i');
//			change_kb(1,x1+2,'e');
//			change_kb(1,x1+4,'u');
//			change_kb(1,x1+5,'a');
//			change_kb(2,x2+1,'g');
//			change_kb(2,x2+2,'n');
//			change_kb(2,x2+3,'a');
//			change_kb(2,x2+4,'o');
//			change_kb(2,x2+5,'n');
//			change_kb(2,x2+6,'g');
//			break;
//		}
//		case 's':{
//			change_kb(1,x1,'o');
//			change_kb(1,x1+1,'i');
//			change_kb(1,x1+2,'o');
//			change_kb(1,x1+3,'n');
//			change_kb(1,x1+4,'g');
//			change_kb(2,x2,'g');
//			change_kb(2,x2+1,'a');
//			change_kb(2,x2+3,'u');
//			change_kb(2,x2+4,'i');
//			change_kb(3,x3,'n');
//			change_kb(3,x3+1,'e');
//			change_kb(3,x3+2,'h');
//			change_kb(3,x3+3,'a');
//			change_kb(3,x3+4,'n');
//			change_kb(3,x3+5,'g');
//			change_kb(4,x4+1,'i');
//			change_kb(4,x4+2,'o');
//			change_kb(4,x4+3,'u');
//			break;
//		}
//		case 't':{
//			change_kb(0,x0+4,'e');
//			change_kb(0,x0+5,'n');
//			change_kb(0,x0+6,'g');
//			change_kb(1,x1+2,'o');
//			change_kb(1,x1+3,'a');
//			change_kb(1,x1+5,'o');
//			change_kb(2,x2+2,'g');
//			change_kb(2,x2+3,'n');
//			change_kb(2,x2+4,'i');
//			change_kb(2,x2+5,'u');
//			change_kb(2,x2+6,'a');
//			change_kb(3,x3+3,'e');
//			change_kb(3,x3+5,'n');
//			break;
//		}
//		case 'w':{
//			change_kb(1,x1,'o');
//			change_kb(1,x1+2,'e');
//			change_kb(1,x1+3,'i');
//			change_kb(2,x2+1,'u');
//			change_kb(2,x2+2,'a');
//			change_kb(2,x2+3,'n');
//			change_kb(2,x2+4,'g');
//			change_kb(3,x3+1,'i');
//			break;
//		}
//		case 'x':{
//			change_kb(1,x1+1,'e');
//			change_kb(1,x1+2,'n');
//			change_kb(1,x1+3,'g');
//			change_kb(2,x2+1,'a');
//			change_kb(2,x2+2,'i');
//			change_kb(2,x2+3,'u');
//			change_kb(2,x2+4,'a');
//			change_kb(2,x2+5,'n');
//			change_kb(3,x3-1,'g');
//			change_kb(3,x3,'n');
//			change_kb(3,x3+1,'o');
//			change_kb(3,x3+3,'e');
//			break;
//		}
//		case 'y':{
//			change_kb(0,x0+6,'e');
//			change_kb(1,x1+4,'i');
//			change_kb(1,x1+6,'u');
//			change_kb(1,x1+7,'a');
//			change_kb(2,x2+3,'g');
//			change_kb(2,x2+4,'n');
//			change_kb(2,x2+5,'a');
//			change_kb(2,x2+6,'o');
//			change_kb(2,x2+7,'n');
//			change_kb(2,x2+8,'g');
//			break;
//		}
//		case 'z':{
//			change_kb(1,x1,'a');
//			change_kb(1,x1+1,'i');
//			change_kb(1,x1+2,'a');
//			change_kb(1,x1+3,'n');
//			change_kb(1,x1+4,'g');
//			change_kb(2,x2,'n');
//			change_kb(2,x2+1,'u');
//			change_kb(2,x2+2,'h');
//			change_kb(2,x2+3,'o');
//			change_kb(2,x2+4,'u');
//			change_kb(3,x3-1,'g');
//			change_kb(3,x3,'o');
//			change_kb(3,x3+2,'e');
//			change_kb(3,x3+3,'n');
//			change_kb(3,x3+4,'g');
//			change_kb(4,x4-1,'n');
//			change_kb(4,x4,'a');
//			change_kb(4,x4+1,'i');
//			break;
//		}
//
//	}
//
//}
//
//void change_kb(int row, int col, char c){
//	keys[row][col].input2 = c;
//	keys[row][col].mode = WAITING_MODE;
//}
//
//int get_surrounding(GdkPoint* p, int direction){
//	if(direction<0 || direction >6){
//		return -1;
//	}
//	int cx=cur_hex.x;
//	int cy=cur_hex.y;
//	switch(direction){
//		case 1:
//			return get_key_pos2(p,cx-WIDTH/2,cy-HEIGHT);
//		case 2:
//			return get_key_pos2(p,cx+WIDTH/2,cy-HEIGHT);
//		case 3:
//			return get_key_pos2(p,cx+WIDTH,cy);
//		case 4:
//			return get_key_pos2(p,cx+WIDTH/2,cy+HEIGHT);
//		case 5:
//			return get_key_pos2(p,cx-WIDTH/2,cy+HEIGHT);
//		case 6:
//			return get_key_pos2(p,cx-WIDTH,cy);
//	}
//	return -1;
//}
//
//int get_surrounding_key(GdkPoint* p, int direction,int cx,int cy){
//	if(direction<0 || direction >6){
//		return -1;
//	}
//	switch(direction){
//		case 0:
//			return get_key(p,cx,cy);
//		case 1:
//			return get_key(p,cx-WIDTH/2,cy-HEIGHT);
//		case 2:
//			return get_key(p,cx+WIDTH/2,cy-HEIGHT);
//		case 3:
//			return get_key(p,cx+WIDTH,cy);
//		case 4:
//			return get_key(p,cx+WIDTH/2,cy+HEIGHT);
//		case 5:
//			return get_key(p,cx-WIDTH/2,cy+HEIGHT);
//		case 6:
//			return get_key(p,cx-WIDTH,cy);
//	}
//	return -1;
//}
//
//void clear_fcitx_result(void){
//	int i;
//	for(i=0;i<MAX_FCITX;i++){
//		fcitx_result[i][0]='\0';
//	}
//}
//
//void clear_input2(void){
//	int i,j;
//	for(i=0;i<ROW;i++){
//		for(j=0;j<COL;j++){
//			keys[i][j].input2 = keys[i][j].input1;
//			keys[i][j].mode = UNPRESSED_MODE;
//		}
//	}
//}
//
