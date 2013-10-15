package com.ml.bus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Category;
import com.ml.bus.model.Cluster;
import com.ml.bus.service.CategoryService;
import com.ml.bus.service.ClusterService;
import com.ml.bus.service.MemoryService;
import com.ml.util.Constants;

@Controller
@RequestMapping(value = "/category")
public class CategoryController {

	    @Autowired
	    CategoryService categoryService;
	    @Autowired
	    private ClusterService clusterService;
	    @Autowired
	    private MemoryService memoryService;
	    
	    @RequestMapping(value = "", method = RequestMethod.GET)
	    public @ResponseBody List<Category> get() {
	    	
	    	return categoryService.findAll();
	    	
	    }
	    
	    @RequestMapping(value = "show", method = RequestMethod.GET)
	    public @ResponseBody List<Map<String, Object>> showCluster(
	    		@RequestParam(value = "isClusterd", required = false) String isClusterd) throws Exception {
	    	long start = System.currentTimeMillis();
	    	List<Category> categoryList = categoryService.findAll();
	    	List<Cluster> clusterList = clusterService.findAll();
	    	Map<String, List<Cluster>> categoryClusters = initCategoryClusters(clusterList);
	    		
	    	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    	for(Category category: categoryList) {
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		map.put("categoryId", category.getId());
	    		map.put("categoryName", category.getName());
	    		
	    		List<Cluster> clusters = categoryClusters.get(category.getId());
	    		if(clusters == null) {
	    			clusters = new ArrayList<Cluster>(1);
	    		}
	    		if(isClusterd != null && isClusterd.equalsIgnoreCase("true")) {
    				clusters = getClustersByLimitSize(clusters);
	    		}
	    		map.put("clusters", clusters);
	    		result.add(map);
	    	}
			long end = System.currentTimeMillis();
			System.out.println("耗时：" + (end -start));
			
			return result;
	    }
	    
	    private List<Cluster> getClustersByLimitSize(List<Cluster> clusterList) {
	    	List<Cluster> clusters = new ArrayList<Cluster>();
	    	for(Cluster cluster: clusterList) {
	    		if(cluster.getClusterNum() >= Constants.clusterKvalue){
	    			clusters.add(cluster);
	    		}
	    	}
	    	return clusters;
		}
	    
	    private Map<String, List<Cluster>> initCategoryClusters(List<Cluster> clusterList) {
	    	Map<String, List<Cluster>> categoryClusters = new HashMap<String, List<Cluster>>();
			for(Cluster cluster: clusterList) {
	    		String categoryId = cluster.getCategoryId();
	    		List<Cluster> clusters = categoryClusters.get(categoryId);
	    		if(clusters == null) {
	    			clusters = new ArrayList<Cluster>();
	    		}
	    		clusters.add(cluster);
	    		categoryClusters.put(categoryId, clusters);
	    	}
			return categoryClusters;
		}
}
