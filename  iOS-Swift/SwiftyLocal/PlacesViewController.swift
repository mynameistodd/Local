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
        var button = UIBarButtonItem(title: "Map", style: UIBarButtonItemStyle.Bordered, target: self, action: "ShowGoogleMap:")
        
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
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        var simpleTableIdentifier : String = "SimpleTableCell"
        var cell = tableView.dequeueReusableCellWithIdentifier("SimpleTableCell") as? UITableViewCell
        
        if cell == nil {
            cell = UITableViewCell(style: UITableViewCellStyle.Value1, reuseIdentifier: "SimpleTableCell")
        }
        cell?.textLabel?.text = "Places Cell Text"
        //makes the little arrow on the right
        cell?.accessoryType = UITableViewCellAccessoryType.DisclosureIndicator
        return cell!
    }
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        performSegueWithIdentifier("PlacesDetailViewSegue", sender: cell)
        
    }
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
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
