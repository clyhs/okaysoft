package org.okaysoft.core.module.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class TreeNode {

	private String id;
    private String text;
    private boolean leaf;
    
    public TreeNode() {
        
    }
    
    public TreeNode(String id, String text, boolean leaf) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
    
    
}
