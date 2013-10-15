package com.ml.bus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ml.bus.model.Cluster;
import com.ml.bus.model.News;
import com.ml.util.Constants;


@Service
public class MemoryService {
	
	Map<String, String> categoryUrl;
	
	@PostConstruct 
    public void init(){ 
		initCategoryUrl();
    }
	
	public Map<String, Integer> getNewsStats(List<News> newsList, List<Cluster> clusterList) {
		Map<String, Integer> stats = new HashMap<String, Integer>();
		stats.put("preTotalNewsSize", newsList.size());

		List<String> unecessaryCluster = getUnecessaryCluster(clusterList);
		stats.put("unecessaryClusterSize", unecessaryCluster.size());
		
		List<News> categoryList = removeUncategoryNews(newsList);
		stats.put("categoryNewsSize", categoryList.size());
		stats.put("uncategoryNewsSize", newsList.size() - categoryList.size());
		
		int unClusterRightNewsSize = calculateNews(categoryList);
		stats.put("unClusterRightNewsSize", unClusterRightNewsSize);

		List<News> moreKClusterList = removeLessKClusterNews(categoryList, unecessaryCluster);
		stats.put("moreKClusterNewsSize", moreKClusterList.size());
		stats.put("lessKClusterNewsSize", categoryList.size() - moreKClusterList.size());
		
		int rightSize = calculateNews(moreKClusterList);
		stats.put("finalTotalNewsSize", moreKClusterList.size());
		stats.put("rightNewsSize", rightSize);
		
		return stats;
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
	
	private List<String> getUnecessaryCluster(List<Cluster> clusterList) {
		//get less than k's cluster
		List<String> unecessaryCluster = new ArrayList<String>();
		for(Cluster cluster: clusterList) {
			if(cluster.getClusterNum() < Constants.clusterKvalue) {
				unecessaryCluster.add(cluster.getClusterId());
			}
		}
		return unecessaryCluster;
	}
	
	private List<News> removeUncategoryNews(List<News> newsList) {
		List<News> list = new ArrayList<News>();
		for(News news: newsList) {
			//remove uncategory news
			String categoryId = news.getCategoryId();
			String originCategory = categoryUrl.get(news.getOriginalCategory());
			if(categoryId == null || originCategory == null ||
					originCategory.equals("")) {
				continue;
			}
			list.add(news);
		}
		return list;
	}
	
	private List<News> removeLessKClusterNews(List<News> newsList, List<String> uneccessaryCluster) {
		List<News> list = new ArrayList<News>();
		for(News news: newsList) {
			//remove less than k's cluster news
			String clusterId = news.getClusterId();
			if(uneccessaryCluster.contains(clusterId)) {
				continue;
			}
			list.add(news);
		}
		return list;
	}
	
	private int calculateNews(List<News> newsList) {
		int i = 0;
		for(News news: newsList) {
			String categoryId = news.getCategoryId();
			String originCategory = categoryUrl.get(news.getOriginalCategory());
			if(originCategory.equals(categoryId)){
				i++;
			}
		}
		return i;
	}
}
