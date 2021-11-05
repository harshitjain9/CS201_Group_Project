package com.example.demo;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;


import org.springframework.boot.SpringApplication;

import com.example.demo.KD.*;
import com.example.demo.VPT.DistFuncImpl;
import com.example.demo.VPT.VPTree;
import com.github.javafaker.Faker;
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
        long startTime = System.nanoTime();
		for (Business business: allData) {
			double currentDistance = Distance.distance(business.getCoordinates()[0], business.getCoordinates()[1], inputCoordinates[0], inputCoordinates[1]);
			if (currentDistance < inputRadius) {
                resultList.add(business); 
			}
		}
        for (Business business: resultList) {
            business.printBusiness(inputCoordinates);
        }
        long endTime = System.nanoTime();
        System.out.println("size is: " + resultList.size());
        long duration = (endTime - startTime);
        System.out.println("time taken is: " + duration/1000000 + "milliseconds");
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

    public static void kdTreePresort() {
        KdNodePresort root = LoadData.getRootKDTreePresort();
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

    public static void vpTree() {
        ArrayList<Business> result = LoadData.getUnsortedList();
        //System.out.println(result.size());
        double[] data = getInputData();
        Business current = new Business("current", "current address", data);
        
        VPTree<Business,Business> vpTree = new VPTree<>(new DistFuncImpl());
        for (Business each:result) {
            vpTree.add(each);
        }

        final List<Business> nearestShops_ = vpTree.getAllWithinDistance(current, current.getCoordinates()[2]);
        //System.out.println(nearestShops_.size());
        for (Business each:nearestShops_) {
            each.printBusiness(current);
        }
    }

    public static void appendJson(int n) {
        Faker faker = new Faker();;
        for (int i = 0; i < n; i++) {
        try {
            BufferedWriter addMoreData = new BufferedWriter(new FileWriter("./yelp_academic_dataset_business.json", true));

                String randomstring = faker.name().fullName();
                double randomlatitude = (Math.random() * ((90.0 - (-90.0)) + 1)) + (-90.0);   // This Will Create A Random Number Inbetween Your Min And Max.
                double randomlongitude = (Math.random() * ((180.0 - (-180.0)) + 1)) + (-180.0);   // This Will Create A Random Number Inbetween Your Min And Max.
                String s = String.format("{\"name\":\"%s\",\"address\":\"%s\",\"latitude\":%s,\"longitude\":%s}", randomstring, randomstring, Double.toString(randomlatitude), Double.toString(randomlongitude));
                // s = String.format("{\"name\":%s,\"latitude\":%lf,\"longitude\":%lf", randomstring, randomstring, randomlatitude, randomlongitude);
                addMoreData.append(s);
                System.out.println("test");
                addMoreData.append("\n");
                addMoreData.close();
            
            
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    }
	public static void main(String[] args) { 
		SpringApplication.run(Cs201ProjectApplication.class, args);
        
        
        System.out.println("Number: Algorithm");
        System.out.println("1: Linear Search");
        System.out.println("2: Space Partioning using KD Tree");
        System.out.println("3: Space Partioning using Balanced KD Tree- Presort");
        System.out.println("4: Vantage Point Tree Partition Search");
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
                    kdTreePresort();
                } else if (algorthimNumber == 4) {
                    validNumber = true;
                    vpTree();
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

