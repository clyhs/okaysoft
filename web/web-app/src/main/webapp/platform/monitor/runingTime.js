Ext.namespace("platform.monitor");


runingTimePanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/monitor/runing-time!query.action',
	createURL:'/okaysoft/monitor/runing-time!create.action',
	updatePartURL:'/okaysoft/monitor/runing-time!updatePart.action?model.id=',
	deleteURL:'/okaysoft/monitor/runing-time!delete.action',
	chartDataURL:'/okaysoft/monitor/runing-time!chart.action?category=',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	
	
	chartTypeLabel:'系统运行时间统计',
	chartTypeColumns:10,
    chartTypeHeight:20,
	category:"runingRate",
	dataTypeLabel:'数据类型',
    dataTypeColumns:9,
    dataTypeHeight:20,
    type:'Column3D',
    
    
    
	getFields: function(){
        var fields=[
                {name: 'id'},
				{name: 'serverIP'},
				{name: 'startupTime'},
				{name: 'shutdownTime'},
				{name: 'runingTime'}
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
			{header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
			{header: "系统启动时间", width: 20, dataIndex: 'startupTime', sortable: true},
			{header: "系统关闭时间", width: 20, dataIndex: 'shutdownTime', sortable: true},
			{header: "持续运行时间", width: 20, dataIndex: 'runingTime', sortable: true}
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
    
    advancedsearch:function(){},
    showall:function(){},
    chart:function(){
    	this.chartShow("系统运行时间统计");
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
	
	getDataTypeItems:function(){
		var dataTypeItems=[
            {boxLabel: '运行效率', name : "dataType", inputValue: 'runingRate',checked:this.category=='runingRate'},
            {boxLabel: '运行序列', name : "dataType", inputValue: 'runingSequence',checked:this.category=='runingSequence'}
        ];
		return dataTypeItems;
	},
	
	
	
	chartShow:function(title){
    	this.chartWin = this.getChartWindow(title,"runingTime");
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