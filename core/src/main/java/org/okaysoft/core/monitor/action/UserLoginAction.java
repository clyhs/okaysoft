package org.okaysoft.core.monitor.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.monitor.model.UserLogin;
import org.okaysoft.core.monitor.service.UserLoginChartDataService;
import org.okaysoft.core.monitor.service.UserLoginSingleService;
import org.okaysoft.core.service.LogService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Scope("prototype")
@Controller
@Namespace("/monitor")
public class UserLoginAction extends ExtJSSimpleAction<UserLogin> {

	private String category;
    @Resource(name="userLoginSingleService")
    private UserLoginSingleService userLoginSingleService;
    @Override
    public String query(){
        LogService.getLogQueue().saveLog();
        return super.query();
    }    
    @Override
    protected void afterRender(Map map,UserLogin obj){
        map.put("onlineTime", obj.getOnlineTimeStr());
        map.remove("userAgent");
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }
    @Override
    protected String generateReportData(List<UserLogin> models) {
        LinkedHashMap<String,Long> data=new LinkedHashMap<>();
        switch (category) {
            case "loginTimes":
                data=UserLoginChartDataService.getUserLoginTimes(models);
                break;
            case "onlineTime":
                data=UserLoginChartDataService.getUserOnlineTime(models);
                break;
        }
        
        return userLoginSingleService.getXML(data);
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
