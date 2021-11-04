package com.example.demo;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;


import org.springframework.boot.SpringApplication;

import com.example.demo.KD.*;
import com.example.demo.LoadData.*;


//import org.springframework.boot.SpringApplication;

//@SpringBootApplication
public class Cs201ProjectApplication {

	public static double distance(double lat1, double lon1, double lat2, double lon2) {

		// The math module contains a function
		// named toRadians which converts from
		// degrees to radians.
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		// Haversine formula
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

		double c = 2 * Math.asin(Math.sqrt(a));

		// Radius of earth in kilometers. Use 3956
		// for miles
		double r = 6371;

		// calculate the result
		return (c * r);
	}

	public static void linearSearch() {
		// ArrayList<Business> allData = new ArrayList<>();
		// JSONParser parser = new JSONParser();
		// String line;
		// try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
		// 	while ((line = reader.readLine()) != null) {
		// 		JSONObject jsonObject = (JSONObject) parser.parse(line);
		// 		double latitude = (double) jsonObject.get("latitude");
		// 		double longitude = (double) jsonObject.get("longitude");
		// 		String name = (String) jsonObject.get("name");
		// 		String address = (String) jsonObject.get("address");
		// 		// String categoriesString = (String) jsonObject.get("categories");
		// 		// String[] categoriesArray = {};
		// 		// try {
		// 		// 	categoriesArray = categoriesString.split(", ");
		// 		// } catch (NullPointerException e) {
		// 		// 	continue;
		// 		// }
		// 		allData.add(new Business(name, address, latitude, longitude));
		// 	}
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// } catch (ParseException e) {
		// 	e.printStackTrace();
		// }

		ArrayList<Business> allData = LoadData.getData();

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
		//System.out.println("Enter the category: ");
		//String inputCategory = scanner.nextLine();

		double minimumDistance = Double.MAX_VALUE;
		String resultName = "";
		String resultAddress = "";
		for (Business each:allData) {
			double currentDistance = distance(each.getLatitude(), each.getLongitude(), inputLatitude, inputLongitude);
			//boolean containsCategory = Arrays.stream(each.getCategories()).anyMatch(inputCategory::equals);
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				resultName = each.getName();
				resultAddress = each.getAddr();
			}
		}
		System.out.println("Name: " + resultName);
		System.out.println("Address: " + resultAddress);
		System.out.println("Minimum Distance: " + String.valueOf(minimumDistance));
		scanner.close();
	}

    public static void spacePartitioning() {
        Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the category: ");
		String inputCategory = scanner.nextLine();
		JSONParser parser = new JSONParser();
		String line;
		int numpoints = 160585;
		KDTree kdt = new KDTree(numpoints);

		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				// String categoriesString = (String) jsonObject.get("categories");
				// String[] categoriesArray = {};
				// try {
				// 	categoriesArray = categoriesString.split(", ");
				// } catch (NullPointerException e) {
				// 	continue;
				// }
				// boolean containsCategory = Arrays.stream(categoriesArray).anyMatch(inputCategory::equals);
				// if (containsCategory) {
					
				//     kdt.add(x);
				// }
                Business business = new Business(name, address, latitude, longitude));
				kdt.add(business);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		double s[] = { inputLatitude, inputLongitude };
        KDNode kdn = kdt.find_nearest(s);
        System.out.println("The nearest neighbor is: ");
        System.out.println("(" + kdn.x[0] + " , " + kdn.x[1] + ")");
        double nearestDistance = kdt.find_nearest_distance();
        System.out.println("The nearest distance is: " + String.valueOf(nearestDistance));
        
    }
 
	public static void main(String[] args) {
		SpringApplication.run(Cs201ProjectApplication.class, args);
		linearSearch();
	}	

}

