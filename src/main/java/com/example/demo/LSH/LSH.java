package com.example.demo.LSH;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.demo.Business;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import info.debatty.java.lsh.LSHMinHash;

public class LSH {
    public void run() {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the latitude value: ");
		double inputLatitude = Double.parseDouble(scanner.nextLine());
		System.out.println("Enter the longitude value: ");
		double inputLongitude = Double.parseDouble(scanner.nextLine());

        ArrayList<double[]> vectors = new ArrayList<>();
        for (Business each:allData) {
            double[] vector = new double[] {each.getCoordinates()[0], each.getCoordinates()[1]};
            vectors.add(vector);
        }

        int sizeOfVectors = 2; // all input vectors should have equal size
        int numberOfBuckets = (int) Math.sqrt(vectors.size());
        int stages = 4; // no. of iterations of LSH for calculating hashes
        LSHMinHash lsh = new LSHMinHash(stages, numberOfBuckets, sizeOfVectors);

        ArrayList<int[]> result = new ArrayList<>();
        for (double[] each:vectors) {
            // need some hashing function
        }
        // int[] firstHash = lsh.hash(vector1);
        // int[] secondHash = lsh.hash(vector2);
        // int[] thirdHash = lsh.hash(vector3);

        // System.out.println(Arrays.toString(firstHash));
        // System.out.println(Arrays.toString(secondHash));
        // System.out.println(Arrays.toString(thirdHash));
    }
}
