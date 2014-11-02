//
//  SettingsViewController.m
//  Local
//
//  Created by Maxwell on 10/27/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//
#import "SettingsViewController.h"
#import "Parse/Parse.h"
@interface SettingsViewController ()

@end

@implementation SettingsViewController {
    //NSArray *business;
}



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 10;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *simpleTableIdentifier = @"SettingsTableCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    //cell.textLabel.text = [recipes objectAtIndex:indexPath.row];
    return cell;
    
}
@end

