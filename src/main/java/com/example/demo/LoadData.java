package com.example.demo;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import com.example.demo.KD.KDTree;

public class LoadData {

    public static ArrayList<Business> getUnsortedList(){
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Data loaded in unsorted list");
        return allData;
    }

	public static KDTree getKDTree(){
		System.out.println("Loading data in KD Tree..");
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
				double[] coordinates = {latitude, longitude};
                Business business = new Business(name, address, coordinates);
				kdt.add(business);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Data loaded in KD Tree");
        return kdt;
    }
}

