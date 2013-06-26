
#import "SwitchViewController.h"
#import	"AeviouViewController.h"
#import	"SettingViewController.h"
#import "Constants.h"

@implementation SwitchViewController

@synthesize aeviouViewController;
@synthesize settingViewController;

- (void)viewDidLoad {
	gSwitchViewController = self;
	aeviouViewController = [[AeviouViewController alloc] initWithNibName:@"AeviouViewController" bundle:nil];
	settingViewController = nil;
	[self.view insertSubview:aeviouViewController.view atIndex:0];
    [super viewDidLoad];
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Overriden to allow any orientation.
    return YES;
}


- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration{
	if (toInterfaceOrientation == UIInterfaceOrientationPortrait ||
		toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) {
		setRotateParameter(YES);
	}else{
		setRotateParameter(NO);
	}
	[[self.view.subviews objectAtIndex:0] setNeedsDisplay];
}


- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}


- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
	[aeviouViewController release];
	if (settingViewController != nil) {
		[settingViewController release];
	}
    [super dealloc];
}

- (void)swtichToSettingView{
	settingViewController = [[SettingViewController alloc] initWithNibName:@"SettingViewController" bundle:nil];
	
	settingViewController.view.frame = CGRectMake(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
	
	[UIView beginAnimations:@"View Flip" context:nil];
	[UIView setAnimationDuration:0.5];
	[UIView	setAnimationCurve:UIViewAnimationCurveEaseInOut];
	
	[UIView setAnimationTransition:
	 UIViewAnimationTransitionCurlDown forView:self.view cache:YES];
	
	[aeviouViewController viewWillDisappear:YES];
	[settingViewController viewWillAppear:YES];
	
	[self.view insertSubview:settingViewController.view atIndex:1];
	
	[aeviouViewController viewDidDisappear:YES];
	[settingViewController viewDidAppear:YES];
	
	[UIView commitAnimations];
}

- (void)swtichToAeviouView{
	[UIView beginAnimations:@"View Flip" context:nil];
	[UIView setAnimationDuration:0.5];
	[UIView	setAnimationCurve:UIViewAnimationCurveEaseInOut];
	
	
	[UIView setAnimationTransition:
	 UIViewAnimationTransitionCurlUp forView:self.view cache:YES];
	[aeviouViewController viewWillAppear:YES];
	[settingViewController viewWillDisappear:YES];
	
	[settingViewController.view removeFromSuperview];
	
	[aeviouViewController viewDidAppear:YES];
	[settingViewController viewDidDisappear:YES];
	[UIView commitAnimations];
	
	[settingViewController release];
	settingViewController = nil;
}

@end
