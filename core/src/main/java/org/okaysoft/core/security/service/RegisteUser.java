package org.okaysoft.core.security.service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.service.RegisterService;
import org.okaysoft.core.utils.PasswordEncoderUtil;
import org.okaysoft.core.utils.XmlUtils;
import org.springframework.stereotype.Service;





@Service
public class RegisteUser extends RegisterService<User> {
	
	@Resource(name="registeOrg")
    protected RegisteOrg registeOrg;
    @Resource(name="registeRole")
    protected RegisteRole registeRole;

	@Override
	protected void registe() {
		
		// TODO Auto-generated method stub
		/*
		String xml="/init/user.xml";
        log.info("注册【"+xml+"】文件");
        log.info("验证【"+xml+"】文件");
        boolean pass=XmlUtils.validateXML(xml);
        if(!pass){
            log.info("验证没有通过，请参考dtd文件");
            return ;
        }
        log.info("验证通过");
        Page<User> page=Page.newInstance(User.class,RegisteUser.class.getResourceAsStream(xml) );
		
		log.info(page.getModels().toString());
		
        if(page!=null){
            for(User user : page.getModels()){
            	user.setPassword(PasswordEncoderUtil.encode(user.getPassword(), user));
                user.setOrg(registeOrg.getRegisteData().get(0));
                user.addRole(registeRole.getRegisteData().get(0).getChild().get(0));
                serviceFacade.create(user);
            }
        }*/
		
		String xmlFile = "init/user.xml";
		String xml     = "";
		xml = process(User.class,xmlFile);
		
		try {
			ByteArrayInputStream bin=new ByteArrayInputStream(xml.getBytes("utf-8"));
			
			
			Page<User> page=Page.newInstance(User.class, bin);
			
			log.info(page.getModels().toString());
			
	        if(page!=null){
	            for(User user : page.getModels()){
	            	user.setPassword(PasswordEncoderUtil.encode(user.getPassword(), user));
	                user.setOrg(registeOrg.getRegisteData().get(0));
	                user.addRole(registeRole.getRegisteData().get(0).getChild().get(0));
	                serviceFacade.create(user);
	            }
	        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.info("user 注册失败");
		}
		
	}

	@Override
	protected String process(Class<User> model, String xmlFile) {
		// TODO Auto-generated method stub
		return super.process(model, xmlFile);
	}
	
	

}
