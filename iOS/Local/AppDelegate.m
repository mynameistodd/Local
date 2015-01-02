//
//  AppDelegate.m
//  Local
//
//  Created by Maxwell on 10/25/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import "AppDelegate.h"
#import "Parse/Parse.h"
#import <GoogleMaps/GoogleMaps.h>

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    // Override point for customization after application launch.
    [GMSServices provideAPIKey:@"AIzaSyDLbCLs2w6xR9gC1WMJjlMdT9F987lh-Qo"];
    
    [Parse setApplicationId:@"m5dzHOXkMFC9BHPEbmprX02KM2GoVv2NBBPC5eUN"
                  clientKey:@"tdKfe6bJDPpstLEigKmnhyRyBhSV7vy94IA1SVHM"];
    
    // to track statistics around application opens
    [PFAnalytics trackAppOpenedWithLaunchOptions:launchOptions];

    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    UITabBar *tabBar = tabBarController.tabBar;
    UITabBarItem *tabBarItem_0 = [tabBar.items objectAtIndex:0];
    UITabBarItem *tabBarItem_1 = [tabBar.items objectAtIndex:1];
    UITabBarItem *tabBarItem_2 = [tabBar.items objectAtIndex:2];


    tabBarItem_0 = [tabBarItem_0 initWithTitle:@"" image:[UIImage imageNamed:@"bullet4.png"] selectedImage:[UIImage imageNamed:@"bullet4.png"]];
    tabBarItem_1 = [tabBarItem_1 initWithTitle:@"" image:[UIImage imageNamed:@"geo_fence-32.png"] selectedImage:[UIImage imageNamed:@"geo_fence-32.png"]];
    tabBarItem_2 = [tabBarItem_2 initWithTitle:@"" image:[UIImage imageNamed:@"internet_explorer-32.png"] selectedImage:[UIImage imageNamed:@"internet_explorer-32.png"]];
    [[UITabBar appearance] setBackgroundColor:[UIColor colorWithRed:0.71 green:0.84 blue:0.66 alpha:1.0]];
    
    // Change the title color of tab bar items
    //example on how to change text color, keep in for now
    /*
    [[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                                       [UIColor blueColor], UITextAttributeTextColor,
                                                       nil] forState:UIControlStateNormal];
    //not really sure what this is, fucking keep it for now....
    UIColor *titleHighlightedColor = [UIColor colorWithRed:153/255.0 green:192/255.0 blue:48/255.0 alpha:1.0];
    [[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                                       titleHighlightedColor, UITextAttributeTextColor,
                                                       nil] forState:UIControlStateHighlighted];
     */
    
    return YES;
}
//NSLog(@"%@",@"didRegisterForRemoteNotificationsWithDeviceToken");
//NSLog(@"%@",@"didReceiveRemoteNotification");

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
