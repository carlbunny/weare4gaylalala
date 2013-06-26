
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
extern int KEY_LEFT_SPACING;
extern int KEY_TOP_SPACING;
extern int GRADIENT_START;
extern int GRADIENT_END;
extern BOOL CHANGE_ORITATION;
extern int VIEW_HEIGHT;
extern int VIEW_WIDTH;
void setRotateParameter(BOOL vertical);
#define KEY_WIDTH 63
#define KEY_HEIGHT 72
#define KEY_CHAR_LEFT_SPACING 23
#define KEY_CHAR_TOP_SPACING 45
#define CANDIDATE_HEIGHT 100

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

@class SwitchViewController;
extern SwitchViewController *gSwitchViewController;