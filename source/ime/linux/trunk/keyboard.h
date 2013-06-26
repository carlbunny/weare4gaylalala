#include "key.h"

#define ROW 5
#define COL 13
#define WIDTH 65
#define HEIGHT 50
#define ROOT_MODE 0
#define UNPRESSED_MODE 1	//impossible successors
#define PRESSED_MODE 2	//next successors
#define WAITING_MODE 3	//possible successors, but not next ones
#define CHOSEN_MODE 4 //already pressed ones
#define RECOGNIZE_R 25
#define MAX_PY_INPUT 100
#define MAX_FCITX 10
#define HZ_WIDTH 15
#define HZ_OFFSET_Y 20
#define ONE_PAGE_WORD 5

#define COM_INPUT 1
#define COM_CH 2
#define COM_EN 3
#define COM_NUM 4
#define COM_UPPER 5

struct key keys[ROW][COL]={
		{{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',0,UNPRESSED_MODE,FALSE,COM_INPUT},
		},
		{
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_NUM},
		 {'q','Q','0','q','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'w','W','1','w','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'e','E','2','e','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'r','R','3','r','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'t','T','-','t','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'y','Y','+','y','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'u','U','*','u','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'i','I','\\','i','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'o','O','=','o','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'p','P','\'','p','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',0,UNPRESSED_MODE,FALSE,COM_INPUT}
		},
		{{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_CH},
		 {'a','A','4','a','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'s','S','5','s','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'d','D','6','d','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'f','F','(','f','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'g','G',')','g','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'h','H','@','h','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'j','J','#','j','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'k','K','|','k','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'l','L',':','l','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\"','\"','\"','\"','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'\0','\0','\0','\0','\0',0,UNPRESSED_MODE,FALSE,COM_INPUT}
		},
		{{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_UPPER},
		 {'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_EN},
		 {'z','Z','7','z','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'x','X','8','x','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'c','C','9','c','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'v','V','!','v','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'b','B','?','b','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'n','N','<','n','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'m','M','>','m','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {',',',',',',',','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {'.','.','.','.','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
		 {' ',' ',' ',' ','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT}
		},
		{{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',WIDTH,UNPRESSED_MODE,TRUE,COM_INPUT},
			{'\0','\0','\0','\0','\0',0,UNPRESSED_MODE,FALSE,COM_INPUT}
		}
};
gint row_indent[ROW]={WIDTH/2,0,WIDTH/2,0,WIDTH/2};
