//
//  FirstViewController.m
//  Local
//
//  Created by Maxwell on 10/25/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import "ListViewController.h"
#import "Parse/Parse.h"
#import "Business.h"
#import "DetailViewController.h"
#import "AppDelegate.h"
#import "CoreLocation/CoreLocation.h"
@interface ListViewController()
@end

@implementation ListViewController {
    NSMutableArray *aBusiness;
    CLLocationManager *locationManager;
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    AppDelegate *LMdelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    locationManager = LMdelegate.locationManager;
    locationManager.delegate = self;
    PFQuery *query = [PFQuery queryWithClassName:@"Business"];
    aBusiness = [NSMutableArray array];
    
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            // The find succeeded.
            for (Business *object in objects) {
                [aBusiness addObject:object];
                
                CLCircularRegion *geoRegion = [[CLCircularRegion alloc]
                                               initWithCenter:CLLocationCoordinate2DMake(object.location.latitude, object.location.longitude)
                                               radius:33
                                               identifier:object.name];
                
                //i forget why we use the arrow thingy
                [self->locationManager startMonitoringForRegion:geoRegion];
                

            }
            //this is a trick for sending the data loaded in this view to the map view
            AppDelegate *delegate1 = (AppDelegate *)[[UIApplication sharedApplication] delegate];
            delegate1.aBusiness = aBusiness;
            
            dispatch_async(dispatch_get_main_queue(), ^ {
                [self.tableView reloadData];
            });
        } else {
            // Log details of the failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];

}

-(void)locationManager:(CLLocationManager *)manager didEnterRegion:(CLRegion *)region {
    NSLog(@"didEnterRegion %@", region.identifier);
    NSString *message = [@"didEnterRegion: " stringByAppendingString:region.identifier] ;
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"didEnterRegionEvent"
                                                    message:message
                                                   delegate:self
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:@"OK", nil];
    [alert show];
}


-(void)locationManager:(CLLocationManager *)manager didExitRegion:(CLRegion *)region {
    NSLog(@"Bye bye");
    NSLog(@"didExitRegion %@", region.identifier);
    NSString *message = [@"didEnterRegion: " stringByAppendingString:region.identifier] ;
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"didExitRegionEvent"
                                                    message:message
                                                   delegate:self
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:@"OK", nil];
    [alert show];
}

-(void)locationManager:(CLLocationManager *)manager didStartMonitoringForRegion:(CLRegion *)region {
    NSLog(@"Now monitoring for %@", region.identifier);
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [aBusiness count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    //cell.textLabel.text = [recipes objectAtIndex:indexPath.row];
    Business *b = [aBusiness objectAtIndex:indexPath.row];
    cell.textLabel.text = b.name;
    return cell;
}
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"DetailViewSegue"])
    {
        DetailViewController *detailViewController = [segue destinationViewController];
        NSIndexPath *myIndexPath = [self.tableView indexPathForSelectedRow];
        
        Business *  b = [aBusiness objectAtIndex:myIndexPath.row];
        detailViewController.business = b;
    }
}
@end
