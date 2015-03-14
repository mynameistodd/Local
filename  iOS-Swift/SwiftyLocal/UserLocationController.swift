//
//  UserLocationController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 3/5/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import Foundation


class UserLocation : NSObject, CLLocationManagerDelegate {
    var locationManager: CLLocationManager
    var seenError: Bool
    var locationFixAchieved: Bool
    var locationStatus : NSString

    override init() {
        self.locationManager = CLLocationManager()
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest
        self.locationManager.requestAlwaysAuthorization()
        self.seenError = false
        self.locationFixAchieved = false
        self.locationStatus = "Not Started"
        super.init()
        //self.locationManger.delegate = self
        CLLocationManager.locationServicesEnabled()
        //self.locationManger.startUpdatingLocation()
        if CLLocationManager.locationServicesEnabled() {
            self.locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
            self.locationManager.startUpdatingLocation()
        }
    }
    func GetLatitude() -> Double {
        return self.locationManager.location.coordinate.latitude
    }
    func GetLongitude() -> Double {
        return self.locationManager.location.coordinate.longitude
    }
    func monitorForLocationByLocation(location: CLLocation) {
        /*
        CLCircularRegion *geoRegion = [[CLCircularRegion alloc]
        initWithCenter:CLLocationCoordinate2DMake(object.location.latitude, object.location.longitude)
        radius:33
        identifier:object.name];
        
        //i forget why we use the arrow thingy
        [self->locationManager startMonitoringForRegion:geoRegion]
        */
    }
    func locationManager(manager: CLLocationManager!, didDetermineState state: CLRegionState, forRegion region: CLRegion!) {
        
    }
    func locationManager(manager: CLLocationManager!, didFailWithError error: NSError!) {
        self.locationManager.stopUpdatingLocation()
        if error != nil {
            if seenError == false {
                seenError = true
                println(error)
            }
        }
    }
    func locationManager(manager: CLLocationManager!, didStartMonitoringForRegion region: CLRegion!) {
        
    }
    func locationManager(manager: CLLocationManager!, didUpdateLocations locations: [AnyObject]!) {
        if locationFixAchieved == false {
            locationFixAchieved = true
            var locationArray = locations as NSArray
            var locationObj = locationArray.lastObject as CLLocation
            var coord = locationObj.coordinate
            
            println(coord.latitude)
            println(coord.longitude)
        }
    }
    func locationManager(manager: CLLocationManager!, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        var shouldAllow = false
        switch status {
        case CLAuthorizationStatus.Restricted:
            self.locationStatus = "Restricted Access to location"
        case CLAuthorizationStatus.Denied:
            self.locationStatus = "User denied access to location"
        case CLAuthorizationStatus.NotDetermined:
            self.locationStatus = "Status not determined"
        default:
            self.locationStatus = "Allowed to location Access"
            shouldAllow = true
        }
        
        NSNotificationCenter.defaultCenter().postNotificationName("LabelHasbeenUpdated", object: nil)
        if (shouldAllow == true) {
            NSLog("Location to Allowed")
            // Start location services
            self.locationManager.startUpdatingLocation()
            }
        else {
            NSLog("Denied access: \(locationStatus)")
        }
    }
}