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
//NSMutableArray *aBusiness;

- (void)loadView {
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:42.2814
                                                            longitude:-83.7483
                                                                 zoom:12];
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.myLocationEnabled = YES;
    self.view = mapView_;
    mapView_.delegate = self;
    
    GMSMarker *marker = [[GMSMarker alloc] init];
    marker.position = CLLocationCoordinate2DMake(42.2814, -83.7483);
    marker.title = @"Ann\nFucking\nArbor";
    marker.snippet = @"HI";
    marker.map = mapView_;
    
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
