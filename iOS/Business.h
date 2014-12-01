//
//  Business.h
//  Local
//
//  Created by Maxwell on 10/27/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Parse/Parse.h>
#import <Parse/PFObject+Subclass.h>

@interface Business : PFObject<PFSubclassing>
//+ (NSString *)Business;
@property (retain) NSString *name;
@property NSString *snippet;
@property PFGeoPoint *location;
//@property NSString *objectId;
@property PFFile *logo;

@end
