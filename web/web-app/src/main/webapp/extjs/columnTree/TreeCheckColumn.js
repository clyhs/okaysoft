/**
 * Provides a checkbox that is always in edit mode so the user does not have 
 * to double-click to enter edit mode and then click the check box to change
 * the value thereby resulting in 3 clicks.  A special renderer is used.
 * NOTE: The data value for the checkbox must be a boolean, not a string ("false") 
 * 
 * Example usage:
    var checkColumn = new Ext.ux.tree.CheckColumn({
       dataIndex: 'yesno'
       ,header: "Yes / No?"
       ,width: 55
    });
    var cm = new Ext.tree.ColumnModel([
        checkColumn
        {...}
    ]);
    var tree = new Ext.tree.TreePanel({
        cm: cm
        ,plugins:checkColumn
        ...
    });
 */
Ext.ns('Ext.ux.tree');
Ext.ux.tree.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.ux.tree.CheckColumn.prototype = {
    
    /**
     * @cfg {Number} nodeSelectorDepth The number of levels to search for nodes in event delegation (defaults to 10)
     */
    nodeSelectorDepth: 10

    /**
     * @cfg {String} nodeSelector The selector used to find nodes internally
     */
    ,nodeSelector: 'div.x-tree-node-el'
    
    ,init : function(tree){
        this.tree = tree;
        tree.on({
            'render': {fn:function(){
                var view = this.tree.getTreeEl();
                view.on('mousedown', this.onMouseDown, this);
            }, scope:this}
            ,'destroy': {fn:this.destroy, scope:this, single:true}
        });
    }

    /**
     * Good practice to provide a destroy
     */
    ,destroy: function() {
        this.onDestroy();
    }
    ,onDestroy: Ext.emptyFn
    
    /**
     * @private
     * @param {Event} evt
     * @param {Element} el
     */
    ,onMouseDown : function(evt, el){
    	// el == <div class="x-grid3-check-col-on x-tree-cc-ext-gen168"> </div>
        if(el.className && el.className.indexOf('x-tree-cc-'+this.id) != -1){
        	Ext.Global.showWaitMsg();
            evt.stopEvent();
            var t = this.tree;
            // 得到一个Ext.Element元素,Ext.Element类是Ext对DOM的封装
            var e = Ext.get(el);
            // HTMLElement 一个html元素
            // p = <div ext:tree-node-id="17" class="x-tree-node-el x-tree-node-leaf ">
			var p = e.findParent(this.nodeSelector, this.nodeSelectorDepth);
			//console.log(p.className+"_"+p.id+"_"+p.style);
			//console.log(p.childNodes.item(5).firstChild.firstChild.className);
            var id = Ext.fly(p).getAttributeNS('ext','tree-node-id');
            var node = this.tree.getNodeById(id);
            if (!node) return;
            var v = node.attributes[this.dataIndex];
            e.replaceClass('x-grid3-check-col'+(v?'-on':''),'x-grid3-check-col'+(!v?'-on':''));
            node.attributes[this.dataIndex] = !v;
			//console.log("moduleId:" + id + " columnId:" + 
			//this.id + " roleId:" + this.roleId + " v:" + !v + " index:" + this.dataIndex);
			
			// crud/0123
			if(this.dataIndex == 'create'){
				if(!v){
					// do ext ui
					var a = node.attributes['add'];
					if (!a) {
						var jsEl = p.childNodes.item(5).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (a ? '-on' : ''), 'x-grid3-check-col' + (!a ? '-on' : ''));
						node.attributes['add'] = !a;
					}
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 0, true, function(){Ext.Global.hideWaitMsg()});
				}else{
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 0, false, function(){Ext.Global.hideWaitMsg()});
				}
			}
			if(this.dataIndex == 'read'){
				if(!v){
					// do ext ui
					var a = node.attributes['add'];
					if (!a) {
						var jsEl = p.childNodes.item(5).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (a ? '-on' : ''), 'x-grid3-check-col' + (!a ? '-on' : ''));
						node.attributes['add'] = !a;
					}
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 1, true, function(){Ext.Global.hideWaitMsg()});
				}else{
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 1, false, function(){Ext.Global.hideWaitMsg()});
				}
			}
			if(this.dataIndex == 'update'){
				if(!v){
					// do ext ui
					var a = node.attributes['add'];
					if (!a) {
						var jsEl = p.childNodes.item(5).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (a ? '-on' : ''), 'x-grid3-check-col' + (!a ? '-on' : ''));
						node.attributes['add'] = !a;
					}
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 2, true, function(){Ext.Global.hideWaitMsg()});
				}else{
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 2, false, function(){Ext.Global.hideWaitMsg()});
				}
			}
			if(this.dataIndex == 'delete'){
				if(!v){
					// do ext ui
					var a = node.attributes['add'];
					if (!a) {
						var jsEl = p.childNodes.item(5).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (a ? '-on' : ''), 'x-grid3-check-col' + (!a ? '-on' : ''));
						node.attributes['add'] = !a;
					}
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 3, true, function(){Ext.Global.hideWaitMsg()});
				}else{
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 3, false, function(){Ext.Global.hideWaitMsg()});
				}
			}
			if(this.dataIndex == 'extend'){
				if(!v){
					// do ext ui
					var a = node.attributes['add'];
					if (!a) {
						var jsEl = p.childNodes.item(6).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (a ? '-on' : ''), 'x-grid3-check-col' + (!a ? '-on' : ''));
						node.attributes['add'] = !a;
					}
					aclManager.addOrUpdateUserExtendsDirect(this.principalSn, id, false, function(){Ext.Global.hideWaitMsg()});
				}else{
					aclManager.addOrUpdateUserExtendsDirect(this.principalSn, id, true, function(){Ext.Global.hideWaitMsg()});
				}
			}
			if(this.dataIndex == 'add'){
				if(!v){
					dwr.engine.setAsync(false);
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 0, true);//C
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 1, true);//R
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 2, true);//U
					if (this.principalType == 'USER') 
						aclManager.addOrUpdateUserExtendsDirect(this.principalSn, id, false);
					aclManager.addOrUpdatePermissionDirect(this.principalType, this.principalSn, id, 3, true, function(){Ext.Global.hideWaitMsg()});//D
					dwr.engine.setAsync(true);
					// c selected
					var c = node.attributes['create'];
					if(!c){
						var jsEl = p.childNodes.item(1).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (c ? '-on' : ''), 'x-grid3-check-col' + (!c ? '-on' : ''));
						node.attributes['create'] = !c;
					}
					var r = node.attributes['read'];
					if(!r){
						var jsEl = p.childNodes.item(2).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (r ? '-on' : ''), 'x-grid3-check-col' + (!r ? '-on' : ''));
						node.attributes['read'] = !r;
					}
					var u = node.attributes['update'];
					if(!u){
						var jsEl = p.childNodes.item(3).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (u ? '-on' : ''), 'x-grid3-check-col' + (!u ? '-on' : ''));
						node.attributes['update'] = !u;
					}
					var d = node.attributes['delete'];
					if(!d){
						var jsEl = p.childNodes.item(4).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (d ? '-on' : ''), 'x-grid3-check-col' + (!d ? '-on' : ''));
						node.attributes['delete'] = !d;
					}
					if (this.principalType == 'USER') {
						var ext = node.attributes['extend'];
						if(!ext){
							var jsEl = p.childNodes.item(5).firstChild.firstChild;
							var extEl = Ext.get(jsEl);
							extEl.replaceClass('x-grid3-check-col' + (ext ? '-on' : ''), 'x-grid3-check-col' + (!ext ? '-on' : ''));
							node.attributes['extend'] = !ext;
						}
					}
				}else{
					aclManager.delPermissionDirect(this.principalType, this.principalSn, id, function(){Ext.Global.hideWaitMsg()});
					// c cancel select
					var c = node.attributes['create'];
					if(c){
						var jsEl = p.childNodes.item(1).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (c ? '-on' : ''), 'x-grid3-check-col' + (!c ? '-on' : ''));
						node.attributes['create'] = !c;
					}
					// d cancel select
					var r = node.attributes['read'];
					if(r){
						var jsEl = p.childNodes.item(2).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (r ? '-on' : ''), 'x-grid3-check-col' + (!r ? '-on' : ''));
						node.attributes['read'] = !r;
					}
					// u cancel select
					var u = node.attributes['update'];
					if(u){
						var jsEl = p.childNodes.item(3).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (u ? '-on' : ''), 'x-grid3-check-col' + (!u ? '-on' : ''));
						node.attributes['update'] = !u;
					}
					// d cancel select
					var d = node.attributes['delete'];
					if(d){
						var jsEl = p.childNodes.item(4).firstChild.firstChild;
						var extEl = Ext.get(jsEl);
						extEl.replaceClass('x-grid3-check-col' + (d ? '-on' : ''), 'x-grid3-check-col' + (!d ? '-on' : ''));
						node.attributes['delete'] = !d;
					}
					if (this.principalType == 'USER') {
						var ext = node.attributes['extend'];
						if(ext){
							var jsEl = p.childNodes.item(5).firstChild.firstChild;
							var extEl = Ext.get(jsEl);
							extEl.replaceClass('x-grid3-check-col' + (ext ? '-on' : ''), 'x-grid3-check-col' + (!ext ? '-on' : ''));
							node.attributes['extend'] = !ext;
						}
					}
				}
			}
        }
    }

    /**
     * Used for generating the initial markup
     * @param {mixed} v The initial value 
     * @param {Object} p The metadata
     * @param {Ext.data.record} record
     * @return {String}
     */
    ,renderer : function(v, p, record){
		// v = true/false
        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-tree-cc-'+this.id+'"> </div>';
    }
};

// register ptype
Ext.reg('treecheckcolumn', Ext.ux.tree.CheckColumn);

// backwards compat
Ext.tree.CheckColumn = Ext.ux.tree.CheckColumn;