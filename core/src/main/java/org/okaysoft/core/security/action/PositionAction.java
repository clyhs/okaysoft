package org.okaysoft.core.security.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.security.model.Position;
import org.okaysoft.core.security.service.PositionService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Scope("prototype")
@Controller
@Namespace("/security")
public class PositionAction extends ExtJSSimpleAction<Position> {

	private String node;
    @Resource(name="positionService")
    private PositionService positionService;
    private List<Command> commands;
    private boolean recursion=false;
    
    public String store(){            
        if(recursion){
            int rootId = positionService.getRootPosition().getId();
            String json=positionService.toJson(rootId,recursion);
            Struts2Utils.renderJson(json);
            
            return null;
        }
        
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
            String json=positionService.toRootJson(recursion);
            Struts2Utils.renderJson(json);
        }else{
            String[] attr=node.trim().split("-");
            if(attr.length==2){
                int positionId=Integer.parseInt(attr[1]);
                String json=positionService.toJson(positionId,recursion);
                Struts2Utils.renderJson(json);                    
            }                
        }
        return null;
    }
    
	public void setNode(String node) {
		this.node = node;
	}
	public void setRecursion(boolean recursion) {
		this.recursion = recursion;
	}
    
    
}
