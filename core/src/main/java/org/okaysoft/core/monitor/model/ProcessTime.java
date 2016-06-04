package org.okaysoft.core.monitor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.okaysoft.core.annotation.IgnoreLog;
import org.okaysoft.core.annotation.IgnoreUser;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.utils.ConvertUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Entity
@Scope("prototype")
@Component
@Table(name="t_processTime")
@IgnoreLog
@IgnoreUser
public class ProcessTime extends Model {

	public String getProcessTimeStr(){
        return ConvertUtils.getTimeDes(processTime);
    }
    @ModelAttr("用户IP地址")
    protected String userIP;
    @ModelAttr("服务器IP地址")
    protected String serverIP;

    @ModelAttr("应用系统名称")
    protected String appName;

    @ModelAttr("资源路径")
    @Column(name="resourceField")
    protected String resource;

    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("开始处理时间")
    protected Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("处理完成时间")
    protected Date endTime;

    //单位为毫秒
    @ModelAttr("操作耗时")
    @Column(name="processTimeField")
    protected Long processTime;
    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }
    

    @Override
    public String getMetaData() {
        return "请求处理时间日志";
    }

}
