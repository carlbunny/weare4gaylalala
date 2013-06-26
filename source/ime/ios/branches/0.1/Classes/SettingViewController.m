
#import "SettingViewController.h"
#import "SwitchViewController.h"
#import	"Constants.h"


@implementation SettingViewController

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Overriden to allow any orientation.
    return YES;
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


- (IBAction)pressButtonOK:(id)sender{
	[gSwitchViewController swtichToAeviouView];
}

- (IBAction)pressButtonCancel:(id)sender{

}

- (void)dealloc {
    [super dealloc];
}


@end
