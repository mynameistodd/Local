//
//  TabViewController.swift
//  SwiftyLocal
//
//  Created by Max Nesler on 2/17/15.
//  Copyright (c) 2015 Max Nesler. All rights reserved.
//
import Parse
import ParseUI
import Foundation

class TabViewController: UITabBarController, UITabBarControllerDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        var tabViewController = self  //not sure if this is needed, should just be set to self
        
        var SubscriptionList : UITabBarItem = self.tabBar.items![0] as UITabBarItem
        var PlacesList       : UITabBarItem = self.tabBar.items![1] as UITabBarItem
        var OptionsLogOut    : UITabBarItem = self.tabBar.items![2] as UITabBarItem
        
        SubscriptionList.title = "Subs"
        PlacesList.title = "Map"
        OptionsLogOut.title = "Options"
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidAppear(animated: Bool) {
    }
}