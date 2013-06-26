
#import <UIKit/UIKit.h>

@class SwitchViewController;

@interface AeviouAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    SwitchViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet SwitchViewController *viewController;

@end

