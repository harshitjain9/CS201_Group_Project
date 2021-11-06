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
import com.example.demo.VPT.DistFuncImpl;
import com.example.demo.VPT.VPTree;
import com.github.javafaker.Faker;
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
		ArrayList<Business> allData = LoadData.getUnsortedList(n);
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
        if (print) {
            for (Business business: resultList) {
                business.printBusiness(inputCoordinates);
            }
        }  
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create list: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the list: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
 
	}

    public static void spacePartitioning(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        KDTree kdt = LoadData.getKDTree(n);
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
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KDNode node = kdNodes.get(i);
                node.x.printBusiness(inputCoordinates);
            }
        }
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create tree: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the tree: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));

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
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KdNodePresort node = kdNodes.get(i);
                node.business.printBusiness(inputCoordinates);
            }
        }
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create tree: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the tree: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));

    }

    public static void kdTreePartition(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        KdNodePartition root = LoadData.getRootKDTreePartition(n);
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
        if (print) {
            for (int i = 0; i < kdNodes.size(); i++) {
                KdNodePartition node = kdNodes.get(i);
                node.business.printBusiness(inputCoordinates);
            }
        }
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create tree: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the tree: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));

    }

    public static void vpTree(boolean print, boolean dummy, int n) {
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();
        ArrayList<Business> result = LoadData.getUnsortedList(160585);
        VPTree<Business,Business> vpTree = new VPTree<>(new DistFuncImpl());
        for (Business each:result) {
            vpTree.add(each);
        }
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
        Business current = new Business("current", "current address", inputCoordinates);
        startTime = System.currentTimeMillis();
        final List<Business> nearestShops_ = vpTree.getAllWithinDistance(current, inputRadius);
        endTime = System.currentTimeMillis();
        long searchingListTime = endTime - startTime;
        if (print) {
            for (Business each:nearestShops_) {
                each.printBusiness(current.getCoordinates());
            }
        }
        System.out.println("n = " + String.valueOf(n));
        System.out.println("Time to create tree: " + String.valueOf(creatingListTime));
        System.out.println("Time to search the tree: " + String.valueOf(searchingListTime));
        System.out.println("Memory used: " + String.valueOf(actualMemUsed));
    }
 
	public static void main(String[] args) {
		SpringApplication.run(Cs201ProjectApplication.class, args);
        // int[] nList = {10, 100, 1000, 10000, 100000, 1000000};
        int[] nList = {1000000};
        System.out.println("Number: Data Structure");
        System.out.println("1: Unsorted List (Linear Search/Brute Force)");
        System.out.println("2: Normal, unbalanced KD Tree");
        System.out.println("3: Balanced KD Tree (Recursive Partitioning)");
        System.out.println("4: Balanced KD Tree (Presort)");
        System.out.println("5: Vantage Point Tree");
        Scanner scanner = new Scanner(System.in);
        boolean validNumber = false;
        int algorthimNumber = 1;
        while (validNumber == false) {
            try {
                System.out.println("Enter the algorithm number:");
                algorthimNumber = Integer.parseInt(scanner.nextLine());
                int dummy = -1;
                while (dummy!= 0 && dummy!= 1) {
                    System.out.println("Do you want to test with dummy input coordinates? Enter 1 for Yes and 0 for No:");
                    dummy = Integer.parseInt(scanner.nextLine());
                }
                boolean dummyBool = false;
                if (dummy == 1) {
                    dummyBool = true;
                }
                int print = -1;
                while (print!= 0 && print!= 1) {
                    System.out.println("Do you want to print the businesses? Enter 1 for Yes and 0 for No:");
                    print = Integer.parseInt(scanner.nextLine());
                }
                boolean printBool = false;
                if (print == 1) {
                    printBool = true;
                }
                if (algorthimNumber == 1) {
                    validNumber = true;
                    for (int n: nList) {
                        linearSearch(printBool,  dummyBool, n);
                    }
                } else if (algorthimNumber == 2) {
                    validNumber = true;
                    for (int n: nList) {
                        spacePartitioning(printBool,  dummyBool, n);
                    }
                } else if (algorthimNumber == 3) {
                    validNumber = true;
                    for (int n: nList) {
                        kdTreePartition(printBool,  dummyBool, n);
                    }
                } else if (algorthimNumber == 4) {
                    validNumber = true;
                    for (int n: nList) {
                        kdTreePresort(printBool,  dummyBool, n);
                    }
                } else if (algorthimNumber == 5) {
                    validNumber = true;
                    for (int n: nList) {
                        vpTree(printBool,  dummyBool, n);
                    }
                } 
                // else if (algorthimNumber == 6) {
                //     validNumber = true;
                //     appendJson(1000010 - 160585);
                // } 
                else {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            } 
        } 
        scanner.close();
	}	

}

