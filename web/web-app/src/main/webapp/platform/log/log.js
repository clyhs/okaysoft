Ext.namespace("platform.log");


operateLogPanel= Ext.extend(platform.abstractPanel,{
	
	storeURL:'/okaysoft/log/operate-log!query.action',
	createURL:'/okaysoft/log/operate-log!create.action',
	updatePartURL:'/okaysoft/log/operate-log!updatePart.action?model.id=',
	deleteURL:'/okaysoft/log/operate-log!delete.action',
	chartDataURL:'/okaysoft/log/operate-log!chart.action?category=',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	
	chartTypeLabel:'图表类型',
	chartTypeColumns:10,
    chartTypeHeight:20,
	category:"operateType",
	dataTypeLabel:'数据类型',
    dataTypeColumns:7,
    dataTypeHeight:20,
    type:'MSColumn3D',
    chartData:'',
    
	
	getFields: function(){
        var fields=[
                {name: 'id'},
				{name: 'ownerUser'},
				{name: 'loginIP'},
				{name: 'serverIP'},
				{name: 'operatingTime'},
 				{name: 'operatingType'},
				{name: 'operatingModel'},
				{name: 'operatingID'}
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
			{header: "用户名称", width: 20, dataIndex: 'ownerUser', sortable: true},
			{header: "登录IP地址", width: 20, dataIndex: 'loginIP', sortable: true},
			{header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
			{header: "操作时间", width: 20, dataIndex: 'operatingTime', sortable: true},
			{header: "操作类型", width: 10, dataIndex: 'operatingType', sortable: true},
			{header: "操作模型", width: 20, dataIndex: 'operatingModel', sortable: true},
			{header: "模型ID", width: 20, dataIndex: 'operatingID', sortable: true}
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
    	this.openPopWin(300,200,Ext.getBody(),this.getAdvanceSearchItems(),this.getButtons('搜索'));
    },
    showall:function(){},
    chart:function(){
    	this.chartShow("增删改统计");
    },
    
    
    getAdvanceSearchItems:function(){
    	var items=[];
        return items;
    },
    
    searchSubmit:function(){
    	var data=[];

        var search_ownerUser_username=Ext.getCmp('search_ownerUser_username').getValue();
        if(search_ownerUser_username!=""){
            search_ownerUser_username=' ownerUser_username:'+search_ownerUser_username;
            data.push(search_ownerUser_username);
        }

        var search_operatingType=Ext.getCmp('search_operatingType').getValue();
        if(search_operatingType!=""){
            search_operatingType=' operatingType:'+search_operatingType;
            data.push(search_operatingType);
        }

        var search_operatingModel=Ext.getCmp('search_operatingModel').getValue();
        if(search_operatingModel!=""){
            search_operatingModel=' operatingModel:'+search_operatingModel;
            data.push(search_operatingModel);
        }

        var search_startOperatingTime=Ext.getCmp('search_startOperatingTime').value;
        var search_endOperatingTime=Ext.getCmp('search_endOperatingTime').value;
        var search_operatingTime="";
        if(search_startOperatingTime!=undefined && search_startOperatingTime!="" && search_endOperatingTime!=undefined && search_endOperatingTime!=""){
            search_operatingTime=' +operatingTime:['+search_startOperatingTime+' TO '+search_endOperatingTime+']';
            data.push(search_operatingTime);
        }
        alert(data);
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
            {boxLabel: '用户', name : "dataType", inputValue: 'user',checked:this.category=='user'},
            {boxLabel: '操作类型', name : "dataType", inputValue: 'operateType',checked:this.category=='operateType'}
        ];
		return dataTypeItems;
	},
	
    
	chartShow:function(title){
    	this.chartWin = this.getChartWindow(title,"cud");
    	this.chartWin.show();
    	this.chartData = this.getChartData();
    	this.type="MSColumn3D";
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
                           this.chartData=this.getChartData();
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
                fieldLabel: this.chartTypeLabel,   
                autoWidth:true,
                columns : this.chartTypeColumns,             
                height:this.chartTypeHeight,
                items: this.getChartTypeItems(),
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
    	this.chartType2={
                xtype: 'radiogroup',  
                autoWidth:true,
                columns : this.chartTypeColumns,             
                height:this.chartTypeHeight,
                items: this.getChartTypeItems2(),
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
		operateLogPanel.superclass.initComponent.call(this);
        this.store = this.getStore();
		this.cm = this.getCM();
		this.grid = this.getGrid();
		this.add(this.grid);
	}
});