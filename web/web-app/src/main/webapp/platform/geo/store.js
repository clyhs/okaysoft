Ext.namespace("platform.geo");
var rootNodeID="root";
var rootNodeText="工作空间";

storePanel= Ext.extend(platform.abstractPanel,{
	storeURL:'/okaysoft/geo/store!query.action',
	createURL:'/okaysoft/geo/store!create.action',
	updatePartURL:'/okaysoft/geo/store!updatePart.action?model.id=',
	deleteURL:'/okaysoft/geo/store!delete.action',
	menuURL:'/okaysoft/geo/store!menu.action?type=1',
	workspaceSelectURL:'/okaysoft/geo/workspace!store.action',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	
	getFields: function(){
        var fields=[
                {name: 'data_type'},
                {name: 'name'},
                {name: 'wsname'},
                {name: 'type'},
                {name: 'enabled'}
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
		    {header: "数据类型", width: 10, dataIndex: 'data_type', sortable: true},
		    {header: "数据集名", width: 30, dataIndex: 'name' , sortable:true},
		    {header: "工作空间", width: 10, dataIndex: 'wsname', sortable: true},
		    {header: "类型", width: 10, dataIndex: 'type', sortable: true},
		    {header: "是否开启", width: 30, dataIndex: 'enabled',sortable: true}
		    
		    ]);
		return cm;
	},
	
	renderType:function(value){
		var strIcon = "";
		if(value!=""){
			strIcon = '<img src="/okaysoft/'+value+'" />';
		}
		 
		return strIcon;
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
                url : this.storeURL
            })
        });
		store.load({
			params : {
				limit :this.pageSize
			}
		});
	    return store;
	},
	
	getTipStr:function(){
    	var tips=['添加(A)'];
    	return tips;
    },
    
    getCommandsStr:function(){
    	var commands=["create"];
    	
    	return commands;
    },
    
    getCallbackStr:function(){
    	var callbacks = [this.create];
    	return callbacks;
    },
    
    create:function(){
    	
    },
    showall:function(){
    },
    chart:function(){
    },
    
    
    getAdvanceSearchItems:function(){
    	var items=[];
        return items;
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
	
	getCommands:function(toolbar,commands,tips,callbacks){
    	if(commands==undefined || tips==undefined || callbacks==undefined){
            return;
        }
    	/*
    	var menu = new Ext.ux.menu.StoreMenu({ 
    	    url:this.menuURL
    	});*/
    	var menu2 = new Ext.menu.Menu();
    	
    	Ext.Ajax.request({
    		url:this.menuURL,
    		success:function(response, options){
    			var arr = eval(response.responseText);
    			for(var i = 0; i < arr.length; i++){
    				menu2.add({
    					text:arr[i].text,
    					handler:this.createStore,
    					scope:this
    				});
    			}
    		},
    		failure:function(response, options){
    		},
    		scope:this
    	});
    	
    	var menu3 = new Ext.menu.Menu();
    	
    	Ext.Ajax.request({
    		url:'/okaysoft/geo/store!menu.action?type=2',
    		success:function(response, options){
    			var arr = eval(response.responseText);
    			for(var i = 0; i < arr.length; i++){
    				menu3.add({
    					text:arr[i].text,
    					handler:this.createStore,
    					scope:this
    				});
    			}
    		},
    		failure:function(response, options){
    		},
    		scope:this
    	});
    	
    	var menu = new Ext.menu.Menu({
    		items:[{
    			text:'矢量数据',
    			menu:menu2
    		},{
    			text:'栅格数据',
    			menu:menu3
    		}]
    	});
    	
    	toolbar.add({
    		text:'添加数据集',
    		iconCls:'create',
    		menu:menu
    	});
    	
    	for(var i=0;i<commands.length;i++){
            var command=commands[i];
            var tip=tips[i];
            var callback=callbacks[i];
            toolbar.add({  
                iconCls : command,  
                text : tip,  
                handler : callback,
                scope: this
            }); 
        }
    },
	
    createStore:function(item){
    	//alert(item.text);
    	var dataName = item.text;
    	if(dataName == 'PostGIS'){
    		//alert('create postgis data conn');
    		this.openPopWin(600,500,Ext.getBody(),this.getCreatePostGISItems(),this.getButtons('保存'));
    	}
    },
    
    getWSSelector:function(isSave,model){

    	var wsSelector= this.createTreeSelector('wsName','',this.workspaceSelectURL,rootNodeID,rootNodeText,"工作空间",'wsId','95%')
 
    	return wsSelector;
    	
    },
    
	getCreatePostGISItems:function(){
		
		this.addWSSelector = this.getWSSelector(true, null);
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
                        items:[this.addWSSelector,{
                        	xtype:'textfield',
                            name: 'wsId',
                            id:'wsId',
                            hidden: true,
                            hideLabel:true
                        },{
                        	xtype:'textfield',
                        	name:'name',
                        	fieldLabel:'数据源名',
                        	value:'',
                        	allowBlank: false,
                        	blankText : '数据名不能为空'
                        },{
                        	xtype:'checkbox',
                        	name:'enabled',
                        	fieldLabel:'开启',
                        	checked:true,
                        	id:'enabled',
                        	inputValue:true
                        }]
                    }]
                },{
                	xtype:'textfield',
                    allowBlank: true,
                    name: 'desc',
                    value: '',
                    fieldLabel: '备注',
                    anchor:"95%"
                }]
    		},{
    			xtype: 'fieldset',
                id:'conn',
                title: '数据库连接信息',
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
                        	xtype:'textfield',
                        	name:'url',
                        	fieldLabel:'连接地址',
                        	value:'localhost',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'port',
                        	fieldLabel:'端口',
                        	value:'5432',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'dataSrc',
                        	fieldLabel:'数据库',
                        	value:'',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'schema',
                        	fieldLabel:'schema',
                        	value:'public',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'username',
                        	fieldLabel:'用户名',
                        	value:'',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'password',
                        	fieldLabel:'密码',
                        	value:'',
                        	allowBlank: false,
                        	blankText : '不能为空',
                        	inputType : 'password'
                        },{
                        	xtype:'checkbox',
                        	name:'epk',
                        	fieldLabel:'暴露主键',
                        	checked:false,
                        	id:'epk',
                        	inputValue:true
                        },{
                        	xtype:'textfield',
                        	name:'maxConn',
                        	fieldLabel:'最大连接数',
                        	value:'10',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'minConn',
                        	fieldLabel:'最小连接数',
                        	value:'1',
                        	allowBlank: false,
                        	blankText : '不能为空'
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
                        	xtype:'textfield',
                        	name:'connTime',
                        	fieldLabel:'连接时间',
                        	value:'20',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'textfield',
                        	name:'fetchSize',
                        	fieldLabel:'条数',
                        	value:'1000',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'checkbox',
                        	name:'vConn',
                        	fieldLabel:'验证连接',
                        	checked:true,
                        	inputValue:true
                        	
                        },{
                        	xtype:'checkbox',
                        	name:'looseBbox',
                        	fieldLabel:'宽松bbox',
                        	checked:true,
                        	inputValue:true
                        },{
                        	xtype:'checkbox',
                        	name:'estimated',
                        	fieldLabel:'预计延伸',
                        	checked:true,
                        	inputValue:true
                        },{
                        	xtype:'checkbox',
                        	name:'pstate',
                        	fieldLabel:'预处理语句',
                        	checked:false,
                        	inputValue:true
                        },{
                        	xtype:'textfield',
                        	name:'mops',
                        	fieldLabel:'最大语句数',
                        	value:'50',
                        	allowBlank: false,
                        	blankText : '不能为空'
                        },{
                        	xtype:'checkbox',
                        	name:'encodefunctions',
                        	fieldLabel:'编码功能',
                        	checked:false,
                        	inputValue:true
                        }]
                    }]
                }]
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
    	alert('4');
    	if (this.formpanel.form.isValid()) {
			this.formpanel.form.submit({
				url:this.createURL,
				method:'post',
				success:function(f,action) {
					Ext.ux.Toast.msg('操作提示：','1'); 
				},
				failure:function(f,action) {
					Ext.ux.Toast.msg('操作提示：','2');
			    },
			    scope:this
			});
		}else{
			alert('3');
		}
    },

	
	initComponent : function(){
		storePanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		this.cm = this.getCM();
		this.grid = this.getGrid();
		this.add(this.grid);
	}
});