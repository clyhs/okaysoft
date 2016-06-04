package org.okaysoft.core.security.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.security.model.Org;
import org.okaysoft.core.security.service.OrgService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Scope("prototype")
@Controller
@Namespace("/security")
public class OrgAction extends ExtJSSimpleAction<Org> {

	private String node;
    @Resource(name="orgService")
    private OrgService orgService;

    public String store(){
        return query();
    }
    @Override
    public String query(){
        //如果node为null则采用普通查询方式
        if(node==null){
            return super.query();
        }
        //如果指定了node则采用自定义的查询方式
        if(node.trim().startsWith("root")){
            String json=orgService.toRootJson();
            Struts2Utils.renderJson(json);
        }else{
            int id=Integer.parseInt(node.trim());
            String json=orgService.toJson(id);
            Struts2Utils.renderJson(json);
        }
        return null;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
