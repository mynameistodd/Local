//
//  AppDelegate.h
//  Local
//
//  Created by Maxwell on 10/25/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "Business.h"
@interface AppDelegate : UIResponder <UIApplicationDelegate, CLLocationManagerDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) NSMutableArray *aBusiness;
@property (strong, nonatomic) CLLocationManager *locationManager;

@end

