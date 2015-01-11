//
//  SecondViewController.m
//  Local
//
//  Created by Maxwell on 10/25/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import "MapViewController.h"
#import <GoogleMaps/GoogleMaps.h>
#import "Business.h"

#import "AppDelegate.h"
#import "ListViewController.h"

@interface MapViewController () <GMSMapViewDelegate, UIAlertViewDelegate>

@end

@implementation MapViewController
GMSMapView *mapView_;
NSMutableArray *aBusiness;

- (void)loadView {
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    aBusiness = delegate.aBusiness;

    
    //hard coded for now
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:42.2814
                                                            longitude:-83.7483
                                                                 zoom:15];
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.settings.myLocationButton = YES;
    mapView_.myLocationEnabled = YES;
    //the order in which the delegate is set matters, I am not sure why, many examples have it both ways...
    //so for now, set the view, then set the delegate. -mx
    //this issue effects the tap events for the map markers
    self.view = mapView_;
    mapView_.delegate = self;
    CLLocation *mylocation = mapView_.myLocation;
    /*
    for (Business *object in aBusiness) {
        NSLog(@"ParseObject: %@", object);
        NSLog(@"%@",object[@"objectid"]);
        NSLog(@"%@",object[@"location"]);
        NSLog(@"%@",object[@"name"]);
        NSLog(@"%@",object[@"snippet"]);
    }
    */
    for (Business *b in aBusiness) {
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(b.location.latitude, b.location.longitude);
        marker.title = b.name;
        marker.snippet = b.snippet;
        marker.tappable = YES;
        marker.appearAnimation = kGMSMarkerAnimationPop;
        marker.map = mapView_;
        
        // Build a circle for the GMSMapView
        GMSCircle *geoFenceCircle = [[GMSCircle alloc] init];
        geoFenceCircle.radius = 130; // Meters
        geoFenceCircle.position = CLLocationCoordinate2DMake(b.location.latitude, b.location.longitude);
        geoFenceCircle.fillColor = [UIColor colorWithWhite:0.7 alpha:0.5];
        geoFenceCircle.strokeWidth = 3;
        geoFenceCircle.strokeColor = [UIColor orangeColor];
        geoFenceCircle.map = mapView_; // Add it to the map.
        
        //this is the offset for the window
        //marker.infoWindowAnchor = CGPointMake(1.0, 0.5);
        //changes the marker image, we should do thiswpmap25
        //marker.icon = [UIImage imageNamed:@"wpmap25"];
        
        //[mapView_ setSelectedMarker:marker];
    }
}
// Location Manager Delegate Methods
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSLog(@"%@", [locations lastObject]);
}
- (void)mapView:(GMSMapView *)mapView didTapInfoWindowOfMarker:(GMSMarker *)marker {
    //alert for Unsubscribe
    NSString *message = [@"Unsubscribe from " stringByAppendingString:marker.title];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Lowecal Unsubscribe"
                                                    message:message
                                                   delegate:self
                                          cancelButtonTitle:@"Yeah"
                                          otherButtonTitles:@"Nope", nil];
    [alert show];

    NSLog(@"window tap");
}

-(BOOL)mapView:(GMSMapView *) mapView didTapMarker:(GMSMarker *)marker
{
    NSString *logmessage = [@"tapped: " stringByAppendingString:marker.title];
    NSLog(logmessage);
    return NO;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (buttonIndex == [alertView cancelButtonIndex]){
        //cancel clicked ...do your action
        NSLog(@"Yeah clicked");
    }else{
        //reset clicked
        NSLog(@"Nope clicked");
    }
}

@end
