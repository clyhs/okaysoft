Ext.namespace("platform.monitor");


userLoginPanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/monitor/user-login!query.action',
	createURL:'/okaysoft/monitor/user-login!create.action',
	updatePartURL:'/okaysoft/monitor/user-login!updatePart.action?model.id=',
	deleteURL:'/okaysoft/monitor/user-login!delete.action',
	chartDataURL:'/okaysoft/monitor/user-login!chart.action?category=',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	
	
	chartTypeLabel:'用户登录统计',
	chartTypeColumns:10,
    chartTypeHeight:20,
	category:"loginTimes",
	dataTypeLabel:'数据类型',
    dataTypeColumns:9,
    dataTypeHeight:20,
    type:'Column3D',
    
    getFields: function(){
        var fields=[
                {name: 'id'},
				{name: 'ownerUser_username'},
				{name: 'loginIP'},
				{name: 'serverIP'},
				{name: 'loginTime'},
				{name: 'logoutTime'},
				{name: 'onlineTime'}
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
		    {header: "编号", width: 10, dataIndex: 'id', sortable: true},
			{header: "用户名称", width: 10, dataIndex: 'ownerUser_username', sortable: true},
			{header: "登录IP地址", width: 20, dataIndex: 'loginIP', sortable: true},
			{header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
			{header: "登录时间", width: 20, dataIndex: 'loginTime', sortable: true},
			{header: "注销时间", width: 20, dataIndex: 'logoutTime', sortable: true},
			{header: "用户在线时间", width: 20, dataIndex: 'onlineTime', sortable: true}
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
    	var tips=['高级搜索(S)','显示全部(A)','图表(T)'];
    	return tips;
    },
    
    getCommandsStr:function(){
    	var commands=["search","query","chart"];
    	
    	return commands;
    },
    
    getCallbackStr:function(){
    	var callbacks = [this.advancedsearch,this.showall,this.chart];
    	return callbacks;
    },
    
    advancedsearch:function(){
    	
    	
    },
    showall:function(){},
    chart:function(){
    	this.chartShow("用户登录统计");
    	//this.grid.hide();
    	//this.add();
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
	
	getDataTypeItems:function(){
		var dataTypeItems=[
            {boxLabel: '登录次数', name : "dataType", inputValue: 'loginTimes',checked:this.category=='loginTimes'},
            {boxLabel: '在线时间', name : "dataType", inputValue: 'onlineTime',checked:this.category=='onlineTime'}
        ];
		return dataTypeItems;
	},
	
	chartShow:function(title){
    	this.chartWin = this.getChartWindow(title,"userLogin");
    	this.chartWin.show();
    	this.chartData = this.getChartData();
    	//this.type="Column3D";
    	this.chartPanel = this.getChartPanel(this.chartData,this.type); 
    	
    	this.dataType={
                xtype: 'radiogroup',
                fieldLabel: this.dataTypeLabel,   
                autoWidth:true,
                columns : this.dataTypeColumns,             
                height:this.dataTypeHeight,
                items: this.getDataTypeItems(),
                listeners :{
                    'change':function(radioGroup,oldValue){
                            //category已经改变
                           this.category=radioGroup.getValue().inputValue;
                           //重新获取数据
                           //this.chartData=this.getChartData();
                           //删除原来的图表
                           this.chartWin.remove(this.chartPanel);
                           //重新获取图表
                           this.chartPanel = this.getChartPanel(this.chartData,this.type); 
                           //加入图表
                           this.chartWin.add(this.chartPanel);
                           //设置图表的高度
                           var height=this.chartWin.getHeight()-120;
                           this.chartPanel.setHeight(height); 
                           //显示图表
                           this.chartWin.doLayout();
                    },
                    scope:this
                }
            };
    	
    	this.chartType={
                xtype: 'radiogroup',   
                autoWidth:true,
                fieldLabel: this.chartTypeLabel,
                columns : this.chartTypeColumns,             
                height:this.chartTypeHeight,
                items: this.getSingleChartTypeItems(),
                listeners :{
                    'change':function(radioGroup,oldValue){
                           //图表类型已经改变
                           this.type=radioGroup.getValue().inputValue;
                           //删除原来的图表
                           this.chartWin.remove(this.chartPanel);
                           //this.chartData=this.getChartData();
                           //重新获取图表
                           this.chartPanel = this.getChartPanel(this.chartData,this.type); 
                           //加入图表
                           this.chartWin.add(this.chartPanel);
                           //设置图表的高度
                           var height=this.chartWin.getHeight()-120;
                           this.chartPanel.setHeight(height); 
                           //显示图表
                           this.chartWin.doLayout();
                    },
                    scope:this
                }
            };
    	
    	this.chartType2={
                xtype: 'radiogroup',   
                autoWidth:true,
                columns : this.chartTypeColumns,             
                height:this.chartTypeHeight,
                items: this.getSingleChartTypeItems2(),
                listeners :{
                    'change':function(radioGroup,oldValue){
                           //图表类型已经改变
                           this.type=radioGroup.getValue().inputValue;
                           //删除原来的图表
                           this.chartWin.remove(this.chartPanel);
                           this.chartData=this.getChartData();
                           //重新获取图表
                           this.chartPanel = this.getChartPanel(this.chartData,this.type); 
                           //加入图表
                           this.chartWin.add(this.chartPanel);
                           //设置图表的高度
                           var height=this.chartWin.getHeight()-120;
                           this.chartPanel.setHeight(height); 
                           //显示图表
                           this.chartWin.doLayout();
                    },
                    scope:this
                }
            };
    	
    	
    	var height=this.chartWin.getHeight()-120;
    	this.chartPanel.setHeight(height);
    	if(this.dataType){
    		this.chartWin.add(this.dataType);
         }
    	this.chartWin.add(this.chartType);
    	this.chartWin.add(this.chartType2);
    	this.chartWin.add(this.chartPanel);
          
    	this.chartWin.on("resize",function(){
            var height=this.chartWin.getHeight()-120;
            this.chartPanel.setHeight(height);
        },this)
    },
	
	initComponent : function(){
		runingTimePanel.superclass.initComponent.call(this);
		this.store = this.getStore();
		
		this.cm = this.getCM();
		this.grid = this.getGrid();
		this.add(this.grid);
	}
});