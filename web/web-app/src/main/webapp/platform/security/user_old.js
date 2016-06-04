items:[{
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
                        }/*,{
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
                            },
                            {
                                cls : 'attr',
                                name: 'confirmPassword',
                                id: 'confirmPassword',
                                fieldLabel: '确认密码',
                                blankText : '确认密码不能为空',
                                inputType : 'password'
                            },{
                                xtype:'textfield',
                                name: 'model.org.id',
                                id:'model.org.id',
                                hidden: true,
                                hideLabel:true
                            }]
                        }*/]
                    },{
                    	xtype:'textfield',
                        allowBlank: true,
                        name: 'model.des',
                        fieldLabel: '备注',
                        anchor:"95%"
                    }]
	        	}/*,{
	        		xtype: 'fieldset',
                    id:'userGroupSelectorSet',
                    title: '选择用户组',
                    collapsible: true,
                    items:[this.userGroupSelector,{
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
                    items:[this.roleSelector,{
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
                        this.positionSelector,{
                        xtype: 'textfield',
                        name: 'positions',
                        id:'positions',
                        hidden: true,
                        hideLabel:true
                    }]
                }*/]
	        }],
	        
	        
	        
	        /**    create user model****/
	        /*
	        createUserPanel = function(){
	        	
	        	this.createURL = '/okaysoft/security/user!create.action';

	        	this.roleLoader = new Ext.tree.TreeLoader({
	                dataUrl:selectRoleStoreURL
	            });
	        	this.roleSelector = new Ext.ux.tree.CheckTreePanel({
	                title : '',
	                id : "roleSelector",
	                bubbleCheck:'none' ,
	                cascadeCheck:'all',
	                deepestOnly:'true',
	                rootVisible : false,
	                loader : this.roleLoader,
	                root : new Ext.tree.AsyncTreeNode({
	                    text:'角色',
	                    id : 'root',
	                    expanded : true
	                })
	            });
	        	
	        	/**/
	        	this.userGroupLoader = new Ext.tree.TreeLoader({
	                dataUrl:selectUserGroupURL
	            });
	        	this.userGroupSelector = new Ext.ux.tree.CheckTreePanel({
	                title : '',
	                id : "userGroupSelector",
	                rootVisible : false,
	                loader : this.userGroupLoader,
	                root : new Ext.tree.AsyncTreeNode({
	                    text:'用户组',
	                    id : 'root',
	                    expanded : true
	                })
	            });
	        	this.userGroupSelector.reset=function(){
	                this.clearValue();
	            };
	            
	            /**/
	        	
	        	this.positionLoader = new Ext.tree.TreeLoader({
	                dataUrl:selectPositionURL
	            }); 
	        	this.positionSelector = new Ext.ux.tree.CheckTreePanel({
	                title : '',
	                id : "positionSelector",
	                bubbleCheck:'none' ,
	                cascadeCheck:'all',
	                deepestOnly:'true',
	                rootVisible : false,
	                loader : this.positionLoader,
	                root : new Ext.tree.AsyncTreeNode({
	                    text:'岗位',
	                    id : 'root',
	                    expanded : true
	                })
	            });
	        	this.positionSelector.reset=function(){
	                this.clearValue();
	            };

	           
	        	
	        	this.frm = function(){
	        		var form =new Ext.form.FormPanel({
	        			id:'userAddForm',
	        			labelAlign: 'left',
	        	        buttonAlign: 'center',
	        	        bodyStyle: 'padding:5px',
	        	        frame: true,//圆角和浅蓝色背景
	        	        labelWidth: 80,
	        	        autoScroll:true,
	        	        defaults: {
	        	            anchor: '95%'
	        	        },
	        	        
	        	        items:[],
	        	        buttons:this.getButtons()
	        		});
	        		return form;
	        		
	        	};
	        	

	        	
	        	this.getButtons = function(){
	        		var buttons = [{
	        			text: '保存',
	        			scope:this,
	        			iconCls:'save',
	        			handler:function(){
	        				this.save();
	        			}
	        		},{
	        			text: '重置',
	        			scope:this,
	        			iconCls:'reset',
	        			handler:function(){
	        				
	        			}
	        		},{
	        			text: '取消',
	        			scope:this,
	        			iconCls:'cancel',
	        			handler:function(){
	        				
	        			}
	        		}];
	        		return buttons;
	        	};
	        	
	        	this.save = function(){
	        		
	        		var flag = true;
	        		
	        		//alert(this.roleSelector.getValue());
	        		/*
	        		var password = Ext.getCmp('password').getValue();
	        		var confirmPassword=Ext.getCmp('confirmPassword').getValue();
	        		if(confirmPassword!=password){
	                    Ext.MessageBox.alert('提示', "密码输入不一致");
	                    flag = false;
	                }else{
	                	Ext.getCmp('roles').setValue(this.roleSelector.getValue());
	                    Ext.getCmp('positions').setValue(this.positionSelector.getValue());
	                    Ext.getCmp('userGroups').setValue(this.userGroupSelector.getValue());
	                    //alert(Ext.getCmp('roles').getValue());
	                    flag = true;
	                }*/
	        		
	        		if(flag){
	        			if(this.frm().getForm().isValid()){
	        				alert("1");
	        				this.frm().getForm().submit({
	        					url:'/okaysoft/security/user!create.action',
	        					method:'post',
	        					success:function(f,action) {
	        						alert("c");
	        					},
	        					failure:function(f,action) {
	        						alert('failure2');
	        					}
	        				});
	        			}else{
	        				alert('2');
	        			}
	        			
	        		}else{
	        			alert(flag);
	        		}
	        		
	        	};
	        		
	        	
	        	var dlg = new Ext.Window({
	        		title: ' ',
	                width:600,
	                height:500,
	                maximizable:true,
	                plain: true,
	                closable: true,
	                frame: true,
	                layout: 'fit',
	                border: false,
	                modal: true,
	                items: [this.frm()]
	        	});
	        	
	        	createUserPanel.superclass.constructor.call(this,dlg);
	        };
	        Ext.extend(createUserPanel, Ext.Window);