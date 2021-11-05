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

    public static double[] getInputData() {
        Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter the radius: ");
		double inputRadius = Double.parseDouble(scanner.nextLine());
        double[] inputData = {inputLatitude, inputLongitude, inputRadius};
        scanner.close();
        return inputData;
    }


	public static void linearSearch() {
		ArrayList<Business> allData = LoadData.getUnsortedList();
        double[] inputData = getInputData();
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        ArrayList<Business> resultList = new ArrayList<>();

		for (Business business: allData) {
			double currentDistance = Distance.distance(business.getCoordinates()[0], business.getCoordinates()[1], inputCoordinates[0], inputCoordinates[1]);
			if (currentDistance < inputRadius) {
                resultList.add(business); 
			}
		}
        for (Business business: resultList) {
            business.printBusiness(inputCoordinates);
        }
        System.out.println(resultList.size());
	}

    public static void spacePartitioning() {
        KDTree kdt = LoadData.getKDTree();
        double[] inputData = getInputData();
        double[] inputCoordinates = {inputData[0], inputData[1]};
        Business newBusiness = new Business("no name", "no address", inputCoordinates);


        ArrayList<KDNode> kdnList = kdt.find_nearest_list(newBusiness, inputCoordinates[0], inputCoordinates[1], inputData[2]);
        KDNode kdn = kdt.find_nearest(newBusiness, inputCoordinates[0], inputCoordinates[1], inputData[2]);
        System.out.println("Name: " + kdn.x.getName());
		System.out.println("Address: " + kdn.x.getAddr());
		System.out.println("Minimum Distance: " + String.valueOf(kdt.find_nearest_distance()));

        System.out.println("\nList of all nodes within the input radius: ");
        for (KDNode node : kdnList) {
            Business business = node.x;
            System.out.println(business.getName());
        }
        
    }

    public static void kdTreePresort(int n) {
        //160585
        //10, 100, 1000, 10000, 100000, 160585 

        //amount of memory occupied by the program - find a tool
        KdNodePresort root = LoadData.getRootKDTreePresort(n);
        double[] inputData = getInputData();
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        List<KdNodePresort> kdNodes = root.searchKdTree(inputCoordinates, inputRadius, 0);
		for (int i = 0; i < kdNodes.size(); i++) {
			KdNodePresort node = kdNodes.get(i);
			node.business.printBusiness(inputCoordinates);
		}
        System.out.println(kdNodes.size());
        System.out.println("");
    }
 
	public static void main(String[] args) {
		SpringApplication.run(Cs201ProjectApplication.class, args);

        System.out.println("Number: Algorithm");
        System.out.println("1: Linear Search");
        System.out.println("2: Space Partioning using KD Tree");
        System.out.println("3: Space Partioning using Balanced KD Tree- Presort");
        Scanner scanner = new Scanner(System.in);
        boolean validNumber = false;
        int algorthimNumber = 1;
        while (validNumber == false) {
            try {
                System.out.println("Enter the algorithm number:");
                algorthimNumber = Integer.parseInt(scanner.nextLine());
                if (algorthimNumber == 1) {
                    validNumber = true;
                    linearSearch();
                } else if (algorthimNumber == 2) {
                    validNumber = true;
                    spacePartitioning();
                } else if (algorthimNumber == 3) {
                    validNumber = true;
                    System.out.println("Enter the value of n:");
                    int iterNum = Integer.parseInt(scanner.nextLine());
                    kdTreePresort(iterNum);
                } else {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            } 
        } 
        scanner.close();
	}	

}

