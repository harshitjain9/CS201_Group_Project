package com.example.demo;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;


import org.springframework.boot.SpringApplication;

import com.example.demo.KD.*;
import com.example.demo.LoadData;
import com.example.demo.Distance;

//@SpringBootApplication
public class Cs201ProjectApplication {

    public static double[] getInputCoordinates() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
        double[] inputCoordinates = {inputLatitude, inputLongitude};
        scanner.close();
        return inputCoordinates;
    }

	public static void linearSearch() {
		ArrayList<Business> allData = LoadData.getUnsortedList();

        double[] inputCoordinates = getInputCoordinates();
		double minimumDistance = Double.MAX_VALUE;

		String resultName = "";
		String resultAddress = "";

		for (Business each:allData) {
			double currentDistance = Distance.distance(each.getCoordinates()[0], each.getCoordinates()[1], inputCoordinates[0], inputCoordinates[1]);
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				resultName = each.getName();
				resultAddress = each.getAddr();
			}
		}
		System.out.println("Name: " + resultName);
		System.out.println("Address: " + resultAddress);
		System.out.println("Minimum Distance: " + String.valueOf(minimumDistance));
	}

    public static void spacePartitioning() {
        KDTree kdt = LoadData.getKDTree();
		double[] inputCoordinates = getInputCoordinates();
        Business newBusiness = new Business("no name", "no address", inputCoordinates);

        KDNode kdn = kdt.find_nearest(newBusiness);
        System.out.println("Name: " + kdn.x.getName());
		System.out.println("Address: " + kdn.x.getAddr());
		System.out.println("Minimum Distance: " + String.valueOf(kdt.find_nearest_distance()));
        
    }
 
	public static void main(String[] args) {
		SpringApplication.run(Cs201ProjectApplication.class, args);

        System.out.println("Number: Algorithm");
        System.out.println("1: Linear Search");
        System.out.println("2: Space Partioning using KD Tree");
        System.out.println("3: Space Partioning using Balanced KD Tree- Presort");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the algorithm number:");
        boolean validNumber = false;
        int algorthimNumber = 1;
        while (validNumber == false) {
            try {
                algorthimNumber = Integer.parseInt(scanner.nextLine());
                validNumber = true;
                if (algorthimNumber == 1) {
                    linearSearch();
                } else if (algorthimNumber == 2) {
                    spacePartitioning();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid number:");
            } 
        } 
        scanner.close();
	}	

}

