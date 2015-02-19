
//  ViewController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/14/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//

import UIKit
import Parse
import ParseUI
class LogInViewController: UIViewController, PFLogInViewControllerDelegate, PFSignUpViewControllerDelegate {


    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.navigationController!.navigationBar.hidden = true
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidAppear(animated: Bool) {
        var user = PFUser.currentUser()
        if PFUser.currentUser() == nil {
            var login: PFLogInViewController = PFLogInViewController()
            login.delegate = self
            
            var signin: PFSignUpViewController = PFSignUpViewController()
            signin.delegate = self
            
            login.signUpController = signin
            
            self.presentViewController(login, animated: true, completion: nil)
            
        }
        else {
            //present your view
            var storyboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            var tabBarController: UITabBarController = storyboard.instantiateViewControllerWithIdentifier("tabBar") as UITabBarController
            //self.navigationController!.popViewControllerAnimated(false)
            //self.navigationController!.pushViewController(tabBarController, animated: false)
            self.presentViewController(tabBarController, animated: false, completion: nil)
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

	