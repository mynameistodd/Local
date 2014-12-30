//
//  DetailViewController.h
//  Local
//
//  Created by Maxwell on 11/1/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Business.h"
@interface DetailViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *label;
@property (weak, nonatomic) IBOutlet UILabel *snippet;
@property (strong, nonatomic) IBOutlet UIImageView *staticImage;
@property Business *business;

@end
