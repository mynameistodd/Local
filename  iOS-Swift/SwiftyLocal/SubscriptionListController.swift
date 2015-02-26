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
    var BusinessData: [Business] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        

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
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        /*
        if PFUser.currentUser() == nil {
            self.BusinessData = ParseDataProvider.GetUserSubscriptionsFromParse()
        } else {
            //else we should have a user AND have the data that the user is subcribed to
            //we still need to check the array though...
            
        }
        */
        var simpleTableIdentifier : String = "SimpleTableCell"
        var cell = tableView.dequeueReusableCellWithIdentifier("SimpleTableCell") as? UITableViewCell
        
        if cell == nil {
            cell = UITableViewCell(style: UITableViewCellStyle.Value1, reuseIdentifier: "SimpleTableCell")
        }
        cell?.textLabel?.text = "cell text"
        //makes the little arrow on the right
        cell?.accessoryType = UITableViewCellAccessoryType.DisclosureIndicator
        return cell!
    }
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        performSegueWithIdentifier("DetailViewSegue", sender: cell)
        
    }
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    /*
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //DetailViewSegue
        if (segue.identifier == "DetailViewSegue") {
            let DetailViewController: DetailViewSubscribedViewController = segue.destinationViewController as DetailViewSubscribedViewController
            let indexPath = self.tableView.indexPathForSelectedRow()
            DetailViewController.dataString = "asdf"
            //viewController.pinCode = self.exams[indexPath.row]
        }
    }
    */
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject!) {
        if (segue.identifier == "DetailViewSegue") {
            var svc = segue.destinationViewController as DetailViewSubscribedViewController
            let indexPath = self.tableView.indexPathForSelectedRow()
            svc.dataString = "asdf"
           //svc.toPass = textField.text
            
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