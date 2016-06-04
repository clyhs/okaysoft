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
import org.okaysoft.core.utils.ConvertUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Entity
@Scope("prototype")
@Component
@Table(name="t_runingTime")
@IgnoreLog
@IgnoreUser
public class RuningTime extends Model {

	public String getRuningTimeStr(){
        return ConvertUtils.getTimeDes(runingTime);
    }
    /**
     * 服务器IP地址
     */
    @ModelAttr("服务器IP地址")
    protected String serverIP;

    @ModelAttr("应用系统名称")
    protected String appName;
    
    @ModelAttr("操作系统名称")
    protected String osName;
    
    @ModelAttr("操作系统版本")
    protected String osVersion;

    @ModelAttr("CPU架构")
    protected String osArch;
    
    @ModelAttr("Java虚拟机名称")
    protected String jvmName;
    
    @ModelAttr("Java虚拟机版本")
    protected String jvmVersion;
    
    @ModelAttr("Java虚拟机提供商")
    protected String jvmVendor;
    
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("系统启动时间")
    protected Date startupTime;

    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("系统关闭时间")
    protected Date shutdownTime;
    
    //单位为毫秒
    @ModelAttr("持续运行时间")
    protected Long runingTime;

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

    public Date getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(Date shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public Date getStartupTime() {
        return startupTime;
    }

    public void setStartupTime(Date startupTime) {
        this.startupTime = startupTime;
    }

    public Long getRuningTime() {
        return runingTime;
    }

    public void setRuningTime(Long runingTime) {
        this.runingTime = runingTime;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getJvmName() {
        return jvmName;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmVendor() {
        return jvmVendor;
    }

    public void setJvmVendor(String jvmVendor) {
        this.jvmVendor = jvmVendor;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }


    @Override
    public String getMetaData() {
        return "系统持续运行时间日志";
    }

}
