package com.ml.bus.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Cluster;
import com.ml.bus.model.News;
import com.ml.bus.service.CategoryService;
import com.ml.bus.service.ClusterService;
import com.ml.bus.service.MemoryService;
import com.ml.bus.service.NewsService;

@Controller
@RequestMapping(value = "/stats")
public class StatsController {

	    @Autowired
	    CategoryService categoryService;
	    @Autowired
	    private ClusterService clusterService;
	    @Autowired
	    private NewsService newsService;
	    @Autowired
	    private MemoryService memoryService;
	    
	    @RequestMapping(value = "", method = RequestMethod.GET)
	    public @ResponseBody Map<String, Integer> get() {
	    	long start = System.currentTimeMillis();
	    	List<News> newsList = newsService.findAll();
			List<Cluster> clusterList = clusterService.findAll();
			Map<String, Integer> stats = memoryService.getNewsStats(newsList, clusterList);
			
			int totalNewsSize = stats.get("totalNewsSize");
			int lessKClusterSize = stats.get("lessKClusterSize");
			int categoryNewsSize = stats.get("categoryNewsSize");
			int unCategoryNewsSize = stats.get("unCategoryNewsSize");
			int rightCategoryNewsSize = stats.get("rightCategoryNewsSize");
			int moreKClusterNewsSize = stats.get("moreKClusterNewsSize");
			int lessKClusterNewsSize = stats.get("lessKClusterNewsSize");
			int rightClusterNewsSize = stats.get("rightClusterNewsSize");
			
			long end = System.currentTimeMillis();
			
			StringBuffer sb = new StringBuffer();
			sb.append("---------------------------------------------------------------------------------------\n");
			sb.append("Total News size: " + totalNewsSize + ", After Category News Size: " + categoryNewsSize + ", After Cluster News Size: " + moreKClusterNewsSize + "\n");
			sb.append("Less Than K Cluster Size: " + lessKClusterSize + ", UnCategory Size: " + unCategoryNewsSize + ", Less Than K Cluster News Size: " + lessKClusterNewsSize + "\n");
			sb.append("After Category Accuracy: " + (rightCategoryNewsSize * 1.0 / categoryNewsSize) + ", right size: " + rightCategoryNewsSize + "\n");
			sb.append("After Cluster Accuracy: " + (rightClusterNewsSize * 1.0 / moreKClusterNewsSize) + ", right size: " + rightClusterNewsSize + "\n");
			sb.append("Cost time:" + (end - start) + "\n");
			sb.append("---------------------------------------------------------------------------------------\n");
			System.out.println(sb.toString());
			return stats;
	    	
	    }
}
