/*
Create the view for the marker window
I commented the old code we used for this, you need to edit the GooglePlace class to jive with it.
*/
import Foundation

class PlaceMarker: GMSMarker {
    // 1
    let place: GooglePlace
    var channelID: String
    // 2
    init(place: GooglePlace) {
        self.place = place
        self.channelID = ""
        super.init()
        
        /*
        
        marker.position = CLLocationCoordinate2DMake(b.location.latitude, b.location.longitude);
        marker.title = b.name;
        marker.snippet = b.snippet;
        marker.tappable = YES;
        marker.appearAnimation = kGMSMarkerAnimationPop;
        marker.map = mapView_;
        
        */
        self.channelID = place.channelID
        snippet = place.channelID
        title = place.name
        position = place.coordinate
        //icon = UIImage(named: place.placeType+"_pin")
        groundAnchor = CGPoint(x: 0.5, y: 1)
        appearAnimation = kGMSMarkerAnimationPop
    }
}