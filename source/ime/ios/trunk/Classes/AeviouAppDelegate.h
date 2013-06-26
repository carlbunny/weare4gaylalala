//
//  AeviouAppDelegate.h
//  Aeviou
//
//  Created by donghl on 10-7-14.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AeviouViewController;

@interface AeviouAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    AeviouViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet AeviouViewController *viewController;

@end

