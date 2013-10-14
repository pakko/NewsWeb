package com.ml.util;


public class Constants {
	
	public static final String separator = "/";
	public static String currentDir = Constants.class.getClassLoader().getResource("").getPath();
	
	public static final String defaultConfigFile = currentDir + separator + "default.properties";
	public static final String defaultClassCategoryFile = currentDir + separator + "ClassList.txt";
	
	public static final String mybatisDBConfigFile = "dbconf" + separator + "MybatisConfig.xml";

	public static final String newsCollectionName = "news";
	public static final String crawlPatternCollectionName = "crawlPattern";
	public static final String categoryCollectionName = "category";
	public static final String clusterCollectionName = "cluster";

    public static final int clusterKvalue = 10;


}
