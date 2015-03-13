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
    
  
    @IBOutlet weak var GooglePlaceIDLabel: UILabel!

    @IBAction func UnSubscribeButton(sender: UIButton) {
    }
    var GooglePlaceId: String? = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        if GooglePlaceId != "" {
            println(GooglePlaceId)
            println(self.GooglePlaceId)
            self.GooglePlaceIDLabel.text = GooglePlaceId
        } else {
            self.GooglePlaceIDLabel.text = "nil"
        }
        
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
