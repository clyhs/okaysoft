package org.okaysoft.gis.core.action;

import java.util.Map;

import org.geoserver.catalog.Info;

public abstract class ExtJSGeoAction<T extends Info> extends GeoActionSupport {
	
	protected T model;
	protected Map map = null;
	
	public abstract String query();
	
	
	
	public int getFirstIndex(){
		page = ( start + limit) / limit;
		
		offset = (page - 1)*limit;
		return offset;
	}
	
}
