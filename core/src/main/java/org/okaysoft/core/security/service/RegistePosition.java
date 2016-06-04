package org.okaysoft.core.security.service;

import java.util.ArrayList;
import java.util.List;

import org.okaysoft.core.security.model.Position;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.service.RegisterService;
import org.okaysoft.core.utils.XMLFactory;
import org.okaysoft.core.utils.XmlUtils;
import org.springframework.stereotype.Service;



@Service
public class RegistePosition extends RegisterService<Position>{
	
	private Position position;

	@Override
	protected void registe() {
		// TODO Auto-generated method stub
		/*
		String xml="/init/position.xml";
        log.info("注册【"+xml+"】文件");
        log.info("验证【"+xml+"】文件");
        boolean pass=XmlUtils.validateXML(xml);
        if(!pass){
            log.info("验证没有通过，请参考dtd文件");
            return ;
        }
        log.info("验证通过");
        XMLFactory factory=new XMLFactory(Position.class);
        position=factory.unmarshal(RegistePosition.class.getResourceAsStream(xml));
        
        assemblePosition(position);
        registePosition(position);*/
		
		String xmlFile="init/position.xml";
		String xml    = "";
		xml = process(Position.class,xmlFile);
		XMLFactory factory=new XMLFactory(Position.class);
        position=factory.unmarshal(xml);
        assemblePosition(position);
        registePosition(position);
	}
	
	@Override
    protected List<Position> getRegisteData() {
        ArrayList<Position> data=new ArrayList<>();
        data.add(position);
        return data;
    }

    private void assemblePosition(Position position) {
        for(Position child : position.getChild()){
            child.setParent(position);
            assemblePosition(child);
        }
    }

    private void registePosition(Position position) {
        serviceFacade.create(position);
    }

	@Override
	protected String process(Class<Position> model, String xmlFile) {
		// TODO Auto-generated method stub
		return super.process(model, xmlFile);
	}
    
    

}
