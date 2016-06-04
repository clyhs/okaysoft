package org.okaysoft.core.criteria;

public enum Sequence {
	
	DESC("desc"),ASC("asc");
    private Sequence(String value){
        this.value=value;
    }
    private String value;
    public String getValue(){
        return this.value;
    }

}
