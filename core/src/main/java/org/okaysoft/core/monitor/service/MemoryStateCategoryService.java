package org.okaysoft.core.monitor.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.okaysoft.core.action.converter.DateTypeConverter;
import org.okaysoft.core.monitor.model.MemoryState;
import org.okaysoft.core.service.CategoryService;
import org.springframework.stereotype.Service;


@Service
public class MemoryStateCategoryService extends CategoryService {

	public String getXML(List<MemoryState> data){
        Collections.sort(data, new Comparator(){

            @Override
            public int compare(Object o1, Object o2) {
                MemoryState p1=(MemoryState)o1;
                MemoryState p2=(MemoryState)o2;
                return (int) (p1.getRecordTime().getTime()-p2.getRecordTime().getTime());
            }
        
        });
        //创建根元素
        Element rootElement = createRootElement("","");     
        //创建categories
        Element categories=createCategories(data);        
        rootElement.addContent(categories);
        //创建datasets
        createUserDatasets(data,rootElement);         
        //格式化输出，将对象转换为XML
        String xml=formatXML(rootElement);
        
        return xml;
    }
	 private Element createCategories(List<MemoryState> data){
	        Element element = new Element("categories");
	    	for(MemoryState item : data){
	            Element subElement = createCategory(DateTypeConverter.toDefaultDateTime(item.getRecordTime()));
	            element.addContent(subElement);
	        }
	        return element;
	    }

	    private void createUserDatasets(List<MemoryState> data, Element rootElement) {
	    	Element dataset = createMaxMemoryDataset(data);
	        rootElement.addContent(dataset);
	        dataset = createTotalMemoryDataset(data);
	        rootElement.addContent(dataset);
	        dataset = createFreeMemoryDataset(data);
	        rootElement.addContent(dataset);
	        dataset = createUsableMemoryDataset(data);
	        rootElement.addContent(dataset);
	        dataset = createUsingMemoryDataset(data);
	        rootElement.addContent(dataset);        
	    }

	    private Element createMaxMemoryDataset(List<MemoryState> data) {
	    	Element element = new Element("dataset");
	    	element.setAttribute(new Attribute("seriesName", "最大可用内存"));
	        for(MemoryState item : data){
	            Element subElement = createDataset(item.getMaxMemory().intValue());
	            element.addContent(subElement);
	        }
	        return element;
	    }

	    private Element createTotalMemoryDataset(List<MemoryState> data) {
	    	Element element = new Element("dataset");
	    	element.setAttribute(new Attribute("seriesName", "已分配内存"));
	        for(MemoryState item : data){
	            Element subElement = createDataset(item.getTotalMemory().intValue());
	            element.addContent(subElement);
	        }
	        return element;
	    }

	    private Element createFreeMemoryDataset(List<MemoryState> data) {
	    	Element element = new Element("dataset");
	    	element.setAttribute(new Attribute("seriesName", "已释放内存"));
	        for(MemoryState item : data){
	            Element subElement = createDataset(item.getFreeMemory().intValue());
	            element.addContent(subElement);
	        }
	        return element;
	    }

	    private Element createUsableMemoryDataset(List<MemoryState> data) {
	    	Element element = new Element("dataset");
	    	element.setAttribute(new Attribute("seriesName", "可用内存"));
	        for(MemoryState item : data){
	            Element subElement = createDataset(item.getUsableMemory().intValue());
	            element.addContent(subElement);
	        }
	        return element;
	    }

	    private Element createUsingMemoryDataset(List<MemoryState> data) {
	    	Element element = new Element("dataset");
	    	element.setAttribute(new Attribute("seriesName", "已用内存"));
	        for(MemoryState item : data){
	            Element subElement = createDataset(item.getTotalMemory().intValue()-item.getFreeMemory().intValue());
	            element.addContent(subElement);
	        }
	        return element;
	    }
}
