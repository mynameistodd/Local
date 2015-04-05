//
//  MapViewController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 3/9/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import UIKit
import Parse
class MapViewController:UIViewController, CLLocationManagerDelegate, GMSMapViewDelegate, UIAlertViewDelegate {

    @IBOutlet var mapView: GMSMapView!
    let locationManager = CLLocationManager()
    //let locationManager = UserLocation()
    let dataProvider = GoogleDataProvider()
    let appDelegate = UIApplication.sharedApplication().delegate as AppDelegate
    //var searchedTypes = ["bakery", "bar", "cafe", "grocery_or_supermarket", "restaurant"]
    var searchedTypes = ["cafe"]
    
    var mapRadius: Double {
        get {
            let region = mapView.projection.visibleRegion()
            let verticalDistance = GMSGeometryDistance(region.farLeft, region.nearLeft)
            let horizontalDistance = GMSGeometryDistance(region.farLeft, region.farRight)
            //println(max(horizontalDistance, verticalDistance)*0.5)
            return max(horizontalDistance, verticalDistance)*0.5
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        mapView.delegate = self
        CLLocationManager.locationServicesEnabled()
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            self.locationManager.startUpdatingLocation()
        }
        var searchButton: UIButton = UIButton()
        
        /*
        UIButton *myLocationButton = [self.mapView myLocationButton];
        
        if (myLocationButton) {
        [myLocationButton setImage:[UIImage imageNamed:@"map_options_button_mylocation_default"] forState:UIControlStateNormal];
        [myLocationButton setImage:[UIImage imageNamed:@"map_options_button_mylocation_pressed"] forState:UIControlStateHighlighted];
        [myLocationButton setImage:[UIImage imageNamed:@"map_options_button_mylocation_pressed"] forState:UIControlStateSelected];
        }
        
        */
        // Do any additional setup after loading the view.
    }
    func locationManager(manager: CLLocationManager!, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        if status == .AuthorizedWhenInUse {
            locationManager.startUpdatingLocation()
            mapView.myLocationEnabled = true
            mapView.settings.myLocationButton = true
        }
    }
    func locationManager(manager: CLLocationManager!, didUpdateLocations locations: [AnyObject]!) {
        if self.locationManager.location != nil {
            println(self.locationManager.location!.coordinate.longitude)
            println(self.locationManager.location!.coordinate.latitude)
        }
        println("didUpdateLocations")
        mapView.camera = GMSCameraPosition(target: locationManager.location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        locationManager.stopUpdatingLocation()
        fetchNearbyPlaces(locationManager.location.coordinate)
    }
    func mapView(mapView: GMSMapView!, didTapMarker marker: GMSMarker!) -> Bool {
        println("tapped marker")
        return false
    }
    func mapView(mapView: GMSMapView!, didTapInfoWindowOfMarker marker: GMSMarker!) {
        println("window tapped: " + marker.snippet)

        let actionSheetController: UIAlertController = UIAlertController(title: "Lowcow says Moooo", message: "Subscribe to " + marker.title + "?", preferredStyle: .ActionSheet)
        let cancelAction: UIAlertAction = UIAlertAction(title: "Nah", style: .Cancel) { action -> Void in
            //Just dismiss the action sheet
        }
        
        let subscribeAction: UIAlertAction = UIAlertAction(title: "Yeah", style: .Default) { action -> Void in
            //Code for sign up here!
            let currentInstallation = PFInstallation.currentInstallation()
            currentInstallation.addUniqueObject(marker.snippet, forKey: "channels")
            currentInstallation.save()
            currentInstallation.saveInBackgroundWithBlock { (succeed:Bool, error: NSError!) -> Void in
                if succeed {
                    println("Subbed to " + marker.snippet)
                    //this reloads the table, we need to change the color of the marker too
                    marker.icon = GMSMarker.markerImageWithColor(UIColor.greenColor())
                    NSNotificationCenter.defaultCenter().postNotificationName("Reload", object: nil)
                }
                else {
                    println("error subbing to " + marker.snippet)
                }
            }
        }
        
        actionSheetController.addAction(cancelAction)
        actionSheetController.addAction(subscribeAction)
        
        //We need to provide a popover sourceView when using it on iPad
        //actionSheetController.popoverPresentationController?.sourceView = sender as UIView;
        
        //Present the AlertController
        self.presentViewController(actionSheetController, animated: true, completion: nil)
    }
    func mapView(mapView: GMSMapView!, didChangeCameraPosition position: GMSCameraPosition!) {
        println("didChangeCameraPosition")
    }
    func mapView(mapView: GMSMapView!, idleAtCameraPosition position: GMSCameraPosition!) {
        println("idleAtCameraPosition")
        //fetchNearbyPlaces(mapView.camera.target)
    }
    /*
    enable this to get a custom marker window, it still needs work, but keep here in case we get froggy.
    func mapView(mapView: GMSMapView!, markerInfoContents marker: GMSMarker!) -> UIView! {
        let placeMarker = marker as PlaceMarker
        var infoView = UIView()
        
        return infoView
    }
    */
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func fetchNearbyPlaces(coordinate: CLLocationCoordinate2D) {
        //dont clear the maps marker while testing, the rate seems to be PER  Place, not request for places
        //mapView.clear()
        dataProvider.fetchPlacesNearCoordinate(coordinate, radius:mapRadius, types: searchedTypes) { places in
        //dataProvider.fetchPlacesNearCoordinate(coordinate, radius:2000.00, types: searchedTypes) { places in
            for place: GooglePlace in places {
                let marker = PlaceMarker(place: place)
                var bContains = contains(self.appDelegate.GooglePlaceChannels, place.channelID)
                if bContains {
                    //we are subbed to the google place, lets change to color of the marker
                    marker.icon = GMSMarker.markerImageWithColor(UIColor.greenColor())
                }
                marker.map = self.mapView
            }
        }
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
