package org.okaysoft.core.criteria;

public class Property {
	
	private static int source;
	
	private String name;
	
	private Object value;
	
	private int seq;
	
	public Property(){}
	
	public Property(String name,Object value){
		this.name = name;
		this.value =value;
		seq=source++;
	}
	
	public String getNameParameter() {
        return name.replace(".", "_") + "_" + seq;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	

}
