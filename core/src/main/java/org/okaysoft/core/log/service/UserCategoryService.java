package org.okaysoft.core.log.service;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.okaysoft.core.log.model.OperateStatistics;
import org.okaysoft.core.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryService extends CategoryService {
	
	public String getXML(List<OperateStatistics> data){
        //创建根元素
        Element rootElement = createRootElement("","");    
        //创建categories       
        Element categories=createCategories(data);        
        rootElement.addContent(categories);
        //创建datasets
        Element addDataset=createAddDataset(data);   
        rootElement.addContent(addDataset);
        Element deleteDataset=createDeleteDataset(data);   
        rootElement.addContent(deleteDataset);
        Element updateDataset=createUpdateDataset(data);   
        rootElement.addContent(updateDataset);
        //格式化输出，将对象转换为XML
        String xml=formatXML(rootElement);
        
        return xml;
    }

	
	private Element createCategories(List<OperateStatistics> data){
    	Element element = new Element("categories");
    	for(OperateStatistics item : data){
            Element subElement = createCategory(item.getUsername());
            element.addContent(subElement);
        }
        return element;
    }

    private Element createAddDataset(List<OperateStatistics> data) {
    	Element element = new Element("dataset");
    	element.setAttribute(new Attribute("seriesName", "添加数据"));
    	for(OperateStatistics item : data){
            Element subElement = createDataset(item.getAddCount());
            element.addContent(subElement);
        }
        return element;
    }

    private Element createDeleteDataset(List<OperateStatistics> data) {
    	Element element = new Element("dataset");
    	element.setAttribute(new Attribute("seriesName", "删除数据"));
    	for(OperateStatistics item : data){
            Element subElement = createDataset(item.getDeleteCount());
            element.addContent(subElement);
        }
        return element;
    }

    private Element createUpdateDataset(List<OperateStatistics> data) {
    	Element element = new Element("dataset");
    	element.setAttribute(new Attribute("seriesName", "修改数据"));
    	for(OperateStatistics item : data){
            Element subElement = createDataset(item.getUpdateCount());
            element.addContent(subElement);
        }
        return element;
    }

}
