package org.okaysoft.core.criteria;

import java.util.ArrayList;
import java.util.List;








public class PropertyCriteria {

	
	private String collection;
    private String object;
    private Criteria criteria = Criteria.and;
    
    private List<PropertyEditor> propertyEditors = new ArrayList<PropertyEditor>();
    
    public PropertyCriteria(){}
    
    public PropertyCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
    
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public Criteria getCriteria() {
		return criteria;
	}
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	
	
	public List<PropertyEditor> getPropertyEditors() {
        return propertyEditors;
    }

    public void addPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditors.add(propertyEditor);
    }
    
    
}
