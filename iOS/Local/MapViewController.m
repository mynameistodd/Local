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
    //this is set to Ann Arbor for now
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:42.2814
                                                            longitude:-83.7483
                                                                 zoom:12];
    
    
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    aBusiness = delegate.aBusiness;
    
    
    for (Business *object in aBusiness) {
        NSLog(@"ParseObject: %@", object);
        NSLog(@"%@",object[@"objectid"]);
        NSLog(@"%@",object[@"location"]);
        NSLog(@"%@",object[@"name"]);
        NSLog(@"%@",object[@"snippet"]);
    }
    
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.myLocationEnabled = YES;
    self.view = mapView_;
    mapView_.delegate = self;
    for (Business *b in aBusiness) {
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(b.location.latitude, b.location.longitude);
        marker.title = b.name;
        marker.tappable = YES;
        marker.snippet = b.snipppet;
        marker.map = mapView_;
    }
    /*
    GMSMarker *marker = [[GMSMarker alloc] init];
    marker.position = CLLocationCoordinate2DMake(42.2814, -83.7483);
    marker.title = @"Ann\nFucking\nArbor";
    marker.snippet = @"HI";
    marker.map = mapView_;
     */
    [mapView_ setMultipleTouchEnabled:YES];
    
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
