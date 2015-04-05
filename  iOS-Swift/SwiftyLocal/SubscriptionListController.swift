//
//  SubscriptionListController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/17/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import Foundation
import ParseUI
import Parse
class SubscriptionListController : UITableViewController, UITableViewDelegate, UITableViewDataSource, PFLogInViewControllerDelegate, PFSignUpViewControllerDelegate {
    let appDelegate = UIApplication.sharedApplication().delegate as AppDelegate
    //var BusinessData: [Business] = []
    var GooglePlaces: [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "ReloadListView:",name:"Reload", object: nil)
        if PFUser.currentUser() != nil {
            GooglePlaces = appDelegate.GooglePlaceChannels
            if GooglePlaces.count > 0 {
                for c in GooglePlaces {
                    println("ListView: " + c)
                }
            }
        }
    }
    func ReloadListView(notification: NSNotification){
        //load data here
        GooglePlaces.removeAll(keepCapacity: false)
        let channels = PFInstallation.currentInstallation().channels
        if channels.count > 0 {
            for c in channels {
                if let channelID = c as? String {
                    GooglePlaces.append(channelID)
                    println("app delegate: " + channelID)
                }
            }
        }
        else {
            println("no subs")
        }
        self.tableView.reloadData()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidAppear(animated: Bool) {
        var debugCurrentUser: PFUser? = PFUser.currentUser()
        if PFUser.currentUser() == nil {
            var login: PFLogInViewController = PFLogInViewController()
            login.delegate = self
            
            var signin: PFSignUpViewController = PFSignUpViewController()
            signin.delegate = self
            
            login.signUpController = signin
            
            self.presentViewController(login, animated: true, completion: nil)
        }
    }
    /*
    override func viewWillAppear(animated: Bool) {
        dispatch_async(dispatch_get_main_queue()) {
            self.tableView.reloadData()
        }
    }
    */
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        //set each cell with the GooglePlace ID
        var simpleTableIdentifier : String = "SimpleTableCell"
        var cell = tableView.dequeueReusableCellWithIdentifier("SimpleTableCell") as? UITableViewCell
        
        if cell == nil {
            cell = UITableViewCell(style: UITableViewCellStyle.Value1, reuseIdentifier: "SimpleTableCell")
        }
        cell?.textLabel?.text = GooglePlaces[indexPath.row]
        //makes the little arrow on the right
        cell?.accessoryType = UITableViewCellAccessoryType.DisclosureIndicator
        return cell!
    }
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        //research why this fucked our world up when going to segue
        //tableView.deselectRowAtIndexPath(indexPath, animated: true)
        performSegueWithIdentifier("DetailViewSegue", sender: cell)
        
    }
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return GooglePlaces.count
    }
    
    //add this to numberOfRowsInSection when ready
    func SetNoSubsMessageLabel(isEnabled: Bool) {
        // Display a message when the table is empty
        var CGRectFrame: CGRect = CGRectMake(0, 0, self.view.bounds.width, self.view.bounds.height)
        var messageLabel = UILabel(frame: CGRectFrame)
        if isEnabled {
        messageLabel.text = "There are no subscriptions, go to the map and find some; temp message!"
        messageLabel.textColor = UIColor.blackColor()
        messageLabel.numberOfLines = 0
        messageLabel.textAlignment = NSTextAlignment.Center
        messageLabel.font = UIFont (name: "Palatino-Italic", size: 20)
        messageLabel.sizeToFit()
        tableView.backgroundView = messageLabel
        tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        } else {
            messageLabel.removeFromSuperview()
        }

    }
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject!) {
        if (segue.identifier == "DetailViewSegue") {
            var svc = segue.destinationViewController as DetailViewSubscribedViewController
            
            if tableView.indexPathForSelectedRow() != nil {
                var indexPath: NSIndexPath = self.tableView.indexPathForSelectedRow()!
                var row = indexPath.row
                svc.GooglePlaceId = GooglePlaces[row]
            }
        }
    }
    func logInViewController(logInController: PFLogInViewController!, didFailToLogInWithError error: NSError!) {
        
    }
    func logInViewController(logInController: PFLogInViewController!, didLogInUser user: PFUser!) {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    func logInViewControllerDidCancelLogIn(logInController: PFLogInViewController!) {
        self.navigationController?.popViewControllerAnimated(true)
    }
    func signUpViewController(signUpController: PFSignUpViewController!, didSignUpUser user: PFUser!) {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    func signUpViewController(signUpController: PFSignUpViewController!, didFailToSignUpWithError error: NSError!) {
        
    }
}