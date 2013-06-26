
#import "WordList.h"

/*
 File format:
 ==============repeat===============
 
 short int    length           2
 short int    listSize         2
 
 
     =========repeat x listSize=========
     short int    hanzi            2 x length
     int          frequency        4
     =============end repeat============
 
 
 =============end repeat============
 */

unsigned char* listFile;

