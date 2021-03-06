
#import "Constants.h"

int KEY_TOP_SPACING = 680;
int KEY_LEFT_SPACING = 2;
int GRADIENT_START = 735;
int GRADIENT_END = 1100;
BOOL CHANGE_ORITATION = NO;
int VIEW_HEIGHT = 1004;
int VIEW_WIDTH = 768;

SwitchViewController *gSwitchViewController;

void setRotateParameter(BOOL vertical){
	if (vertical) {
		KEY_TOP_SPACING = 680;
		KEY_LEFT_SPACING = 2;
		GRADIENT_START = 735;
		GRADIENT_END = 1100;
		VIEW_HEIGHT = 1004;
		VIEW_WIDTH = 768;
	}else{
		KEY_TOP_SPACING = 423;
		KEY_LEFT_SPACING = 135;
		GRADIENT_START = 478;
		GRADIENT_END = 850;
		VIEW_HEIGHT = 748;
		VIEW_WIDTH = 1024;
	}
	CHANGE_ORITATION = YES;
}