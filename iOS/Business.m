//
//  Business.m
//  Local
//
//  Created by Maxwell on 10/27/14.
//  Copyright (c) 2014 Maxwell. All rights reserved.
//

#import "Business.h"
#import "Parse/Parse.h"

@implementation Business

+ (void)load {
    [self registerSubclass];
}
+ (NSString *)parseClassName {
    return @"Business";
}

@dynamic name;
@dynamic snippet;
@dynamic location;
@dynamic objectId;
@dynamic logo;
@end
