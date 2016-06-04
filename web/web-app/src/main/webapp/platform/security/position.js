Ext.namespace("platform.security");


positionPanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/security/position!query.action',
	createURL:'/okaysoft/security/position!create.action',
	
	updatePartURL:'/okaysoft/security/position!updatePart.action?model.id=',
	
	deleteURL:'/okaysoft/security/position!delete.action',
	currentNode:'',
	currentName:"角色",
	currentId:'1',
	queryString:'',
	search:false,
	pageSize:'17',
	
	grid:'',
	model:'',
	getFields: function(){
        var fields=[
            {name: 'id'},
            {name: 'version'},
            {name: 'positionName'}
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
            {header: "岗位名称", width: 40, dataIndex: 'positionName', sortable: true,editor:new Ext.form.TextField()}
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
		
		//this.store = this.getStore();
		
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
    
    create:function(){},
    remove:function(){},
    modify:function(){},
	
	initComponent : function(){
		positionPanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		
		this.cm = this.getCM();
		this.grid = this.getGrid();
		this.add(this.grid);
	}
	
});