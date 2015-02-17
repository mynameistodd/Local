//
//  GoogleDataProvider.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/14/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import UIKit
import Foundation
import CoreLocation

class GoogleDataProvider {
    
    let apiKey = ""
    var photoCache = [String:UIImage]()
    var placesTask = NSURLSessionDataTask()
    var session: NSURLSession {
        return NSURLSession.sharedSession()
    }
}