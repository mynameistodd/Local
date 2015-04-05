//
//  DetailViewSubscribedViewController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/17/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import UIKit
import Parse
class DetailViewSubscribedViewController: UIViewController {
    
    var staticMapURL = "https://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=390x346&key=AIzaSyBHMTiSraTxwrYX7X2auIFLV4Yzm81Iagk"
    
    
    @IBOutlet weak var StaticMapView: UIImageView!
    @IBOutlet weak var GooglePlaceIDLabel: UILabel!
    @IBAction func UnSubscribeButton(sender: UIButton) {
        let currentInstallation = PFInstallation.currentInstallation()
        currentInstallation.removeObject(self.GooglePlaceId, forKey: "channels")
        currentInstallation.saveInBackgroundWithBlock { (succeed:Bool, error: NSError!) -> Void in
            if succeed {
                println("removed channelid: " + self.GooglePlaceId!)
                NSNotificationCenter.defaultCenter().postNotificationName("Reload", object: nil)
            }
            else {
                println("error removing channelid: " + self.GooglePlaceId!)
            }
            
        }
    }
    var GooglePlaceId: String? = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        if GooglePlaceId != "" {
            println(GooglePlaceId)
            println(self.GooglePlaceId)
            self.GooglePlaceIDLabel.text = GooglePlaceId
        } else {
            self.GooglePlaceIDLabel.text = "nil"  //test code, there is a bug with parse setting an empty channelID string on app start
        }
        if let url = NSURL(string: "https://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=390x346&key=AIzaSyBHMTiSraTxwrYX7X2auIFLV4Yzm81Iagk") {
            if let data = NSData(contentsOfURL: url){
                self.StaticMapView.contentMode = UIViewContentMode.ScaleAspectFit
                self.StaticMapView.image = UIImage(data: data)
            }
        }
        /*
        dispatch_async(dispatch_get_main_queue()) {
            var imageURL: NSString = "https://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=390x346&key=AIzaSyBHMTiSraTxwrYX7X2auIFLV4Yzm81Iagk"
            var
            //var imageData: NSData = NSData(contentsOfURL:
        }
        */
        /*
        //imgURL needs to be fixed up for getting the right GPS corrds
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString *imgURL = @"https://maps.googleapis.com/maps/api/staticmap?center=-15.800513,-47.91378&zoom=11&size=200x200";
        NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:imgURL]];
        
        //set your image on main thread.
        dispatch_async(dispatch_get_main_queue(), ^{
        [self.staticImage setImage:[UIImage imageWithData:data]];
        
        });
        });
        
        */
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
