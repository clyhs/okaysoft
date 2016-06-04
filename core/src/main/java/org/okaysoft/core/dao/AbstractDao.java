package org.okaysoft.core.dao;

import java.util.List;

import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.model.Model;

public class AbstractDao<T extends Model> extends DaoSupport implements Dao<T> {
	
	private Class<T> modelClass;

	public void create(T model) {
		// TODO Auto-generated method stub
		
	}

	public void update(T model) {
		// TODO Auto-generated method stub
		
	}

	public void update(Integer modelId, List<Property> properties) {
		// TODO Auto-generated method stub
		
	}

	public void delete(Integer modelId) {
		// TODO Auto-generated method stub
		
	}

	public T find(Class<T> modelClass, Integer modelId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<T> query() {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<T> query(PageCriteria page) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<T> query(PageCriteria page, PropertyCriteria propertyCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<T> query(PageCriteria page, PropertyCriteria propertyCriteria,
			OrderCriteria orderCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
