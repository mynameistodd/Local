//
//  ParseDataProvider.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/23/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import Foundation
import Parse
class ParseDataProvider {
    
    class func GetUserSubscriptionsFromParse() -> [Business] {
        
        //var currentUser: PFUser
        //currentUser = PFUser.currentUser()
        var businessesUserIsSubcribedTo: [Business] = []
        var url : String = "https://api.parse.com/1/classes/User"
        var request : NSMutableURLRequest = NSMutableURLRequest()
        request.URL = NSURL(string: url)
        request.HTTPMethod = "GET"
        
        NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue(),
            completionHandler:{ (response:NSURLResponse!, data: NSData!, error: NSError!) -> Void in
            var error: AutoreleasingUnsafeMutablePointer<NSError?> = nil
                
            var jsonResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: error) as NSDictionary
                
            if(error != nil) {
                    // If there is an error parsing JSON, print it to the console
                    println("JSON Error \(error.debugDescription)")
            }
                //return businessesUserIsSubcribedTo
            
        }) //NSURLConnection end call
        
        return businessesUserIsSubcribedTo
        
    }
}