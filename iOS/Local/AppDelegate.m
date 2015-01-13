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
#import <CoreLocation/CoreLocation.h>
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

    
    //for push code
    UIUserNotificationType userNotificationTypes = (UIUserNotificationTypeAlert |
                                                    UIUserNotificationTypeBadge |
                                                    UIUserNotificationTypeSound);
    
    UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:userNotificationTypes
                                                                             categories:nil];
    [application registerUserNotificationSettings:settings];
    [application registerForRemoteNotifications];
    
    //for geofencing, knowing where the client is
    self.locationManager = [[CLLocationManager alloc] init];
    
    self.locationManager.delegate = self;
    [self.locationManager requestAlwaysAuthorization];
    
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestWhenInUseAuthorization];
    }
    [self.locationManager startUpdatingLocation];
    //used to pass the locationManger object to the mapview, so we dont need to recreate it again.
    AppDelegate *LMdelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    LMdelegate.locationManager = self.locationManager;
    
    //created the tabs here
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    UITabBar *tabBar = tabBarController.tabBar;
    UITabBarItem *tabBarItem_0 = [tabBar.items objectAtIndex:0];
    UITabBarItem *tabBarItem_1 = [tabBar.items objectAtIndex:1];
    UITabBarItem *tabBarItem_2 = [tabBar.items objectAtIndex:2];


    tabBarItem_0 = [tabBarItem_0 initWithTitle:@"" image:[UIImage imageNamed:@"bullet4.png"] selectedImage:[UIImage imageNamed:@"bullet4.png"]];
    tabBarItem_1 = [tabBarItem_1 initWithTitle:@"" image:[UIImage imageNamed:@"geo_fence-32.png"] selectedImage:[UIImage imageNamed:@"geo_fence-32.png"]];
    tabBarItem_2 = [tabBarItem_2 initWithTitle:@"" image:[UIImage imageNamed:@"internet_explorer-32.png"] selectedImage:[UIImage imageNamed:@"internet_explorer-32.png"]];
    [[UITabBar appearance] setBackgroundColor:[UIColor colorWithRed:0.71 green:0.84 blue:0.66 alpha:1.0]];
    
    //create geofences here?  It should be in its own class(es)
    
    
    
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

//the request for location services will be here, one the user sets it up they wont need to do it again.
- (void)requestAlwaysAuthorization
{
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    
    // If the status is denied or only granted for when in use, display an alert
    if (status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusDenied) {
        NSString *title;
        title = (status == kCLAuthorizationStatusDenied) ? @"Location services are off" : @"Background location is not enabled";
        NSString *message = @"To use background location you must turn on 'Always' in the Location Services Settings";
        
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                            message:message
                                                           delegate:self
                                                  cancelButtonTitle:@"Cancel"
                                                  otherButtonTitles:@"Settings", nil];
        [alertView show];
    }
    // The user has not enabled any location services. Request background authorization.
    else if (status == kCLAuthorizationStatusNotDetermined) {
        [self.locationManager requestAlwaysAuthorization];
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1) {
        // Send the user to the Settings for this app
        NSURL *settingsURL = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
        [[UIApplication sharedApplication] openURL:settingsURL];
    }
}
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSLog(@"%@",@"didRegisterForRemoteNotificationsWithDeviceToken");
    // Store the deviceToken in the current installation and save it to Parse.
    PFInstallation *currentInstallation = [PFInstallation currentInstallation];
    [currentInstallation setDeviceTokenFromData:deviceToken];
    currentInstallation.channels = @[ @"global" ];
    [currentInstallation saveInBackground];
}
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    NSLog(@"%@",@"didReceiveRemoteNotification");
    [PFPush handlePush:userInfo];
}
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
