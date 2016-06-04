
package org.okaysoft.core.log.model;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.okaysoft.core.annotation.IgnoreUser;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Entity
@Scope("prototype")
@Component
@IgnoreUser
@Table(name="t_operate_log")
public class OperateLog extends Model {
    @ModelAttr("登录IP地址")
    protected String loginIP;
    @ModelAttr("服务器IP地址")
    protected String serverIP;
    @ModelAttr("应用系统名称")
    protected String appName;

    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("操作时间")
    protected Date operatingTime;
    @ModelAttr("操作类型")
    protected String operatingType;   
    @ModelAttr("操作模型")
    protected String operatingModel;
    @ModelAttr("操作ID")
    protected Integer operatingID;


    public Integer getOperatingID() {
        return operatingID;
    }

    public void setOperatingID(Integer operatingID) {
        this.operatingID = operatingID;
    }

    public String getOperatingModel() {
        return operatingModel;
    }

    public void setOperatingModel(String operatingModel) {
        this.operatingModel = operatingModel;
    }
    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }


    @Override
    public String getMetaData() {
        return "业务操作日志";
    }
}