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
			
			int preTotalNewsSize = stats.get("preTotalNewsSize");
			int unecessaryClusterSize = stats.get("unecessaryClusterSize");
			int categoryNewsSize = stats.get("categoryNewsSize");
			int uncategoryNewsSize = stats.get("uncategoryNewsSize");
			int unClusterRightNewsSize = stats.get("unClusterRightNewsSize");
			int lessKClusterNewsSize = stats.get("lessKClusterNewsSize");
			int finalTotalNewsSize = stats.get("finalTotalNewsSize");
			int rightNewsSize = stats.get("rightNewsSize");
			
			long end = System.currentTimeMillis();
			
			StringBuffer sb = new StringBuffer();
			sb.append("---------------------------------------------------------------------------------------\n");
			sb.append("Total News size: " + preTotalNewsSize + ", UnCluster Total News Size: " + categoryNewsSize + ", Final Total News Size: " + finalTotalNewsSize + "\n");
			sb.append("Unecessary Cluster Size: " + unecessaryClusterSize + ", UnCategory size: " + uncategoryNewsSize + ", less than K Cluster size: " + lessKClusterNewsSize + "\n");
			sb.append("Before Cluster Accuracy: " + (unClusterRightNewsSize * 1.0 / categoryNewsSize) + ", right size: " + unClusterRightNewsSize + "\n");
			sb.append("Accuracy: " + (rightNewsSize * 1.0 / finalTotalNewsSize) + ", right size: " + rightNewsSize + "\n");
			sb.append("Cost time:" + (end - start) + "\n");
			sb.append("---------------------------------------------------------------------------------------\n");
			System.out.println(sb.toString());
			return stats;
	    	
	    }
}
