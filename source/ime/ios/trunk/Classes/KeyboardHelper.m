
#import "KeyboardHelper.h"
#import "Constants.h"

@implementation KeyboardHelper

+ (int)getKeyIdByPoint:(CGPoint)point{
	float y = point.y - KEY_TOP_SPACING;
	float x = point.x - KEY_LEFT_SPACING;
	if (y < 0 || x < 0){
		return -1;
	}
	int row = y / (KEY_HEIGHT + KEY_VERTICAL_SPACING);
	int rowRemain = (int)y % (KEY_HEIGHT + KEY_VERTICAL_SPACING);
	rowRemain = KEY_HEIGHT / 4 - rowRemain;
	if (row % 2 != 0){
		x = x - (KEY_WIDTH + KEY_HORIZONTAL_SPACING) / 2;
	}
	if (x < 0){
		return -1;
	}
	int col = x / (KEY_WIDTH + KEY_HORIZONTAL_SPACING);
	int colRemain = (int)x % (KEY_WIDTH + KEY_HORIZONTAL_SPACING);
	
	if (rowRemain > 0){
		float k = 0.75 * KEY_HEIGHT / KEY_WIDTH;
		float t1 = k * colRemain - rowRemain;
		float t2 = -k * colRemain + KEY_HEIGHT / 1.5 - rowRemain;
		if (t1 < 0 && t2 > 0){
			row = row - 1;
			if (row % 2 != 0){
				col -= 1;
			}
			col = col;
		}else if (t1 > 0 && t2 < 0){
			row = row - 1;
			if (row % 2 == 0) {
				col += 1;
			}
		}else if (t1 < 0 && t2 < 0){
			return -1;
		}
	}

	if (row > 4 || col < 0) {
		return -1;
	}

	if (row % 2 == 0) {
		if (col > 10){
			return -1;
		}
	}else {
		if (col > 9) {
			return -1;
		}
	}

	if (row % 2 == 0) {
		return (row / 2 * 21 + col);
	}else {
		return (row / 2 * 21 + 11 + col);
	}

}

@end