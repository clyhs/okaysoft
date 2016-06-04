package org.okaysoft.core.security.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.security.model.Position;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.security.model.UserGroup;
import org.okaysoft.core.utils.PasswordEncoderUtil;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Scope("prototype")
@Controller
@Namespace("/security")
public class UserAction extends ExtJSSimpleAction<User> {

	private int orgId;
	private String oldPassword;
	private String newPassword;
	private String roles;
	private String positions;
	private String userGroups;

	// 在线用户 根据org查找
	private String org;
	// 在线用户 根据role查找
	private String role;
	// 用户重置密码
	private String password;

	// 用户选择组件
	private boolean select;

	@Override
	protected void checkModel(User model) throws Exception {
		// TODO Auto-generated method stub
		PropertyCriteria propertyCriteria = new PropertyCriteria();
		propertyCriteria.addPropertyEditor(new PropertyEditor("username",
				Operator.eq, "String", model.getUsername()));
		Page<User> p = service.query(User.class, null, propertyCriteria);
		if (p.getTotalRecords() > 0) {
			throw new RuntimeException("添加的用户已存在，请更换用户名");
		}

	}

	@Override
	protected void old(User model) {

		if (model.getUsername().equals("admin")) {
			throw new RuntimeException("演示版本不能修改admin用户");
		}

	}

	@Override
	protected void assemblyModelForPartUpdate(List<Property> properties) {
		// TODO Auto-generated method stub
		for (Property property : properties) {
			if ("password".equals(property.getName().trim())) {
				property.setValue(PasswordEncoderUtil.encode(property
						.getValue().toString(), model));
				break;
			}
		}
	}

	@Override
	protected void assemblyModelForUpdate(User model) {
		if (roles != null) {
			model.clearRole();
			assemblyRoles(model);
		}

		if (positions != null) {
			model.clearPosition();
			assemblyPositions(model);
		}

		if (userGroups != null) {
			model.clearUserGroup();
			assemblyUserGroups(model);
		}
	}

	@Override
	protected void render(Map map, User model) {
		map.put("id", model.getId());
		map.put("version", model.getVersion());
		map.put("username", model.getUsername());
		map.put("realName", model.getRealName());
		map.put("enabled", model.isEnabled() == true ? "启用" : "停用");
		String orgName = "";
		int id = 0;
		if (model.getOrg() != null) {
			orgName = model.getOrg().getOrgName();
			id = model.getOrg().getId();
		}
		map.put("orgName", orgName);
		map.put("orgId", id + "");
		map.put("des", model.getDes());
	}

	@Override
	protected void renderJsonForfind(Map map) {
		render(map, model);
		map.put("roles", model.getRoleStrs());
		map.put("positions", model.getPositionStrs());
		map.put("userGroups", model.getUserGroupStrs());
	}

	@Override
	protected void renderJsonForQuery(List result) {
		for (User user : pageData.getModels()) {
			Map temp = new HashMap();
			render(temp, user);

			StringBuilder str = new StringBuilder();
			for (Role r : user.getRoles()) {
				str.append(r.getRoleName()).append(",");
			}
			temp.put(
					"roles",
					str.length() > 1 ? str.toString().substring(0,
							str.length() - 1) : "");

			str = new StringBuilder();
			for (Position p : user.getPositions()) {
				str.append(p.getPositionName()).append(",");
			}
			temp.put(
					"positions",
					str.length() > 1 ? str.toString().substring(0,
							str.length() - 1) : "");
			result.add(temp);

			str = new StringBuilder();
			for (UserGroup p : user.getUserGroups()) {
				str.append(p.getUserGroupName()).append(",");
			}
			temp.put(
					"userGroups",
					str.length() > 1 ? str.toString().substring(0,
							str.length() - 1) : "");
			result.add(temp);
		}
	}

	@Override
	public void prepareForDelete(Integer[] ids) {
		for (int id : ids) {

			User toDeleteUser = service.find(modelClass, id);
			if (toDeleteUser.getUsername().equals("admin")) {
				throw new RuntimeException("演示版本不能删除admin用户");
			}

		}
	}

	@Override
	public void assemblyModelForCreate(User model) {
		model.setPassword(PasswordEncoderUtil.encode(model.getPassword(), model));
		// 组装角色
		assemblyRoles(model);
		// 组装岗位
		assemblyPositions(model);
		// 组装用户组
		assemblyUserGroups(model);
	}

	public void assemblyRoles(User model) {
		if (roles != null && !"".equals(roles.trim())) {
			String[] roleIds = roles.trim().split(",");
			for (String id : roleIds) {
				String[] attr = id.split("-");
				if (attr.length == 2) {
					int roleId = Integer.parseInt(attr[1]);
					Role temp = service.find(Role.class, roleId);
					if (temp != null) {
						model.addRole(temp);
					}
				}
			}
		}
	}

	public void assemblyPositions(User model) {
		if (positions != null && !"".equals(positions.trim())) {
			String[] positionIds = positions.trim().split(",");
			for (String id : positionIds) {
				String[] attr = id.split("-");
				if (attr.length == 2) {
					int positionId = Integer.parseInt(attr[1]);
					Position temp = service.find(Position.class, positionId);
					if (temp != null) {
						model.addPosition(temp);
					}
				}
			}
		}
	}

	public void assemblyUserGroups(User model) {
		if (userGroups != null && !"".equals(userGroups.trim())) {
			String[] userGroupIds = userGroups.trim().split(",");
			for (String id : userGroupIds) {
				String[] attr = id.split("-");
				if (attr.length == 2) {
					int userGroupId = Integer.parseInt(attr[1]);
					UserGroup temp = service.find(UserGroup.class, userGroupId);
					if (temp != null) {
						model.addUserGroup(temp);
					}
				}
			}
		}
	}
	
	public String reset(){
        Integer[] ids=super.getIds();
        
        log.info("reset");
        if(ids!=null && ids.length>0){
            if(!StringUtils.isBlank(password)){
                for(int id : ids){
                    User user=service.find(User.class, id);
                    user.setPassword(PasswordEncoderUtil.encode(password,user));
                    service.update(user);
                }
                ///Struts2Utils.renderText("已经成功将 "+ids.length+" 个用户的密码重置为"+password);
                map = new HashMap();
        		map.put("success", true);
        		map.put("message", "已经成功将 "+ids.length+" 个用户的密码重置为"+password);
        		Struts2Utils.renderJson(map);
            }else{
                //Struts2Utils.renderText("重置密码不能为空");
            	map = new HashMap();
    			map.put("success", false);
    			map.put("message", "重置密码不能为空");
    			Struts2Utils.renderJson(map);
            }
        }else{
        	map = new HashMap();
			map.put("success", false);
			map.put("message", "必须要指定需要重置密码的用户");
			Struts2Utils.renderJson(map);
            //Struts2Utils.renderText("必须要指定需要重置密码的用户");
        }
        return null;
    }

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(String userGroups) {
		this.userGroups = userGroups;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSelect() {
		return select;
	}

}
