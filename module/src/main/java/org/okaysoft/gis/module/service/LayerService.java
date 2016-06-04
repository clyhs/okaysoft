package org.okaysoft.gis.module.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.okaysoft.core.log.OkayLogger;
import org.opengis.filter.Filter;
import org.springframework.stereotype.Service;

@Service
public class LayerService {

	protected static final OkayLogger log  = new OkayLogger(LayerService.class);
	
	@Resource(name="catalog")
	private Catalog catalog;
	
	
	public String getLayers(){
		
		StringBuilder sb = new StringBuilder();
		
		Iterator<LayerInfo> layerInfos =  catalog.list(LayerInfo.class, Filter.INCLUDE, 0, 2, null);
	    
	    	
	    while(layerInfos.hasNext()){
	    	LayerInfo li = layerInfos.next();
	    	sb.append(li.getName());
	    }
		
	    
	    
		return sb.toString();
	}
	
	
}
