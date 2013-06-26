
#import <Foundation/Foundation.h>
#import "AbstractStatusHandler.h"

@interface SpecStatusHandler : AbstractStatusHandler {

}

- (SpecStatusHandler *)init:(DynamicKey**)pKeys;

- (void)load;

- (void)pressSpecStatus;

- (void)pressCap;
- (void)pressFull;
- (void)pressShift;

@end
