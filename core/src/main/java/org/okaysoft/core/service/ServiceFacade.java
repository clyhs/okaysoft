package org.okaysoft.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.dao.DaoFacade;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceFacade {
	
	protected final OkayLogger log = new OkayLogger(getClass());
	
	@Resource(name = "daoFacade")
	private DaoFacade dao;
	
	public void clear(){
		dao.clear();
	}
	
	@Transactional
	public <T extends Model> void create(T model){
		dao.create(model);
	}
	
	@Transactional
	public <T extends Model> void update(T model){
		dao.update(model);
	}
	/**
	 * 不用加@Transactional 
	 * @param modelClass
	 * @param modelId
	 * @return
	 */
	public <T extends Model> T find(Class<T> modelClass,Integer modelId){
		T model = dao.find(modelClass, modelId);
		return model;
	}
	
	@Transactional
	public <T extends Model> void delete(Class<T> modelClass, Integer modelId){
		dao.delete(modelClass, modelId);
	}
	
	@Transactional
	public <T extends Model> List<Integer> delete(Class<T> modelClass,Integer[] modelIds) {
        List<Integer> ids=new ArrayList<>();
		for(Integer modelId : modelIds){
			try{
				this.delete(modelClass,modelId);
                ids.add(modelId);
			}catch(Exception e){
				log.error("删除模型出错",e);
			}
		}
        return ids;
	}
	
	
	public <T extends Model> Page<T> query(Class<T> modelClass){
		return dao.query(modelClass);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria page){
		return dao.query(modelClass,page);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria page,PropertyCriteria propertyCriteria){
		return dao.query(modelClass,page,propertyCriteria);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria page,PropertyCriteria propertyCriteria,OrderCriteria orderCriteria){
		return dao.query(modelClass,page,propertyCriteria,orderCriteria);
	}
	
	public <T extends Model> Page<T> search(Class<T> modelClass , PageCriteria pageCriteria , String queryString){
		Page<T> page = dao.search(modelClass, pageCriteria, queryString);
        return page;
	}

}
