
typedef enum{
	kNormalStatus = 0,
	kNextStatus,
	kPressedStatus,
	kSelectedStatus,
	kPossibleStatus
} KeyStatus;

typedef enum{
    kNormalKey = 0,
	kSymbolKey,
	kHiddenKey,
	kShiftKey,
	kCapKey,
	kEnterKey,
	kSetupKey,
	kDeleteKey,
	kBlankKey,
	kEnKey,
	kDotKey
} KeyType;

typedef enum{
	kChStatus = 0,
	kEnStatus,
	kNumStatus,
	kSpecStatus,
} InputStatus;

#define KEY_HORIZONTAL_SPACING 7
#define KEY_VERTICAL_SPACING -10
#define KEY_LEFT_SPACING 2
#define KEY_TOP_SPACING 680
#define KEY_WIDTH 63
#define KEY_HEIGHT 72
#define KEY_CHAR_LEFT_SPACING 23
#define KEY_CHAR_TOP_SPACING 45
#define KEYBOARD_LEFT 0
#define KEYBOARD_WIDTH 768
#define KEYBOARD_TOP 650
#define KEYBOARD_HEIGHT 400
#define GRADIENT_START 670
#define GRADIENT_END 1100

#define NEIGHBOURS_END -1

#define IMAGE_CHAR_BACKSPACE 1
#define IMAGE_CHAR_CAP 2
#define IMAGE_CHAR_CH 3
#define IMAGE_CHAR_DEL 4
#define IMAGE_CHAR_EN 5
#define IMAGE_CHAR_ENTER 6
#define IMAGE_CHAR_FULL 7
#define IMAGE_CHAR_HALF 8
#define IMAGE_CHAR_NUMBER 9
#define IMAGE_CHAR_SETUP 10
#define IMAGE_CHAR_SHIFT 11
#define IMAGE_CHAR_SPECIAL 12
#define IMAGE_CHAR_TAB 13
#define IMAGE_TOTAL_SIZE 13