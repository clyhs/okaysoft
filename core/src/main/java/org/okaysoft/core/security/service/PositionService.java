package org.okaysoft.core.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.okaysoft.core.criteria.Criteria;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.model.Position;
import org.okaysoft.core.service.ServiceFacade;
import org.springframework.stereotype.Service;


@Service
public class PositionService {

	protected static final OkayLogger log = new OkayLogger(PositionService.class);
    @Resource(name="serviceFacade")
    private ServiceFacade serviceFacade;

    public static List<String> getChildNames(Position position){
        List<String> names=new ArrayList<>();
        List<Position> child=position.getChild();
        for(Position item : child){
            names.add(item.getPositionName());
            names.addAll(getChildNames(item));
        }
        return names;
    }
    public static List<Integer> getChildIds(Position position){
        List<Integer> ids=new ArrayList<>();
        List<Position> child=position.getChild();
        for(Position item : child){
            ids.add(item.getId());
            ids.addAll(getChildIds(item));
        }
        return ids;
    }
    public static boolean isParentOf(Position parent,Position child){
        Position position=child.getParent();
        while(position!=null){
            if(position.getId()==parent.getId()){
                return true;
            }
            position=position.getParent();
        }
        return false;
    }
    
    public String toRootJson(boolean recursion){
        Position rootPosition=getRootPosition();
        if(rootPosition==null){
            log.error("获取根岗位失败！");
            return "";
        }
        StringBuilder json=new StringBuilder();
        json.append("[");

        json.append("{'text':'")
            .append(rootPosition.getPositionName())
            .append("','id':'position-")
            .append(rootPosition.getId());
            if(rootPosition.getChild().isEmpty()){
                json.append("','leaf':true,'cls':'file'");
            }else{
                json.append("','leaf':false,'cls':'folder'");
                
                if (recursion) {
                    for(Position item : rootPosition.getChild()){
                        json.append(",children:").append(toJson(item.getId(), recursion));
                    }
                }
            }
        json.append("}");
        json.append("]");
        
        return json.toString();
    }
    public String toJson(int positionId, boolean recursion){
        Position position=serviceFacade.find(Position.class, positionId);
        if(position==null){
            log.error("获取ID为 "+positionId+" 的岗位失败！");
            return "";
        }
        List<Position> child=position.getChild();
        if(child.isEmpty()){
            return "";
        }
        StringBuilder json=new StringBuilder();
        json.append("[");

        
        for(Position item : child){
            json.append("{'text':'")
                .append(item.getPositionName())
                .append("','id':'position-")
                .append(item.getId());
                if(item.getChild().isEmpty()){
                    json.append("','leaf':true,'cls':'file'");
                }else{
                    json.append("','leaf':false,'cls':'folder'");
                    if (recursion) {
                        json.append(",children:").append(toJson(item.getId(), recursion));
                    }
                }
           json .append("},");
        }
        //删除最后一个,号，添加一个]号
        json=json.deleteCharAt(json.length()-1);
        json.append("]");

        return json.toString();
    }
    public Position getRootPosition(){
        PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("positionName", Operator.eq, "String","岗位"));
        Page<Position> page = serviceFacade.query(Position.class, null, propertyCriteria);
        if (page.getTotalRecords() == 1) {
            return page.getModels().get(0);
        }
        return null;
    }
}
