//
//  PlacesViewController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/18/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import UIKit

class PlacesViewController: UITableViewController, UITableViewDelegate, UITableViewDataSource {

    override func viewDidLoad() {
        super.viewDidLoad()
        //self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"Contact"
        //style:UIBarButtonItemStylePlain target:self action:@selector(contact:)];
        var button = UIBarButtonItem(title: "Map", style: UIBarButtonItemStyle.Plain, target: self, action: "ShowGoogleMap:")
        
        self.navigationItem.rightBarButtonItem = button

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func ShowGoogleMap(sender: UIBarButtonItem) {
        performSegueWithIdentifier("GoogleMapSegue", sender: self)
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
