package com.example.demo;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import com.example.demo.KD.KDTree;
import com.example.demo.KD.KdNodePresort;
import com.example.demo.KD.KdNodePartition;

public class LoadData {

    public static ArrayList<Business> getUnsortedList(int n){
		System.out.println("Loading data in unsorted list..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		int walk = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				double[] coordinates = {latitude, longitude};
				allData.add(new Business(name, address, coordinates));
				walk++;
				if (walk == n) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Data loaded in unsorted list");
        return allData;
    }

	public static KDTree getKDTree(int n){
		System.out.println("Loading data in KD Tree..");
		JSONParser parser = new JSONParser();
		String line;
		KDTree kdt = new KDTree(n);
		int walk = 0;
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
				walk++;
				if (walk == n) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Data loaded in KD Tree");
        return kdt;
    }

	public static KdNodePresort getRootKDTreePresort(int n){
		System.out.println("Loading data in KD Tree..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		int walk = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				double[] coordinates = {latitude, longitude};
				allData.add(new Business(name, address, coordinates));
				walk++;
				if (walk == n) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		KdNodePresort root = KdNodePresort.createKdTree(allData);
		System.out.println("Data loaded in KD Tree");
        return root;
    }

	public static KdNodePartition getRootKDTreePartition(int n){
		System.out.println("Loading data in KD Tree..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		int walk = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("./yelp_academic_dataset_business.json"))) {
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				double latitude = (double) jsonObject.get("latitude");
				double longitude = (double) jsonObject.get("longitude");
				String name = (String) jsonObject.get("name");
				String address = (String) jsonObject.get("address");
				double[] coordinates = {latitude, longitude};
				allData.add(new Business(name, address, coordinates));
				walk++;
				if (walk == n) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		KdNodePartition root = KdNodePartition.createKdTree(allData);
		System.out.println("Data loaded in KD Tree");
        return root;
    }
}

