package com.ml.bus.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.ml.bus.model.Category;
import com.ml.db.MongoDB;
import com.ml.util.Constants;

public class InitDatasetsService {
	private MongoDB mongodb;
	
	public InitDatasetsService(MongoDB mongodb) {
		this.mongodb = mongodb;
	}
	
	private String getText(String filePath, String token) throws Exception {
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(
				filePath), "UTF8");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();

		while ((aline = reader.readLine()) != null) {
			sb.append(aline.trim() + token);
		}
		isReader.close();
		reader.close();
		return sb.toString();
	}
	
	private void saveCategory() throws Exception {
		String filePath = Constants.defaultClassCategoryFile;
		String text = getText(filePath, " ");
		String[] lines = text.split(" ");
		for(String line: lines) {
			String[] l = line.split("\t");
			Category category = new Category(l[0], l[1]);
			mongodb.save(category, Constants.categoryCollectionName);
		}
		
	}
	
	public static void main(String[] args) throws Exception  {
		String confFile = Constants.defaultConfigFile;
		Properties props = new Properties();
		props.load(new FileInputStream(confFile));
		MongoDB mongodb = new MongoDB(props);
		
		InitDatasetsService mk = new InitDatasetsService(mongodb);
		mk.saveCategory();
	}
}
