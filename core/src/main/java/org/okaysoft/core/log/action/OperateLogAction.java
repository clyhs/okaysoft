package org.okaysoft.core.log.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.log.model.OperateLog;
import org.okaysoft.core.log.model.OperateStatistics;
import org.okaysoft.core.log.service.OperateLogChartDataService;
import org.okaysoft.core.log.service.OperateTypeCategoryService;
import org.okaysoft.core.log.service.UserCategoryService;
import org.okaysoft.core.model.ModelMetaData;
import org.okaysoft.core.service.LogService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Scope("prototype")
@Controller
@Namespace("/log")
public class OperateLogAction extends ExtJSSimpleAction<OperateLog> {

	@Resource(name="userCategoryService")
    private UserCategoryService userCategoryService;
    @Resource(name="operateTypeCategoryService")
    private OperateTypeCategoryService operateTypeCategoryService;
    private String category;
    
    @Override
    public String query(){
        LogService.getLogQueue().saveLog();
        return super.query();
    }
    @Override
    protected void afterRender(Map map,OperateLog obj){
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }
    
    @Override
    protected String generateReportData(List<OperateLog> models) {
        List<OperateStatistics> data=OperateLogChartDataService.getData(models);
        if("user".equals(category)){
            return userCategoryService.getXML(data);
        }else{
            return operateTypeCategoryService.getXML(data);
        }
    }
    /**
     * 所有模型信息
     * @return 
     */
    public String store(){        
        List<Map<String,String>> data=new ArrayList<>();
        for(String key : ModelMetaData.getModelDes().keySet()){
            Map<String,String> temp=new HashMap<>();
            temp.put("value", ModelMetaData.getModelDes().get(key));
            temp.put("text", ModelMetaData.getModelDes().get(key));
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
