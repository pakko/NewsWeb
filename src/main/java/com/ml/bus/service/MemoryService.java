package com.ml.bus.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.INewsDAO;
import com.ml.bus.model.News;


@Service
public class MemoryService {
	
	@Autowired
	INewsDAO newsDAO;

	Map<String, String> categoryUrl;
	
	@PostConstruct 
    public void init(){ 
		long start = System.currentTimeMillis();
		
		List<News> newsList = newsDAO.findAll();
		
		initCategoryUrl();
		calculateNews(newsList);
		
		long end = System.currentTimeMillis();
		
		System.out.println("Complete init memory datasets. Classified News size: " + newsList.size());
		System.out.println("Cost time:" + (end - start) );
		
		newsList.clear();
		newsList = null;
    }

	private void calculateNews(List<News> newsList) {
		double i = 0;
		double j = 0;
		for(News news: newsList) {
			String categoryId = news.getCategoryId();
			
			String originCategory = categoryUrl.get(news.getOriginalCategory());
			if(categoryId == null || originCategory == null ||
					originCategory.equals("")) {
				j++;
				continue;
			}
				
			if(originCategory.equals(categoryId)){
				i++;
			}
		}
		System.out.println("Accuracy:" + (i / newsList.size()) + "right size: " + i + ", miss size: " + j );
		
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
	
}
