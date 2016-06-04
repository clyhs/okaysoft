package org.okaysoft.core.dao;

import java.util.List;

import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.model.Model;

public interface Dao<T extends Model> {

	public void create(T model);
	
	public void update(T model);
	
	public void update(Integer modelId,List<Property> properties);
	
	public void delete(Integer modelId);
	
	public T find(Class<T> modelClass,Integer modelId);
	
	public Page<T> query();
	
	public Page<T> query(PageCriteria page);
	
	public Page<T> query(PageCriteria page,PropertyCriteria propertyCriteria);
	
	public Page<T> query(PageCriteria page,PropertyCriteria propertyCriteria,OrderCriteria orderCriteria);
}
