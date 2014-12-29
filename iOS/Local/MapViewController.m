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

@interface MapViewController () <GMSMapViewDelegate>

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
                                                                 zoom:15];
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.myLocationEnabled = YES;
    
    //the order in which the delegate is set matters, I am not sure why, many examples have it both ways...
    //so for now, set the view, then set the delegate. -mx
    //this issue effects the tap events for the map markers
    self.view = mapView_;
    mapView_.delegate = self;
    
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
- (void)mapView:(GMSMapView *)mapView didTapInfoWindowOfMarker:(GMSMarker *)marker {
    //alert for Unsubscribe
    NSString *message = [@"Unsubscribe from " stringByAppendingString:marker.title];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Lowecal Unsubscribe"
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"Yeah"
                                          otherButtonTitles:@"Nope", nil];
    [alert show];

    NSLog(@"window tap");
}
-(BOOL)mapView:(GMSMapView *) mapView didTapMarker:(GMSMarker *)marker
{
    NSLog(@"marker tap");
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
