package com.example.demo;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import com.github.javafaker.Faker;


import org.springframework.boot.SpringApplication;

import com.example.demo.KD.*;
import com.example.demo.LoadData;
import com.example.demo.Distance;

//@SpringBootApplication
public class Cs201ProjectApplication {

    public static void appendJson(int n) {
        Faker faker = new Faker();;
        for (int i = 0; i < n; i++) {
        try {
            BufferedWriter addMoreData = new BufferedWriter(new FileWriter("./yelp_academic_dataset_business.json", true));

                String randomstring = faker.name().fullName();
                double randomlatitude = (Math.random() * ((90.0 - (-90.0)) + 1)) + (-90.0);   // This Will Create A Random Number Inbetween Your Min And Max.
                double randomlongitude = (Math.random() * ((180.0 - (-180.0)) + 1)) + (-180.0);   // This Will Create A Random Number Inbetween Your Min And Max.
                String s = String.format("{\"name\":\"%s\",\"address\":\"%s\",\"latitude\":%s,\"longitude\":%s}", randomstring, randomstring, Double.toString(randomlatitude), Double.toString(randomlongitude));
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


	public static void linearSearch(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
		ArrayList<Business> allData = LoadData.getUnsortedList();
        long endTime = System.currentTimeMillis();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        long creatingListTime = endTime - startTime;
        double[] inputData = {40.0, -82.0, 10};
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        if (!dummy) {
            inputData = getInputData();
            inputCoordinates[0] = inputData[0];
            inputCoordinates[1] = inputData[1];
            inputRadius = inputData[2];
        }
        ArrayList<Business> resultList = new ArrayList<>();
        startTime = System.currentTimeMillis();
		for (Business business: allData) {
			double currentDistance = Distance.distance(business.getCoordinates()[0], business.getCoordinates()[1], inputCoordinates[0], inputCoordinates[1]);
			if (currentDistance < inputRadius) {
                resultList.add(business); 
			}
		}
        endTime = System.currentTimeMillis();
        long searchingListTime = endTime - startTime;
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create list: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the list: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
        if (print) {
            for (Business business: resultList) {
                business.printBusiness(inputCoordinates);
            }
        }   
	}

    public static void spacePartitioning(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        KDTree kdt = LoadData.getKDTree();
        long endTime = System.currentTimeMillis();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        long creatingListTime = endTime - startTime;
        double[] inputData = {40.0, -82.0, 10};
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        if (!dummy) {
            inputData = getInputData();
            inputCoordinates[0] = inputData[0];
            inputCoordinates[1] = inputData[1];
            inputRadius = inputData[2];
        }
        startTime = System.currentTimeMillis();
        List<KDNode> kdNodes = kdt.searchKdTree(inputCoordinates, inputRadius, 0, kdt.Root);
        endTime = System.currentTimeMillis();
        long searchingListTime = endTime - startTime;
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create list: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the list: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KDNode node = kdNodes.get(i);
                node.x.printBusiness(inputCoordinates);
            }
        }
    }

    public static void kdTreePresort(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        KdNodePresort root = LoadData.getRootKDTreePresort(n);
        long endTime = System.currentTimeMillis();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        long creatingListTime = endTime - startTime;
        double[] inputData = {40.0, -82.0, 10};
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        if (!dummy) {
            inputData = getInputData();
            inputCoordinates[0] = inputData[0];
            inputCoordinates[1] = inputData[1];
            inputRadius = inputData[2];
        }
        startTime = System.currentTimeMillis();
        List<KdNodePresort> kdNodes = root.searchKdTree(inputCoordinates, inputRadius, 0);
        endTime = System.currentTimeMillis();
        long searchingListTime = endTime - startTime;
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create list: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the list: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KdNodePresort node = kdNodes.get(i);
                node.business.printBusiness(inputCoordinates);
            }
        }
    }

    public static void kdTreePartition(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        KdNodePartition root = LoadData.getRootKDTreePartition();
        long endTime = System.currentTimeMillis();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        long creatingListTime = endTime - startTime;
        double[] inputData = {40.0, -82.0, 10};
        double[] inputCoordinates = {inputData[0], inputData[1]};
        double inputRadius = inputData[2];
        if (!dummy) {
            inputData = getInputData();
            inputCoordinates[0] = inputData[0];
            inputCoordinates[1] = inputData[1];
            inputRadius = inputData[2];
        }
        startTime = System.currentTimeMillis();
        List<KdNodePartition> kdNodes = root.searchKdTree(inputCoordinates, inputRadius, 0);
        endTime = System.currentTimeMillis();
        long searchingListTime = endTime - startTime;
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create list: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the list: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KdNodePartition node = kdNodes.get(i);
                node.business.printBusiness(inputCoordinates);
            }
        }
    }
 
	public static void main(String[] args) {
		SpringApplication.run(Cs201ProjectApplication.class, args);
        int[] nList = {10, 100, 1000, 10000, 100000, 1000000};
        System.out.println("Number: Algorithm");
        System.out.println("1: Linear Search");
        System.out.println("2: Space Partioning using KD Tree");
        System.out.println("3: Space Partioning using Balanced KD Tree- Partition");
        System.out.println("4: Space Partioning using Balanced KD Tree- Presort");
        Scanner scanner = new Scanner(System.in);
        boolean validNumber = false;
        int algorthimNumber = 1;
        while (validNumber == false) {
            try {
                System.out.println("Enter the algorithm number:");
                algorthimNumber = Integer.parseInt(scanner.nextLine());
                if (algorthimNumber == 1) {
                    validNumber = true;
                    for (int n: nList) {
                        linearSearch(false,  true, n);
                    }
                } else if (algorthimNumber == 2) {
                    validNumber = true;
                    for (int n: nList) {
                        spacePartitioning(false,  true, n);
                    }
                } else if (algorthimNumber == 3) {
                    validNumber = true;
                    for (int n: nList) {
                        kdTreePartition(false,  true, n);
                    }
                } else if (algorthimNumber == 4) {
                    validNumber = true;
                    for (int n: nList) {
                        kdTreePresort(false,  true, n);
                    }
                } else if (algorthimNumber == 5) {
                    validNumber = true;
                    appendJson(1000010 - 160585);
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

