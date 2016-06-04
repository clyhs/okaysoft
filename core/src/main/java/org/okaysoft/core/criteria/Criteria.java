package org.okaysoft.core.criteria;

public enum Criteria {
	
	and("and"),or("or");
	private Criteria(String symbol){
		this.symbol = symbol;
	}
	
	private String symbol;

	public String getSymbol() {
		return symbol;
	}
	
	

}
