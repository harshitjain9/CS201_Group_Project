package com.example.demo;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import com.example.demo.KD.KDTree;
import com.example.demo.KD.KdNodePresort;

public class LoadData {
	private static String path = "./yelp_academic_dataset_business.json";
    public static ArrayList<Business> getUnsortedList(){
		System.out.println("Loading data in unsorted list..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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

	public static KdNodePresort getRootKDTreePresort(){
		System.out.println("Loading data in KD Tree..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
		KdNodePresort root = KdNodePresort.createKdTree(allData);
		System.out.println("Data loaded in KD Tree");
        return root;
    }

	public static KdNodePresort getRootKDTreePresort(int n){
		System.out.println("Loading data in KD Tree..");
        ArrayList<Business> allData = new ArrayList<>();
		JSONParser parser = new JSONParser();
		String line;
		int walk = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			while (walk < n && (line = reader.readLine()) != null) {
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
}

