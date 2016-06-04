package org.okaysoft.core.security.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.security.model.Org;
import org.okaysoft.core.service.RegisterService;
import org.okaysoft.core.utils.FileUtils;
import org.okaysoft.core.utils.XMLFactory;
import org.okaysoft.core.utils.XmlUtils;
import org.springframework.stereotype.Service;



@Service
public class RegisteOrg extends RegisterService<Org>{

	private Org org = null;
	
	@Override
	protected void registe() {
		// TODO Auto-generated method stub
		/*
		String xml = "/init/org.xml";
		
        log.info("注册【"+xml+"】文件");
        log.info("验证【"+xml+"】文件");
        boolean pass=XmlUtils.validateXML(xml);
        if(!pass){
            log.info("验证没有通过，请参考dtd文件");
            return ;
        }
        log.info("验证通过");
        XMLFactory factory=new XMLFactory(Org.class);
        org=factory.unmarshal(RegisteOrg.class.getResourceAsStream(xml));
        assembleOrg(org);
        registeOrg(org);*/
		
		
		String xmlFile = "init/org.xml";
		
		
		
		String xml     = process(Org.class,xmlFile);
		XMLFactory factory=new XMLFactory(Org.class);
        org=factory.unmarshal(xml);
        assembleOrg(org);
        registeOrg(org);
		
		
		
        
	}
	
	
	
	
	@Override
    protected List<Org> getRegisteData() {
        ArrayList<Org> data=new ArrayList<>();
        data.add(org);
        return data;
    }
	
	private void assembleOrg(Org org) {
        for(Org child : org.getChild()){
            child.setParent(org);
            assembleOrg(child);
        }
    }

    private void registeOrg(Org org) {
        serviceFacade.create(org);
    }

	@Override
	protected String process(Class<Org> model, String xmlFile) {
		// TODO Auto-generated method stub
		return super.process(model, xmlFile);
	}
    
    

}
