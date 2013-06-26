//
//  SwitchViewController.h
//  Aeviou
//
//  Created by Dong Haoliang on 10-8-2.
//  Copyright 2010 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AeviouViewController;
@class SettingViewController;

@interface SwitchViewController : UIViewController {
	AeviouViewController *aeviouViewController;
	SettingViewController *settingViewController;
}

@property (retain, nonatomic) AeviouViewController *aeviouViewController;
@property (retain, nonatomic) SettingViewController *settingViewController;
-(void)swtichToSettingView;
-(void)swtichToAeviouView;

@end
