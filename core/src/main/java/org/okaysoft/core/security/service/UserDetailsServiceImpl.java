/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.okaysoft.core.security.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.okaysoft.core.criteria.Criteria;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.service.ServiceFacade;
import org.okaysoft.core.utils.FileUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    protected static final OkayLogger log = new OkayLogger(UserDetailsServiceImpl.class);
    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;
    private static Map<String,String> messages = new HashMap<>();
    private String message;
    //private static final IPAccessControler ipAccessControler=new IPAccessControler();

    public synchronized static String getMessage(String username) {
        String result = messages.get(username);
        log.debug("username "+username+" getMessage:"+result);
        messages.clear();
        return result;
    }

    @Override
    public synchronized UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        try{
        	/*
            if(ipAccessControler.deny(OpenEntityManagerInViewFilter.request)){
                message = "IP访问策略限制";
                throw new UsernameNotFoundException(message);
            }*/
            return load(username);
        }catch(  UsernameNotFoundException | DataAccessException e){
            throw e;
        }
        finally{
            log.debug("messages put "+username+":"+message);
            messages.put(TextEscapeUtils.escapeEntities(username), message);
        }
    }
    
    public UserDetails load(String username) throws UsernameNotFoundException, DataAccessException {
        message = "密码不正确";
        /*
        if(FileUtils.existsFile("/WEB-INF/licence")){
            Collection<String> reqs = FileUtils.getTextFileContent("/WEB-INF/licence");
            message="您还没有购买产品";
            if(reqs!=null && reqs.size()==1){
                message+=":"+reqs.iterator().next().toString();
            }
            log.info(message);
            throw new UsernameNotFoundException(message);
        }*/
        if (StringUtils.isBlank(username)) {
            log.info("请输入用户名");
            message = "请输入用户名";
            throw new UsernameNotFoundException("请输入用户名");
        }
        /* 取得用户 */
        PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("username", Operator.eq, "String",username));

        Page<User> page = serviceFacade.query(User.class, null, propertyCriteria);

        if (page.getTotalRecords() != 1) {
            log.info("用户账号不存在: " + username);
            message = "用户账号不存在";
            throw new UsernameNotFoundException("用户账号不存在");
        }
        if(!page.getModels().get(0).isEnabled()){
            message = "用户账号被禁用";
            throw new UsernameNotFoundException("用户账号被禁用");
        }
        if(!page.getModels().get(0).isAccountNonExpired()){
            message = "用户帐号已过期";
            throw new UsernameNotFoundException("用户帐号已过期");
        }
        if(!page.getModels().get(0).isAccountNonLocked()){
            message = "用户帐号已被锁定";
            throw new UsernameNotFoundException("用户帐号已被锁定");
        }
        if(!page.getModels().get(0).isCredentialsNonExpired()){
            message = "用户凭证已过期";
            throw new UsernameNotFoundException("用户凭证已过期");
        }
        if(page.getModels().get(0).getAuthorities()==null){
            message = "用户帐号未被授予任何权限";
            throw new UsernameNotFoundException("用户帐号未被授予任何权限");
        }

        return page.getModels().get(0);
    }
}