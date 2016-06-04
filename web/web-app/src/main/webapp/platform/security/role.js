Ext.namespace("platform.security");




//var propertyCriteriaPre="parent.id:eq:";
//var propertyCriteria=propertyCriteriaPre+currentId;

rolePanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/security/role!query.action',
	createURL:'/okaysoft/security/role!create.action',
	
	updatePartURL:'/okaysoft/security/role!updatePart.action?model.id=',
	
	deleteURL:'/okaysoft/security/role!delete.action',
	currentNode:'',
	currentName:"角色",
	currentId:'1',
	queryString:'',
	search:false,
	pageSize:'17',
	orgId:"-1",
	grid:'',
	model:'',
	getFields: function(){
        var fields=[
            {name: 'id'},
            {name: 'version'},
            {name: 'roleName'},
            {name: 'des'}
            ];
       return fields;     
    },
    
    getCM:function(){
		var cm =  new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer({
		    	header : '行号',
                width : 40
		    }),
		    new Ext.grid.CheckboxSelectionModel(),
		    {header: "编号", width: 20, dataIndex: 'id', sortable: true},
            {header: "版本", width: 20, dataIndex: 'version', sortable: true},
            {header: "角色名称", width: 40, dataIndex: 'roleName', sortable: true,editor:new Ext.form.TextField()},
            {header: "描述", width: 20, dataIndex: 'des', sortable: true,editor:new Ext.form.TextField()}
		    ]);
		return cm;
	},
	
	getStore:function(){
		if(undefined==this.storeURLParameter){
            this.storeURLParameter="";
        }

		var store = new Ext.data.Store({
            reader: new Ext.data.JsonReader({
                totalProperty: 'totalProperty',
                root: 'root'
            },
            Ext.data.Record.create(this.getFields())
            ),
            proxy : new Ext.data.HttpProxy({
                url : '/okaysoft/security/role!query.action'
            })
        });
		store.load({
			params : {
				limit : this.pageSize
			}
		});
	    return store;
	},
	
	getTipStr:function(){
    	var tips=['增加(C)','删除(R)','修改(U)'];
    	return tips;
    },
    
    getCommandsStr:function(){
    	var commands=["create","delete","updatePart"];
    	return commands;
    },
    
    getCallbackStr:function(){
    	var callbacks = [this.create,this.remove,this.modify];
    	return callbacks;
    },
    
    getGrid:function(){
		
		var cb = new Ext.grid.CheckboxSelectionModel();
		
		
		
		var contextmenu=this.getContextMenu(this.getCommandsStr(),this.getTipStr(),this.getCallbackStr());
		
		var grid = new Ext.grid.GridPanel({
			title:' ',
            autoHeight: true,
            frame:true,
			store: this.store,
			sm : cb,
            
			cm: this.cm,
			selModel: new Ext.grid.RowSelectionModel({
				singleSelect: true
			}),
			stripeRows : true,
            autoScroll : true,
            viewConfig : {
                loadingText : '数据加载中,请稍等...',
                emptyText : '无对应信息',
                deferEmptyText : true,
                autoFill : true,
                forceFit:true  
            },
			loadMask: true,
			bbar:this.getBBar(),
			tbar:this.getToolbar() 
		});
		grid.on("rowcontextmenu",function(grid,rowIndex,e){
            e.preventDefault();
            grid.getSelectionModel().selectRow(rowIndex);
            contextmenu.showAt(e.getXY());
        });
		grid.on('rowdblclick',function(grid,index,e){
            //alert();
        });
		
		return grid;
	},
    
    create:function(){
    	
    	var nameList = this.getFieldList("roleName");
    	var idList = this.getIdList();
    	if(idList.length>1){
            Ext.ux.Toast.msg('操作提示：','只能选择一条记录');  
            return ;
        }
    	
    	if(idList.length==1){
    		this.currentId = idList[0];
    		this.currentName = nameList[0];
    	}else{
    		this.currentId = '1';
    		this.currentName = '角色';
    	}
    	//alert(this.currentId);
    	//alert(this.currentName+this.currentId);
    	
    	this.openPopWin(500,400,Ext.getBody(),this.getCreateItems(),this.getButtons('保存'));
    },
    remove:function(){
    	var idList = this.getIdList();
    	if(idList.length<1){
            Ext.ux.Toast.msg('操作提示：','请选择要进行操作的记录');  
            return ;
        }
    	Ext.MessageBox.confirm("请确认","确实要删除吗？",function(button,text){
            if(button == "yes"){
                var ids = idList.join(',');
                alert(ids);
                Ext.Ajax.request({
                    url : this.deleteURL+'?time='+new Date().toString(),
                    waitTitle: '请稍等',
                    waitMsg: '正在删除数据……',
                    params : {
                        ids : ids
                    },
                    method : 'POST',
                    success : function(response,opts){
                    	Ext.ux.Toast.msg('操作提示：',response.responseText); 
                    	this.store.reload();
    					this.tree.getLoader().dataUrl = this.storeURL+'?time='+new Date().toString();
    			        this.tree.getLoader().load(this.tree.root);
                    },
                    scope:this
                });
            }
        },this);
    },
    modify:function(){},
    
    
    getPrivilegeSelector:function(isSave,model){
    	var privilegeLoader = new Ext.tree.TreeLoader({
            dataUrl:contextPath + '/module/module!query.action?privilege=true'
        });
    	var privilegeSelector = new parent.Ext.ux.tree.CheckTreePanel({
            title : '',
            id : "privilegeSelector",
            bubbleCheck:'none' ,
            cascadeCheck:'all',
            deepestOnly:'true',
            rootVisible : false,
            loader : privilegeLoader,
            root : new Ext.tree.AsyncTreeNode({
                text:'功能菜单',
                id : 'root',
                expanded : true
            })
        });
        privilegeSelector.reset=function(){
            this.clearValue();
        };
        
        return privilegeSelector;
    },
    
    
    getCreateItems:function(){
    	this.addPrivilegeSelector = this.getPrivilegeSelector(true, null);
    	
    	var items=[{
    		xtype: 'fieldset',
            id:'baseInfo',
            title: '基本信息',
            collapsible: false,
            defaults: {
                anchor: '95%'
            },
            items:[{
                xtype:'textfield',
                readOnly:true,
                disabled:true,
                fieldClass:'detail_field',
                value: this.currentName,
                fieldLabel: '上级角色'
            },{
                xtype: 'textfield',
                name: 'model.roleName',
                fieldLabel: '角色名称',
                allowBlank: false,
                blankText : '角色名称不能为空'
            },{
                xtype: 'textfield',
                name: 'model.superManager',
                id:'superManager',
                hidden: true,
                hideLabel:true
            },{
                xtype: 'textfield',
                name: 'model.des',
                fieldLabel: '备注',
                allowBlank: true
            },{
                xtype: 'checkbox',
                fieldLabel: '超级权限',
                boxLabel: '',
                listeners : {"check" : function(obj,ischecked){
                        if(ischecked){
                            Ext.getCmp('privilegeSelectorSet').hide();
                            Ext.getCmp('superManager').setValue("true");
                        }else{
                            Ext.getCmp('privilegeSelectorSet').show();
                            Ext.getCmp('superManager').setValue("false");
                        }
                }}  
            }]
    	},{
            xtype: 'fieldset',
            id:'privilegeSelectorSet',
            title: '普通权限',
            collapsible: true,
            items: [this.addPrivilegeSelector,{
                xtype: 'textfield',
                name: 'privileges',
                id:'privileges',
                hidden: true,
                hideLabel:true
            }]
        }];
    	return items;
    },
    
    createForm:function(items){
    	var formpanel =new Ext.form.FormPanel({
			labelAlign: 'left',
	        buttonAlign: 'center',
	        bodyStyle: 'padding:5px',
	        frame: true,//圆角和浅蓝色背景
	        labelWidth: 80,
	        autoScroll:true,
	        defaults: {
	            anchor: '95%'
	        },
	        items:items
		});
		return formpanel;
    },
    
    getButtons:function(text){
    	
    	var buttons = [{
			text: text,
			scope:this,
			iconCls:'save',
			handler:function(){
				if(text == '保存'){
					this.save(true);
				}
				if(text == '修改'){
					this.save(false);
				}
			}
		},{
			text: '重置',
			scope:this,
			iconCls:'reset',
			handler:function(){
				this.resetPopWin();
			}
		},{
			text: '取消',
			scope:this,
			iconCls:'cancel',
			handler:function(){
				this.closePopWin(Ext.getBody());
			}
		}];
		return buttons;
    },
    
    save:function(isSave){
    	
    	
    	var url = '';
    	if(isSave){
    		Ext.getCmp('privileges').setValue(this.addPrivilegeSelector.getValue());
    		url = this.createURL+'?model.parent.id='+this.currentId+this.extraCreateParameters();
    	}else{
    		
    	}

    	//alert(url);
    	if (this.formpanel.form.isValid()) {
			this.formpanel.form.submit({
				url:url,
				method:'post',
				success:function(f,action) {
					Ext.ux.Toast.msg('操作提示：',action.result.message);
					this.closePopWin(Ext.getBody());
					this.store.reload();
					this.tree.getLoader().dataUrl = this.storeURL+'?time='+new Date().toString();
			        this.tree.getLoader().load(this.tree.root);
				},
				failure:function(f,action) {
					//alert("f")
					Ext.ux.Toast.msg('操作提示：',action.result.message);
			    },
			    scope:this
			});
		}
    },
    
    /****************************************/
    
    createTree:function(){
    	var tree = new Ext.tree.TreePanel({
            frame : true,// 美化界面
            split : true, //分隔条
            animate:true,
            autoScroll:true,
            collapsible : true,
            region:'west',
            width:200,
            loader: new Ext.tree.TreeLoader({dataUrl:this.storeURL}),
            //loader: new Ext.tree.DWRTreeLoader({dataUrl: treeNodeManager.getTree}),
            containerScroll: false,
            border: false,
            rootVisible: false
            
        });
    	var root = new Ext.tree.AsyncTreeNode({
            text: "角色",
            iconCls : 'role',
            draggable:false, // disable root node dragging
            id:'root'
        });
    	tree.setRootNode(root);
        tree.on('click',function(node, event){
            this.onClick(node, event);                    
        },this);
        return tree;
    },
    
    onClick: function(node, event){
    	this.select(node, event);
    },
    
    select: function(node,event){
        node.expand(false, true);
        this.currentNode=node;
        this.currentId=node.id.split("-")[1];
        this.currentName=node.text;
        this.grid.setTitle("已选中【"+this.currentName+"】");
        
    },
    
    
    /*************************************/
    
    getPanel:function(){
    	
    	this.tree = this.createTree();
    	//this.currentNode=this.tree.root;
        this.store = this.getStore();
		
		this.cm = this.getCM();
    	this.grid = this.getGrid();
    	
    	var panel = new Ext.Panel({
    		layout : 'border',
    		items:[this.tree,{
    			region:'center',
                autoScroll:true,
                layout: 'fit',
                items:[this.grid]
    		}]
    	});
    	return panel;
    },
	
	initComponent : function(){
		rolePanel.superclass.initComponent.call(this);
        this.panel = this.getPanel();
		
		this.add(this.panel);
	}
});