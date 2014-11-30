//
//  TabViewController.m
//  Local
//
//  Created by Maxwell on 11/2/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import "TabViewController.h"
#import "MapViewController.h"
@interface TabViewController ()

@end

@implementation TabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
    NSLog(@"controller class: %@", NSStringFromClass([viewController class]));
    NSLog(@"controller title: %@", viewController.title);
    
    if (viewController == tabBarController.moreNavigationController)
    {
        tabBarController.moreNavigationController.delegate = self;
    }
}
- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item
{
    switch (tabBar.selectedItem.tag) {
        case 0:
            NSLog(@"Selected the listView Tab");
            break;
        case 1:
            NSLog(@"Selected the MapView Tab");
            //somehow need to pass data from the listview to the mapview via this thing
            //AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
            //NSString *valueInTab = delegate.myProperty;
            break;
        case 2:
            NSLog(@"Selected the SettingsView Tab");
            break;
        default:
            NSLog(@"Opps Default reached");
            break;
    }
   // NSLog(@"tab selected: %d", tabBar.selectedItem.tag);
}


#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([[segue identifier] isEqualToString:@"DetailViewSegue"])
    {
    }
    
}


@end
