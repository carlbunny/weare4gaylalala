
#import <Foundation/Foundation.h>
#import "AbstractStatusHandler.h"

@interface NumStatusHandler : AbstractStatusHandler {

}

- (EnStatusHandler *)init:(DynamicKey**)pKeys;

- (void)load;

- (void)pressNumStatus;

- (void)pressCap;
- (void)pressFull;
- (void)pressShift;

@end