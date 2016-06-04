Ext.namespace("platform.geo");


workspacePanel= Ext.extend(platform.abstractPanel,{
	storeURL:'/okaysoft/geo/workspace!query.action',
	createURL:'/okaysoft/geo/workspace!create.action',
	updatePartURL:'/okaysoft/geo/workspace!updatePart.action?model.id=',
	deleteURL:'/okaysoft/geo/workspace!delete.action',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	
	getFields: function(){
        var fields=[
                {name: 'name'},
                {name: 'default'}
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
		    {header: "名称", width: 10, dataIndex: 'name', sortable: true},
		    {header: "默认空间", width: 30, dataIndex: 'default', renderer:this.renderType}
		    
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
				limit : this.pageSize
			}
		});
	    return store;
	},
	
	getTipStr:function(){
    	var tips=['添加空间(A)'];
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
    	this.openPopWin(500,400,Ext.getBody(),this.getCreateItems(),this.getButtons('保存'));
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
	
	
	
	getCreateItems:function(){
		var items = [];
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
    	var url = this.createURL;
    	if (this.formpanel.form.isValid()) {
			this.formpanel.form.submit({
				url:url,
				method:'post',
				success:function(f,action) {
					Ext.ux.Toast.msg('操作提示：',action.result.message);
					this.closePopWin(Ext.getBody());
					this.store.reload();
				},
				failure:function(f,action) {
					//alert("f")
					Ext.ux.Toast.msg('操作提示：',action.result.message);
			    },
			    scope:this
			});
		}else{
			alert("2");
		}
    },

	
	initComponent : function(){
		workspacePanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		this.cm = this.getCM();
		this.grid = this.getGrid();
		this.add(this.grid);
	}
});