package org.okaysoft.core.module.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.okaysoft.core.criteria.Criteria;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.module.model.TreeNode;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.service.RoleService;
import org.okaysoft.core.service.ServiceFacade;
import org.springframework.stereotype.Service;

@Service("treeService")
public class TreeNodeManager {
	
	protected static final OkayLogger log = new OkayLogger(TreeNodeManager.class);

	@Resource(name="roleService")
    private RoleService roleService;

	public List getTree(String id) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        /*
        String seed1 = id + 1;
        String seed2 = id + 2;
        String seed3 = id + 3;
        list.add(new TreeNode(seed1, "" + seed1, false));
        list.add(new TreeNode(seed2, "" + seed2, false));
        list.add(new TreeNode(seed3, "" + seed3, true));*/
        

        Role role = roleService.getRootRole();
        
        list = getTreeNode( role);
        
        
        return list;
    }
	
	public static List<TreeNode> getTreeNode(Role role){
		List<TreeNode> list = new ArrayList<TreeNode>();
		
		TreeNode tn = new TreeNode();
		
		tn.setId(role.getId().toString());
		tn.setText(role.getRoleName());
		if(role.getChild().isEmpty()){
			tn.setLeaf(true);
			list.add(tn);
			
		}else{
			tn.setLeaf(false);
			list.add(tn);
			for(Role item:role.getChild()){
				TreeNode tn2 = new TreeNode();
				tn2.setId(item.getId().toString());
				tn2.setText(item.getRoleName());
				tn2.setLeaf(true);
				list.add(tn2);
			}
		}
		return list;
		
	}
	
	
	
	
}
