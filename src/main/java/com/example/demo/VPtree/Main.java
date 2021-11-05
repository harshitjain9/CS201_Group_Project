package com.example.demo.VPtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.demo.VPtree.jvptree.DistFuncImpl;
import com.eatthepath.jvptree.VPTree;

import org.json.simple.*;
import org.json.simple.parser.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //System.out.println(LoadData());
        ArrayList<Business> result = LoadData();
        // for (double[] each:result) {
        //     System.out.println("" + each[0] + " " + each[1]);
        // }
        //System.out.println(rs);
        Business current = getInputData();
        VPTree<Business,Business> vpTree = new VPTree<>(new DistFuncImpl());
        for (Business each:result) {
            vpTree.add(each);
        }
        final List<Business> nearestShops = vpTree.getNearestNeighbors(current, 5);
        for (Business each:nearestShops) {
            each.printBusiness();
        }
    }

    public static ArrayList<Business> LoadData() {
        System.out.println("Loading data in unsorted list..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				double[] coordinates = {latitude, longitude};
				allData.add(new Business(name, address, coordinates));
                //allData.add(coordinates);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        System.out.println("Data loaded in unsorted list");
        return allData;
    }

    public static Business getInputData() {
        Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
        //System.out.println("Enter the radius: ");
		//double inputRadius = Double.parseDouble(scanner.nextLine());
        double[] inputData_ = {inputLatitude, inputLongitude};
        Business inputData = new Business("Current", "CurrentAddress", inputData_);
        scanner.close();
        return inputData;
    }
}
