
#import <Foundation/Foundation.h>
#import "AbstractStatusHandler.h"

@interface EnStatusHandler : AbstractStatusHandler {

}

- (EnStatusHandler *)init:(DynamicKey**)pKeys;

- (void)load;

- (void)pressEnStatus;

- (void)pressCap;
- (void)pressFull;
- (void)pressShift;

@end
