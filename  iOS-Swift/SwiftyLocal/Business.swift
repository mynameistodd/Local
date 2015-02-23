//
//  Business.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/17/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import Foundation

class Business {
    var objectId: String
    var message: String
    var channelId: String
    var location: String
    var logo: String
    var name: String
    var snippet: String
    
    init(objectId: String, message: String, channelId: String, location: String, logo: String, name: String, snippet: String) {
        self.objectId = objectId
        self.message = message
        self.channelId = channelId
        self.location = location
        self.logo = logo
        self.name = name
        self.snippet = snippet
    }
}