package com.ml.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.IClusterDAO;
import com.ml.bus.model.Cluster;

@Service
public class ClusterService {
	
	@Autowired
	IClusterDAO clusterDAO;
	
	public void save(Cluster cluster) {
		clusterDAO.save(cluster);
	}
	
	public List<Cluster> findAll() {
		return clusterDAO.findAll();
	}
	
	public List<Cluster> findByCategory(String categoryId) {
		return clusterDAO.findByCategory(categoryId);
	}
	
	public void delete(Cluster cluster) {
		clusterDAO.delete(cluster);
	}
	
}
