package org.okaysoft.core.security.service;

import java.util.ArrayList;
import java.util.List;

import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.service.RegisterService;
import org.okaysoft.core.utils.XMLFactory;
import org.okaysoft.core.utils.XmlUtils;
import org.springframework.stereotype.Service;


@Service
public class RegisteRole extends RegisterService<Role> {

	private Role role = null;

	@Override
	protected void registe() {
		// TODO Auto-generated method stub
        /*
		String xml="/init/role.xml";
        log.info("注册【"+xml+"】文件");
        log.info("验证【"+xml+"】文件");
        boolean pass=XmlUtils.validateXML(xml);
        if(!pass){
            log.info("验证没有通过，请参考dtd文件");
            return ;
        }
        log.info("验证通过");
        XMLFactory factory=new XMLFactory(Role.class);
        role=factory.unmarshal(RegisteRole.class.getResourceAsStream(xml));
        
        assembleRole(role);
        registeRole(role);*/
		
		String xmlFile="init/role.xml";
		String xml    ="";
		xml = process(Role.class,xmlFile);
		XMLFactory factory=new XMLFactory(Role.class);
        role=factory.unmarshal(xml);
        
        assembleRole(role);
        registeRole(role);
		
	}

	@Override
	protected List<Role> getRegisteData() {
		ArrayList<Role> data = new ArrayList<Role>();
		data.add(role);
		return data;
	}

	private void assembleRole(Role role) {
		for (Role child : role.getChild()) {
			child.setParent(role);
			assembleRole(child);
		}
	}

	private void registeRole(Role role) {
		serviceFacade.create(role);
	}

	@Override
	protected String process(Class<Role> model, String xmlFile) {
		// TODO Auto-generated method stub
		return super.process(model, xmlFile);
	}
	
	
}
