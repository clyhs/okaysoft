Ext.namespace("platform.security");

var selectRoleStoreURL='/okaysoft/security/role!store.action?recursion=true';
var selectUserGroupURL=contextPath + '/security/user-group!store.action';
var selectPositionURL =contextPath + '/security/position!store.action?recursion=true';
var selectOrgStoreURL=contextPath+'/security/org!store.action';
var rootNodeID="root";
var rootNodeText="组织架构";
var orgId="-1";

userPanel = Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/security/user!query.action',
	queryURL:'/okaysoft/security/user!query.action',
	createURL:'/okaysoft/security/user!create.action',
	findURL:'/okaysoft/security/user!find.action?model.id=',
	updatePartURL:'/okaysoft/security/user!updatePart.action?model.id=',
	exportURL:'/okaysoft/security/user!export.action',
	deleteURL:'/okaysoft/security/user!delete.action',
	resetURL:'/okaysoft/security/user!reset.action',
	
	queryString:'',
	search:false,
	pageSize:'17',
	orgId:"-1",
	grid:'',
	model:'',
	
	getFields:function(){
		var fields =[
		 	{name: 'id'},
		 	{name: 'version'},
			{name: 'username'},
			{name: 'realName'},
			{name: 'enabled'},
			{name: 'roles'},
			{name: 'positions'},
			{name: 'orgName'},
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
                url : this.storeURL+this.storeURLParameter
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
		    {header: "编号", width: 10, dataIndex: 'id', sortable: true},
		    {header: "版本", width: 10, dataIndex: 'version', sortable: true},
			{header: "账号", width: 20, dataIndex: 'username', sortable: true},
			{header: "姓名", width: 20, dataIndex: 'realName', sortable: true,editor:new Ext.form.TextField()},
			{header: "状态", width: 20, dataIndex: 'enabled', sortable: true},
			{header: "拥有角色", width: 40, dataIndex: 'roles', sortable: true},
			{header: "拥有岗位", width: 40, dataIndex: 'positions', sortable: true},
			{header: "组织架构", width: 40, dataIndex: 'orgName', sortable: true},
			{header: "描述", width: 40, dataIndex: 'des', sortable: true,editor:new Ext.form.TextField()}
		    ]);
		return cm;
	},
    
    create:function(){
    	this.openPopWin(600,500,Ext.getBody(),this.getItems(),this.getButtons('保存'));
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
                    this.openPopWin(600,500,Ext.getBody(),this.getModifyItems(this.model),this.getButtons('修改'));
                },
                scope:this   ////如果不加这个，上面的窗口不能弹出来，函数也无法使用
            });
        }else{
            Ext.ux.Toast.msg('操作提示：','只能选择一个要进行操作的记录！');  
        }
    	
    },
    advancedsearch:function(){
    	this.openPopWin(300,200,Ext.getBody(),this.getAdvanceSearchItems(),this.getButtons('搜索'));
    },
    showall:function(){
    	this.queryString="";
    	this.search = false;
    	this.store.load({params:{
            limit:this.pageSize,
            queryString:this.queryString,
            search:this.search
        }});
        this.grid.getView().refresh();
    },
    exportData:function(){
    	alert(this.queryString);
    	Ext.Ajax.request({
            url :this.exportURL+'?time='+new Date().toString(),
            waitTitle: '请稍等',
            waitMsg: '正在导出数据……',
            params : {
                queryString : this.queryString,
                search : this.search
            },
            method : 'POST',
            success:function(response, opts){
                var path = response.responseText;
                //contextPath定义在引用了此JS的页面中
                path=contextPath+path;
                window.open(path,'_blank','width=1,height=1,toolbar=no,menubar=no,location=no');
            },
            failure : function(response,options){
                Ext.ux.Toast.msg('操作提示：', "导出失败");
            }
        });
    },
    reset:function(){
    	var idList=this.getIdList();
        if(idList.length<1){
            Ext.ux.Toast.msg('操作提示：','请选择要进行操作的记录');  
            return ;
        }
        Ext.MessageBox.confirm("操作提示：","确实要对所选的用户执行密码重置操作吗？",function(button,text){
            if(button == "yes"){
                Ext.Msg.prompt('操作提示', '请输入重置密码:', function(btn, text){
                    if (btn == 'ok'){
                        if(text.toString()==null||text.toString().trim()==""){
                            Ext.ux.Toast.msg('操作提示：','密码不能为空'); 
                        }else{
                        	//alert("123");
                            this.resetPassword(idList.join(','),text);
                        }
                    };
                },this);
            }
        },this);
    },
    
    resetPassword: function(ids,password){
    	//alert("1");
        Ext.Ajax.request({
            url : this.resetURL+'?time='+new Date().toString(),
            waitTitle: '请稍等',
            waitMsg: '正在重置密码……',
            params : {
                ids : ids,
                password : password
            },
            method : 'POST',
            success : function(response,opts){
                var data=response.responseText;
                Ext.ux.Toast.msg('操作提示：','{0}',data);
            	//alert("c");
            },
            failure:function(response,opts){
            	var data=response.responseText;
                Ext.ux.Toast.msg('操作提示：',data); 
            	//alert("f");
            },
            scope:this
        });
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
			//tbar:this.getToolbar()
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
    	var tips=['增加(C)','删除(R)','修改(U)','高级搜索(S)','显示全部(A)','导出(E)',"重置密码(Z)"];
    	return tips;
    },
    
    getCommandsStr:function(){
    	var commands=["create","delete","updatePart","search","query","export","reset"];
    	return commands;
    },
    
    getCallbackStr:function(){
    	var callbacks = [this.create,this.remove,this.modify,this.advancedsearch,this.showall,this.exportData,this.reset];
    	return callbacks;
    },
    
    
    
    
    
    
	
	
	getPlugins: function(){
        //return [Ext.ux.plugins.Print];
        return [];
    },
    
    getOrgSelector:function(isSave,model){
    	if(isSave){
    		//var orgSelector=new TreeSelector('model.org.name','',selectOrgStoreURL,rootNodeID,rootNodeText,"组织架构",'model.org.id','95%');   	
    		var orgSelector= this.createTreeSelector('model.org.name','',selectOrgStoreURL,rootNodeID,rootNodeText,"组织架构",'model.org.id','95%')
    	}else{
    		//var orgSelector=new TreeSelector('model.org.name',model.orgName,selectOrgStoreURL,rootNodeID,rootNodeText,"组织架构",'model.org.id','95%');
    		var orgSelector=this.createTreeSelector('model.org.name',model.orgName,selectOrgStoreURL,rootNodeID,rootNodeText,"组织架构",'model.org.id','95%');
    	}
    	return orgSelector;
    	
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
    
    getUserGroupSelector:function(isSave,model){
    	var userGroupLoader = new Ext.tree.TreeLoader({
            dataUrl:selectUserGroupURL
        });
    	var userGroupSelector = new Ext.ux.tree.CheckTreePanel({
            title : '',
            id : "userGroupSelector",
            rootVisible : false,
            loader : userGroupLoader,
            root : new Ext.tree.AsyncTreeNode({
                text:'用户组',
                id : 'root',
                expanded : true
            })
        });
    	userGroupSelector.reset=function(){
            this.clearValue();
        };
        if(!isSave){   	
        }
        return userGroupSelector;
    },
    
    getPositionSelector:function(isSave,model){
    	var positionLoader = new Ext.tree.TreeLoader({
            dataUrl:selectPositionURL
        }); 
    	var positionSelector = new Ext.ux.tree.CheckTreePanel({
            title : '',
            id : "positionSelector",
            bubbleCheck:'none' ,
            cascadeCheck:'all',
            deepestOnly:'true',
            rootVisible : false,
            loader : positionLoader,
            root : new Ext.tree.AsyncTreeNode({
                text:'岗位',
                id : 'root',
                expanded : true
            })
        });
    	positionSelector.reset=function(){
            this.clearValue();
        };
        
        if(!isSave){
        	positionLoader.on("load",function(){
                //在数据装载完成并展开树之后再设值
                positionSelector.getRootNode().expand(true,true);
                if(model.positions!=undefined && model.positions.toString().length>1){
                    positionSelector.setValue(model.positions);
                }
                positionSelector.bubbleCheck='none';
                positionSelector.cascadeCheck='all';
            });
        } 
        return positionSelector;
    },
    
    getItems:function(){
    	
    	
        
    	
    	this.addOrgSelector=this.getOrgSelector(true, null);
    	this.addRoleSelector = this.getRoleSelector(true,null);    	
    	this.addUserGroupSelector = this.getUserGroupSelector(true, null);
        this.addPositionSelector = this.getPositionSelector(true,null);
    	
    	var items=[{
    		layout: 'form',
    		items:[{
    			xtype: 'fieldset',
                id:'baseInfo',
                title: '基本信息',
                collapsible: true,
                defaults: {
                    allowBlank: false,
                    anchor: '95%'
                },
                items:[{
                	layout:'column',
                    defaults: {width: 250},
                    items:[{
                    	columnWidth:.5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor:"90%"
                        },
                        items:[{
                            cls : 'attr',
                            name: 'model.username',
                            fieldLabel: '账号',
                            blankText : '账号不能为空'
                        },
                        {
                            cls : 'attr',
                            name: 'model.realName',
                            fieldLabel: '姓名',
                            blankText : '姓名不能为空'
                        }]
                    },{
                    	columnWidth:.5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor:"90%"
                        },
                        items:[{
                            cls : 'attr',
                            id:'password',
                            name: 'model.password',
                            fieldLabel: '密码',
                            blankText : '密码不能为空',
                            inputType : 'password'
                        },{
                            cls : 'attr',
                            name: 'confirmPassword',
                            id: 'confirmPassword',
                            fieldLabel: '确认密码',
                            blankText : '确认密码不能为空',
                            inputType : 'password'
                        },this.addOrgSelector,
                        {
                            xtype:'textfield',
                            name: 'model.org.id',
                            id:'model.org.id',
                            hidden: true,
                            hideLabel:true
                        }]
                    }]
                },{
                	xtype:'textfield',
                    allowBlank: true,
                    name: 'model.des',
                    fieldLabel: '备注',
                    anchor:"95%"
                }]
    		},{
        		xtype: 'fieldset',
                id:'userGroupSelectorSet',
                title: '选择用户组',
                collapsible: true,
                items:[this.addUserGroupSelector,{
                	xtype: 'textfield',
                    name: 'userGroups',
                    id:'userGroups',
                    hidden: true,
                    hideLabel:true
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
        	},{
                xtype: 'fieldset',
                id:'positionSelectorSet',
                title: '选择岗位',
                collapsible: true,
                items: [
                    this.addPositionSelector,{
                    xtype: 'textfield',
                    name: 'positions',
                    id:'positions',
                    hidden: true,
                    hideLabel:true
                }]
            }]
    	}];
    	return items;
    },
    
    getModifyItems:function(model){
    	
    	this.modifyOrgSelector=this.getOrgSelector(false, model); 	
    	this.modifyUserGroupSelector = this.getUserGroupSelector(false,model);
    	this.modifyRoleSelector = this.getRoleSelector(false,model);  	
    	this.modifyPositionSelector = this.getPositionSelector(false,model);
          	
    	var items = [{
    		layout: 'form',
    		items:[{
    			xtype: 'fieldset',
                id:'baseInfo',
                title: '基本信息',
                collapsible: true,
                defaults: {
                    allowBlank: false,
                    anchor: '95%'
                },
                items:[{
                	layout:'column',
                    defaults: {width: 250},
                    items:[{
                    	columnWidth:.5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor:"90%"
                        },
                        items:[{
                        	readOnly:true,
                            fieldClass:'detail_field',
                            name: 'model.username',
                            value: model.username,
                            fieldLabel: '账号'
                        },{
                        	xtype:'textfield',
                            name: 'model.realName',
                            value: model.realName,
                            fieldLabel: '姓名',
                            allowBlank: false,
                            blankText : '姓名不能为空'
                        }]
                    },{
                    	columnWidth:.5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor:"90%"
                        },
                        items:[this.modifyOrgSelector,{
                        	xtype:'textfield',
                            value: model.orgId,
                            name: 'model.org.id',
                            id:'model.org.id',
                            hidden: true,
                            hideLabel:true
                        }]
                    }]
                
                },{
                	xtype:'textfield',
                    allowBlank: true,
                    name: 'model.des',
                    value: model.des,
                    fieldLabel: '备注',
                    anchor:"95%"
                }]
    		},{
                xtype: 'fieldset',
                id:'userGroupSelectorSet',
                title: '选择用户组',
                collapsible: true,
                items: [this.modifyUserGroupSelector,{
                    xtype: 'textfield',
                    name: 'userGroups',
                    id:'userGroups',
                    hidden: true,
                    hideLabel:true
                }]
            },{
                xtype: 'fieldset',
                id:'roleSelectorSet',
                title: '选择角色',
                collapsible: true,
                items: [this.modifyRoleSelector,{
                    xtype: 'textfield',
                    name: 'roles',
                    id:'roles',
                    hidden: true,
                    hideLabel:true
                }]
            },{
                xtype: 'fieldset',
                id:'positionSelectorSet',
                title: '选择岗位',
                collapsible: true,
                items: [this.modifyPositionSelector,{
                    xtype: 'textfield',
                    name: 'positions',
                    id:'positions',
                    hidden: true,
                    hideLabel:true
                }]
            }]
    	}];
    	return items;
    },
 
    getAdvanceSearchItems:function(){
    	var items = [{
            xtype: 'textfield',
            id:'search_username',
            fieldLabel: '账号'
        },
        {
            xtype: 'textfield',
            id:'search_realName',
            fieldLabel: '姓名'
        },TreeSelector('search_orgName','',selectOrgStoreURL,rootNodeID,rootNodeText,"组织架构名称",'model.org.id','95%'),{
        	xtype:'textfield',
            name: 'model.org.id',
            id:'model.org.id',
            hidden: true,
            hideLabel:true
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
    
    save:function(isSave){
    	var flag = true;
    	if(undefined==this.createURLParameter){
            this.createURLParameter="";
        }
    	var url = '';
    	if(isSave){
    		url = this.createURL+this.createURLParameter+this.extraCreateParameters();
    	}else{
    		url = this.updatePartURL+this.model.id+'&model.version='+this.model.version+this.extraModifyParameters();
    	}
    	
		if(isSave){
			var password = Ext.getCmp('password').getValue();
			var confirmPassword=Ext.getCmp('confirmPassword').getValue();
			if(confirmPassword!=password){
	            Ext.MessageBox.alert('提示', "密码输入不一致");
	            flag = false;
	        }else{
	        	Ext.getCmp('roles').setValue(this.addRoleSelector.getValue());
	            Ext.getCmp('positions').setValue(this.addPositionSelector.getValue());
	            Ext.getCmp('userGroups').setValue(this.addUserGroupSelector.getValue());
	            //alert(this.addRoleSelector.getValue());
	            flag = true;
	        }
		}else{
			Ext.getCmp('roles').setValue(this.modifyRoleSelector.getValue());
            Ext.getCmp('positions').setValue(this.modifyPositionSelector.getValue());
            Ext.getCmp('userGroups').setValue(this.modifyUserGroupSelector.getValue());
			flag = true;
		}
		
		if(flag){
			if (this.formpanel.form.isValid()) {
				this.formpanel.form.submit({
					url:url,
					method:'post',
					success:function(f,action) {
						Ext.ux.Toast.msg('操作提示：',action.result.message); 
						this.store.reload();
				        this.closePopWin(Ext.getBody());
					},
					failure:function(f,action) {
						Ext.ux.Toast.msg('操作提示：',action.result.message);
				    },
				    scope:this
				});
			}
		}
    },
    
    searchSubmit:function(){
    	var data=[];

        var search_username=Ext.getCmp('search_username').getValue();
        if(search_username!=""){
            search_username=' username:'+search_username;
            data.push(search_username);
        }

        var search_realName=Ext.getCmp('search_realName').getValue();
        if(search_realName!=""){
            search_realName=' realName:'+search_realName;
            data.push(search_realName);
        }

        var search_orgName=Ext.getCmp('search_orgName').getValue();
        if(search_orgName!=""){
            search_orgName=' org.orgName:'+search_orgName;
            data.push(search_orgName);
        }
        
        var queryString = "";
        var alias = 'User';

        for(var i=0;i<data.length;i++){
            if(data[i]!=""){
                    queryString+=data[i];
            }
        }
        
        this.queryString = queryString;
        //alert(queryString);
        this.search = true;
        this.store.load({params:{
            limit:this.pageSize,
            queryString:this.queryString,
            search:this.search
        }});
        this.grid.getView().refresh();
        this.closePopWin(Ext.getBody());
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
				if(text == '搜索'){
					this.searchSubmit();
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
    
	initComponent : function(){
		userPanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		
		this.cm = this.getCM();
		//this.grid = this.getGrid();
		this.grid = this.getGrid();
		
		this.add(this.grid);
		
	}
});


