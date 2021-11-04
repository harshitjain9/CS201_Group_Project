package com.example.demo;
import java.util.*;
import java.lang.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class LoadData {
    public static ArrayList<Business> getData(){
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
				// String categoriesString = (String) jsonObject.get("categories");
				// String[] categoriesArray = {};
				// try {
				// 	categoriesArray = categoriesString.split(", ");
				// } catch (NullPointerException e) {
				// 	continue;
				// }
				allData.add(new Business(name, address, latitude, longitude));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

        return allData;

    }
}
