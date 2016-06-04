package org.okaysoft.core.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.model.UserGroup;
import org.okaysoft.core.service.ServiceFacade;
import org.springframework.stereotype.Service;


@Service
public class UserGroupService {

	protected static final OkayLogger log =new OkayLogger(UserGroupService.class);
	@Resource(name="serviceFacade")
    private ServiceFacade serviceFacade;
	
	public String toAllUserGroupJson(){
        List<UserGroup> userGroups=serviceFacade.query(UserGroup.class, null).getModels();
        return toJson(userGroups);
    }
	
	public String toJson(List<UserGroup> userGroups){        
        if(userGroups==null || userGroups.isEmpty()){
            return "";
        }
        
        StringBuilder json=new StringBuilder();
        
        json.append("[");
        for(UserGroup userGroup : userGroups){
            json.append("{'text':'")
                .append(userGroup.getUserGroupName())
                .append("','id':'userGroup-")
                .append(userGroup.getId())
                .append("','iconCls':'")
                .append("role")
                .append("'")
                .append(",'leaf':true")
                .append("},");
        }
        json=json.deleteCharAt(json.length()-1);
        json.append("]");
            
        return json.toString();
    }
}
