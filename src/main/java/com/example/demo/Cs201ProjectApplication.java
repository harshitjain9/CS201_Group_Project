package com.example.demo;

import java.io.*;
import java.util.*;
import java.lang.*;


import org.json.simple.*;
import org.json.simple.parser.*;

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
		double a = Math.pow(Math.sin(dlat / 2), 2)
		+ Math.cos(lat1) * Math.cos(lat2)
		* Math.pow(Math.sin(dlon / 2),2);

		double c = 2 * Math.asin(Math.sqrt(a));

		// Radius of earth in kilometers. Use 3956
		// for miles
		double r = 6371;

		// calculate the result
		return(c * r);
	}


	public static void main(String[] args) {
//		SpringApplication.run(Cs201ProjectApplication.class, args);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the category: ");
		String inputCategory = scanner.nextLine();
		// System.exit(0);
	    JSONParser parser = new JSONParser();
		String line;
		double minimumDistance = Double.MAX_VALUE;
		String resultName = "";
		String resultAddress = "";
		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {

			while ((line = reader.readLine()) != null) {     
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				String categoriesString = (String) jsonObject.get("categories");
				String[] categoriesArray = {};
				try {
					categoriesArray = categoriesString.split(", ");
				} catch (NullPointerException e) {
					continue;
				}
				double currentDistance = distance(latitude, longitude, inputLatitude, inputLongitude);
				boolean containsCategory = Arrays.stream(categoriesArray).anyMatch(inputCategory::equals);
				if ( containsCategory && (currentDistance < minimumDistance)) {
					minimumDistance = currentDistance;
					resultName = name;
					resultAddress = address;
				}

				// System.out.println("Latitude: " + String.valueOf(latitude));
				// System.out.println("Longitude: " + String.valueOf(longitude));
				// System.out.println("Name: " +  name);
				// System.out.println("Address: " + address);
				// System.out.println("Categories: " + Arrays.toString(categoriesArray));
			  }

			System.out.println("Name: " + resultName);
			System.out.println("Address: " + resultAddress);
			System.out.println("Minimum Distance: " + String.valueOf(minimumDistance));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}

// try {
// 	Object obj = parser.parse(new FileReader("./yelp_academic_dataset_business.json"));
	
// //	         JSONObject jsonObject = (JSONObject)obj;
// //	         String name = (String)jsonObject.get("Name");
// //	         String course = (String)jsonObject.get("Course");
// //	         JSONArray subjects = (JSONArray)jsonObject.get("Subjects");
// //	         System.out.println("Name: " + name);
// //	         System.out.println("Course: " + course);
// //	         System.out.println("Subjects:");
// //	         Iterator iterator = subjects.iterator();
// //	         while (iterator.hasNext()) {
// //	            System.out.println(iterator.next());
// //	         }
//  } catch(Exception e) {
// 	e.printStackTrace();
//  }
