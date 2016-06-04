Ext.namespace("platform.security");

userGroupPanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/security/user-group!query.action',
	createURL:'/okaysoft/security/user-group!create.action',
	
	updatePartURL:'/okaysoft/security/user-group!updatePart.action?model.id=',
	
	deleteURL:'/okaysoft/security/user-group!delete.action',
	
	
	queryString:'',
	search:false,
	pageSize:'17',
	orgId:"-1",
	grid:'',
	model:'',
	
	getFields:function(){
		var fields=[
	 		{name: 'id'},
	 		{name: 'version'},
			{name: 'userGroupName'},
			{name: 'roles'},
			{name: 'des'}
		];
		return fields;
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
                url : '/okaysoft/security/user-group!query.action'
            })
        });
		store.load({
			params : {
				limit : this.pageSize
			}
		});
	    return store;
	},
	
	getCM:function(){
		var cm =  new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer({
		    	header : '行号',
                width : 40
		    }),
		    new Ext.grid.CheckboxSelectionModel(),
		    {header: "编号", width: 5, dataIndex: 'id', sortable: true},
			{header: "版本", width: 5, dataIndex: 'version', sortable: true},
			{header: "用户组名称", width: 20, dataIndex: 'userGroupName', sortable: true,editor:new Ext.form.TextField()},
			{header: "拥有的角色", width: 20, dataIndex: 'roles', sortable: true},
			{header: "描述", width: 20, dataIndex: 'des', sortable: true,editor:new Ext.form.TextField()}
		    ]);
		return cm;
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
	
    create:function(){
    	this.openPopWin(500,280,Ext.getBody(),this.getCreateItems(),this.getButtons('保存'));
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
                //alert(ids);
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
                    },
                    scope:this
                });
            }
        },this);
    },
    modify:function(){
    	var idList=this.getIdList();
    	if(idList.length<1){
            Ext.ux.Toast.msg('操作提示：','请选择要进行操作的记录');  
            return ;
        }
    	
    	
    	if(idList.length==1){
            var id=idList[0];

        
            Ext.Ajax.request({
                url : this.findURL+id+this.extraModifyParameters(),
                waitTitle: '请稍等',
                waitMsg: '正在检索数据……',
                method : 'POST',
                success : function(response,options){
                	
                	//alert("c");
                    var data=response.responseText;
                    //返回的数据是对象，在外层加个括号才能正确执行eval
                    this.model=eval('(' + data + ')');
                    //alert(data);
                    this.openPopWin(500,280,Ext.getBody(),this.getModifyItems(this.model),this.getButtons('修改'));
                },
                scope:this   ////如果不加这个，上面的窗口不能弹出来，函数也无法使用
            });
        }else{
            Ext.ux.Toast.msg('操作提示：','只能选择一个要进行操作的记录！');  
        }
    },
    
    save:function(isSave){
    	
    	if(undefined==this.createURLParameter){
            this.createURLParameter="";
        }
    	
    	//alert(Ext.getCmp('roles').getValue());
    	var url = '';
    	if(isSave){
    		Ext.getCmp('roles').setValue(this.addRoleSelector.getValue());
    		url = this.createURL+this.createURLParameter+this.extraCreateParameters();
    	}else{
    		Ext.getCmp('roles').setValue(this.modifyRoleSelector.getValue());
    		url = this.updatePartURL+this.model.id+'&model.version='+this.model.version+this.extraModifyParameters();
    	}
    	//alert(url);
    	if (this.formpanel.form.isValid()) {
			this.formpanel.form.submit({
				url:url,
				method:'post',
				success:function(f,action) {
					Ext.ux.Toast.msg('操作提示：',action.result.message); 
					this.store.reload();
			        this.closePopWin(Ext.getBody());
					//alert("c");
				},
				failure:function(f,action) {
					alert("f")
					//Ext.ux.Toast.msg('操作提示：',action.result.message);
			    },
			    scope:this
			});
		}
    	
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
    
    getRoleSelector:function(isSave,model){
    	var roleLoader = new Ext.tree.TreeLoader({
            dataUrl:selectRoleStoreURL
        });
    	var roleSelector = new Ext.ux.tree.CheckTreePanel({
            title : '',
            id : "roleSelector",
            bubbleCheck:'none' ,
            cascadeCheck:'all',
            deepestOnly:'true',
            rootVisible : false,
            loader : roleLoader,
            root : new Ext.tree.AsyncTreeNode({
                text:'角色',
                id : 'root',
                expanded : true
            })
        });	
    	if(!isSave){
    		roleLoader.on("load",function(){
                //在数据装载完成并展开树之后再设值
                roleSelector.getRootNode().expand(true,true);
                if(model.roles!=undefined && model.roles.toString().length>1){
                    roleSelector.setValue(model.roles);
                }
                roleSelector.bubbleCheck='none';
                roleSelector.cascadeCheck='all';
            });
    	}
    	return roleSelector; 
    },
    
    
    getCreateItems:function(){
    	this.addRoleSelector = this.getRoleSelector(true,null);   
    	var items=[{
    		xtype: 'fieldset',
            id:'baseInfo',
            title: '基本信息',
            collapsible: false,
            defaults: {
                anchor: '95%'
            },
            items:[{
                xtype: 'textfield',
                name: 'model.userGroupName',
                fieldLabel: '用户组名称',
                allowBlank: false,
                blankText : '用户组名称不能为空'
            },{
                xtype: 'textfield',
                name: 'model.des',
                fieldLabel: '备注',
                allowBlank: true
            }]
    	},{
    		xtype: 'fieldset',
            id:'roleSelectorSet',
            title: '选择角色',
            collapsible: true,
            items:[this.addRoleSelector,{
                xtype: 'textfield',
                name: 'roles',
                id:'roles',
                hidden: true,
                hideLabel:true
            }]
    	}];
    	return items;
    },
    
    getModifyItems:function(model){
    	this.modifyRoleSelector = this.getRoleSelector(false,model);  
    	
    	var items=[{
    		xtype: 'fieldset',
            id:'baseInfo',
            title: '基本信息',
            collapsible: false,
            defaults: {
                anchor: '95%'
            },
            items:[{
                xtype: 'textfield',
                name: 'model.userGroupName',
                value:model.userGroupName,
                fieldLabel: '用户组名称',
                allowBlank: false,
                blankText : '用户组名称不能为空'
            },{
                xtype: 'textfield',
                name: 'model.des',
                value:model.des,
                fieldLabel: '备注',
                allowBlank: true
            }]
    	},{
    		xtype: 'fieldset',
            id:'roleSelectorSet',
            title: '选择角色',
            collapsible: true,
            items:[this.modifyRoleSelector,{
            	xtype: 'textfield',
                name: 'roles',
                id:'roles',
                hidden: true,
                hideLabel:true
            }]
    	}];
    	return items;
    },
    
	initComponent : function(){
		userGroupPanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		
		this.cm = this.getCM();
        this.grid = this.getGrid();
		
		this.add(this.grid);
	}
});