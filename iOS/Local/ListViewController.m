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
@interface ListViewController()
@end

@implementation ListViewController {
    NSMutableArray *aBusiness;// = [NSMutableArray array];
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    PFQuery *query = [PFQuery queryWithClassName:@"Business"];
    aBusiness = [NSMutableArray array];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            // The find succeeded.
            for (Business *object in objects) {
                [aBusiness addObject:object];
            }
            AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
            
            delegate.aBusiness = nil;
            dispatch_async(dispatch_get_main_queue(), ^ {
                [self.tableView reloadData];
            });
            //now we have to pass our array to the next control
            //the control needs a MSMutableArray also, kinda like .ascx controls being rendered.
            //in this case its going to be the tablelistthingy
            
             for (Business *object in aBusiness) {
                NSLog(@"ParseObject: %@", object);
                 NSLog(@"%@",object[@"objectid"]);
                 NSLog(@"%@",object[@"location"]);
                 NSLog(@"%@",object[@"name"]);
                 NSLog(@"%@",object[@"snippet"]);
             }
            
             
        } else {
            // Log details of the failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];

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
