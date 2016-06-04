package org.okaysoft.gis.core.action;

import javax.annotation.Resource;
import javax.persistence.MappedSuperclass;

import org.geoserver.catalog.Catalog;
import org.okaysoft.core.log.OkayLogger;

@MappedSuperclass
public abstract class GeoAction {
	
	protected final OkayLogger log = new OkayLogger(getClass());
	
	@Resource(name="catalog")
	protected Catalog catalog;
	
	public String execute() {
		return null;
	}
	
	

}
