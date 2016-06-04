package org.okaysoft.core.monitor.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.okaysoft.core.annotation.IgnoreLog;
import org.okaysoft.core.annotation.IgnoreUser;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Entity
@Scope("prototype")
@Component
@Table(name="t_memoryState")
@IgnoreLog
@IgnoreUser
public class MemoryState extends Model {

	@ModelAttr("服务器IP地址")
    protected String serverIP;

    @ModelAttr("应用系统名称")
    protected String appName;
    
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("记录时间")
    protected Date recordTime;

    @ModelAttr("最大可用内存")
    protected Float maxMemory;

    @ModelAttr("已分配内存")
    protected Float totalMemory;
    
    @ModelAttr("已释放内存")
    protected Float freeMemory;
    
    @ModelAttr("可用内存")
    protected Float usableMemory;
    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Float getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Float freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Float getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Float maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Float getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Float totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Float getUsableMemory() {
        return usableMemory;
    }

    public void setUsableMemory(Float usableMemory) {
        this.usableMemory = usableMemory;
    }

    @Override
    public String getMetaData() {
        return "内存使用情况日志";
    }

}
