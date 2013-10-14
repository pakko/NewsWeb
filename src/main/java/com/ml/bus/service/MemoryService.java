package com.ml.bus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.IClusterDAO;
import com.ml.bus.dao.INewsDAO;
import com.ml.bus.model.Cluster;
import com.ml.bus.model.News;
import com.ml.util.Constants;


@Service
public class MemoryService {
	
	@Autowired
	INewsDAO newsDAO;
	@Autowired
	IClusterDAO clusterDAO;
	
	Map<String, String> categoryUrl;
	int unCategorySize;
	int lessKClusterSize;
	int rightSize;
	
	private Map<String, List<Cluster>> categoryClusters;
	
	@PostConstruct 
    public void init(){ 
		long start = System.currentTimeMillis();
		
		List<News> newsList = newsDAO.findAll();
		List<Cluster> clusterList = clusterDAO.findAll();
    	
		//get category's clusters
    	initCategoryClusters(clusterList);
    	
		//calculate the right accuracy
		initCategoryUrl();
		List<String> uneccessaryCluster = getUneccessaryCluster(clusterList);
		List<News> list = getNeededNews(newsList, uneccessaryCluster);
		calculateNews(list);
		
		long end = System.currentTimeMillis();
		
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("Accuracy: " + (rightSize / list.size()) + ", right size: " + rightSize);
		System.out.println("UnCategory size: " + unCategorySize + ", less than K Cluster size: " + lessKClusterSize );
		System.out.println("Complete init memory datasets. Total News size: " + newsList.size());
		System.out.println("Cost time:" + (end - start) );
		System.out.println("---------------------------------------------------------------------------------------");

		newsList.clear();
		newsList = null;
    }

	private void initCategoryUrl() {
		categoryUrl = new HashMap<String, String>();
		categoryUrl.put("auto", "C000007");
		categoryUrl.put("business", "C000008");
		categoryUrl.put("it", "C000010");
		categoryUrl.put("health", "C000013");
		categoryUrl.put("sports", "C000014");
		categoryUrl.put("travel", "C000016");
		categoryUrl.put("learning", "C000020");
		categoryUrl.put("job", "C000022");
		categoryUrl.put("cul", "C000023");
		categoryUrl.put("mil", "C000024");
		
	}
	
	private List<String> getUneccessaryCluster(List<Cluster> clusterList) {
		//get less than k's cluster
		List<String> uneccessaryCluster = new ArrayList<String>();
		for(Cluster cluster: clusterList) {
			if(cluster.getClusterNum() < Constants.clusterKvalue) {
				uneccessaryCluster.add(cluster.getClusterId());
			}
		}
		return uneccessaryCluster;
	}
	
	private List<News> getNeededNews(List<News> newsList, List<String> uneccessaryCluster) {
		unCategorySize = 0;
		lessKClusterSize = 0;
		List<News> list = new ArrayList<News>();
		for(News news: newsList) {
			//remove uncategory news
			String categoryId = news.getCategoryId();
			String originCategory = categoryUrl.get(news.getOriginalCategory());
			if(categoryId == null || originCategory == null ||
					originCategory.equals("")) {
				unCategorySize++;
				continue;
			}
			
			//remove less than k's cluster news
			String clusterId = news.getClusterId();
			if(uneccessaryCluster.contains(clusterId)) {
				lessKClusterSize++;
				continue;
			}
			list.add(news);
		}
		return list;
	}
	
	private void calculateNews(List<News> newsList) {
		rightSize = 0;
		for(News news: newsList) {
			String categoryId = news.getCategoryId();
			String originCategory = categoryUrl.get(news.getOriginalCategory());
			if(originCategory.equals(categoryId)){
				rightSize++;
			}
		}
	}

	private void initCategoryClusters(List<Cluster> clusterList) {
		categoryClusters = new HashMap<String, List<Cluster>>();
		for(Cluster cluster: clusterList) {
    		String categoryId = cluster.getCategoryId();
    		List<Cluster> clusters = categoryClusters.get(categoryId);
    		if(clusters == null) {
    			clusters = new ArrayList<Cluster>();
    		}
    		clusters.add(cluster);
    	}
	}
	
	

	public Map<String, List<Cluster>> getCategoryClusters() {
		return categoryClusters;
	}

	public void setCategoryClusters(Map<String, List<Cluster>> categoryClusters) {
		this.categoryClusters = categoryClusters;
	}
	
}
