package org.okaysoft.gis.core.action;

public abstract class GeoActionSupport extends GeoAction {
	
	protected int start=-1;
	protected int limit=-1;
	
	protected int page;
	
	protected int offset = 0;
	
	
	public int getOffset() {
		return offset;
	}
	
	

	public GeoActionSupport(){
		if(start==-1){
			start = 0;
		}
		if(limit==-1){
			limit = 17;
		}
		
	}
	
	
	
	public int getPage() {
		return page;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	

}
