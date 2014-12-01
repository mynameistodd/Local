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

@interface MapViewController ()

@end

@implementation MapViewController
GMSMapView *mapView_;
NSMutableArray *aBusiness;

- (void)loadView {
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    aBusiness = delegate.aBusiness;
    // Create a GMSCameraPosition that tells the map to display the
    // coordinate -33.86,151.20 at zoom level 6.
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:42.2814
                                                            longitude:-83.7483
                                                                 zoom:12];
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.myLocationEnabled = YES;
    self.view = mapView_;
    
    // Creates a marker in the center of the map.
    GMSMarker *marker = [[GMSMarker alloc] init];
    marker.position = CLLocationCoordinate2DMake(42.2814, -83.7483);
    marker.title = @"Ann Arbor";
    marker.snippet = @"Michigan";
    marker.map = mapView_;
    
    for (Business *object in aBusiness) {
        NSLog(@"ParseObject: %@", object);
        NSLog(@"%@",object[@"objectid"]);
        NSLog(@"%@",object[@"location"]);
        NSLog(@"%@",object[@"name"]);
        NSLog(@"%@",object[@"snippet"]);
    }
    
    for (Business *b in aBusiness) {
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(b.location.latitude, b.location.longitude);
        marker.title = b.name;
        marker.tappable = YES;
        marker.snippet = b.snippet;
        marker.map = mapView_;
        
        //this is the offset for the window
        //marker.infoWindowAnchor = CGPointMake(1.0, 0.5);
        
        //changes the marker image, we should do thiswpmap25
        //marker.icon = [UIImage imageNamed:@"wpmap25"];
        [mapView_ setSelectedMarker:marker];
    }
}
-(BOOL) mapView:(GMSMapView *) mapView didTapMarker:(GMSMarker *)marker
{
    NSLog(@"try");
    return YES;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
