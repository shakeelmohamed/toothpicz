//
//  ToothpiczViewController.m
//  Tracker
//
//  Created by Shakeel Mohamed on 5/9/13.
//  Copyright (c) 2013 Toothpicz. All rights reserved.
//

#import "ToothpiczViewController.h"

@interface ToothpiczViewController ()

@end

@implementation ToothpiczViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    NSLog(@"Hello world!");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)buttonPressed:(UIButton *)sender {
    NSLog(@"Button was pressed.");
}

@end
