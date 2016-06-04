Ext.namespace("platform.geo");


layerPanel= Ext.extend(platform.abstractPanel,{
	storeURL:'/okaysoft/geo/layer!query.action',
	createURL:'/okaysoft/geo/layer!create.action',
	updatePartURL:'/okaysoft/geo/layer!updatePart.action?model.id=',
	deleteURL:'/okaysoft/geo/layer!delete.action',
	treeURL:'/okaysoft/geo/store!select.action',
	
	queryString:'',
	search:false,
	grid:'',
	model:'',
	storeId:'',
	storeName:'',
	storeGrid:'',
	
	getFields: function(){
        var fields=[
                {name: 'type'},
                {name: 'prefix'},
                {name: 'logotype'},
                {name: 'title'},
                {name: 'srs'}
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
		    {header: "类型", width: 10, dataIndex: 'type', renderer:this.renderType },
		    {header: "图层名", width: 30, dataIndex: 'prefix', sortable: true},
		    {header: "数据集类型", width: 30, dataIndex: 'logotype', sortable: true},
		    {header: "标题", width: 30, dataIndex: 'title', sortable: true},
		    {header: "投影坐标", width: 30, dataIndex: 'srs', sortable: true}
		    
		    ]);
		return cm;
	},
	
	renderType:function(value){
		var strIcon = '<img src="/okaysoft/'+value+'" />';
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
    	var tips=['浏览图层(A)','新建图层(A)'];
    	return tips;
    },
    
    getCommandsStr:function(){
    	var commands=["query","create"];
    	
    	return commands;
    },
    
    getCallbackStr:function(){
    	var callbacks = [this.showall,this.create];
    	return callbacks;
    },
    
    advancedsearch:function(){
    },
    showall:function(){
    	this.mapWin = this.createMapWindow();
    	this.mapWin.show(Ext.getBody());
    },
    chart:function(){
    	this.chartShow("增删改统计");
    },
    
    create:function(){
    	
    	if(this.storeId==''){
    		Ext.MessageBox.alert('提示', "请选择数据源");
    	}else{
    		this.storeWin=this.createStoreWin();
    		this.storeGrid.setTitle("【"+this.storeName+"】");
        	this.storeWin.show(Ext.getBody());
    	}
    	
    	
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
	
	
	
    createMapWindow:function(){
    	var map;
        var wms;
        var pureCoverage = false;

        format = 'image/png';
        var bounds = new OpenLayers.Bounds(
                113.852203202009, 22.8959660258002,
                113.904998803241, 22.9772903666881
            );
        var options = {
            controls: [],
            maxExtent: bounds,
            maxResolution: 0.0004237922075273,
            projection: "EPSG:4326",
            units: 'degrees'
        };
        map = new OpenLayers.Map('map', options);

        // setup tiled layer
        wms = new OpenLayers.Layer.WMS(
                "fconline - Tiled", "http://localhost:8080/okaysoft/gis/cly/wms",
                {
                    LAYERS: 'cly:building',
                    STYLES: '',
                    format: format
                },
                {
                    buffer: 0,
                    tiles: false,
                    displayOutsideMaxExtent: true,
                    isBaseLayer: true,
                    yx: { 'EPSG:4326': true }
                }
            );

        map.addLayers([wms]);
        map.addControl(new OpenLayers.Control.Navigation());
        map.addControl(new OpenLayers.Control.PanZoom());
        map.zoomToExtent(bounds);
    	
    	var mapPanel = new GeoExt.MapPanel({
    	    border: true,
    	    region: "center",
    	    //width:780,
    	    //height:450,
    	    // we do not want all overlays, to try the OverlayLayerContainer
    	    map: map,
    	    zoom: 2
    	});
    	
    	var window = new Ext.Window({ 
            title : 'map', 
            maximizable:true,
            buttonAlign: 'center',
            width:800,
            height:500,
            iconCls:'',
            plain: true,
            closable: true,
            frame: true,
            layout : 'border',
            border: false,
            modal: true,
            items:[mapPanel],
            keys:[{
                 key : Ext.EventObject.ENTER,
                 fn : function(){
                     window.close();
                 },
                 scope : this
            }]
      }); 
      return window;
    },
    
    
    /*******************tree*********************/
    
    createTree:function(){
    	var tree = new Ext.tree.TreePanel({
    		title:'数据',
            frame : true,// 美化界面
            split : true, //分隔条
            animate:true,
            autoScroll:true,
            collapsible : true,
            region:'west',
            width:200,
            loader: new Ext.tree.TreeLoader({dataUrl:this.treeURL}),
            //loader: new Ext.tree.DWRTreeLoader({dataUrl: treeNodeManager.getTree}),
            containerScroll: false,
            border: false,
            rootVisible: true
            
        });
    	var root = new Ext.tree.AsyncTreeNode({
            text: "数据源",
            iconCls : 'data',
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
        this.storeId=node.id;
        this.storeName=node.text;
        this.grid.setTitle("已选中【"+this.storeName+"】");
        
        
    },
    
    
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
    
    /******************end tree*******************/
    
    
    getStoreFields: function(){
        var fields=[
                {name: 'localPart'},
                {name: 'published'}
            ];
       return fields;     
    },
    getStoreCM:function(){
		var cm =  new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer({
		    	header : '行号',
                width : 40
		    }),
		    new Ext.grid.CheckboxSelectionModel(),
		    {header: "图层名", width: 10, dataIndex: 'localPart', sortable:true },
		    {header: "是否发布", width: 30, dataIndex: 'published', sortable: true}
		    
		    ]);
		return cm;
	},
	
	getStoreLayer:function(){
		

		var store = new Ext.data.Store({
            reader: new Ext.data.JsonReader({
                totalProperty: 'totalProperty',
                root: 'root'
            },
            Ext.data.Record.create(this.getStoreFields())
            ),
            proxy : new Ext.data.HttpProxy({
                url : '/okaysoft/geo/layer!find.action?storeId='+this.storeId
            })
        });
		store.load({
			params : {
				limit : this.pageSize
			}
		});
		
	    return store;
	},
	
    getStoreGrid:function(){
		
		var cb = new Ext.grid.CheckboxSelectionModel();
		
		var grid = new Ext.grid.GridPanel({
			title:' ',
            autoHeight: true,
            frame:true,
			store: this.storeLayer,
			sm : cb,
            
			cm: this.storeCM,
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
			bbar:this.getStoreBBar()
		});
		
		return grid;
	},
	
    getStoreBBar: function(){
    	
    	
    	
        return  new Ext.PagingToolbar({             //-======定义翻页控件
            pageSize:this.pageSize,                         // 参数1：每页显示数
            store: this.storeLayer,                         // 数据源---非常重要
            displayInfo:true,
            displayMsg:'显示第{0}条数据到{1}条数据,一共有{2}条',
            emptyMsg:'没有记录'
        }) 
    	
    	
    },
	
	createStoreWin:function(){
		
		this.storeCM = this.getStoreCM();
		this.storeLayer = this.getStoreLayer();
		
		this.storeGrid = this.getStoreGrid();
		
		this.layerWin = this.createLayerWin(this.createLayerItems(),'name');
		
		var toolbar = new Ext.Toolbar();
		toolbar.add({
			text: '发布图层',
            iconCls:'create',
            scope: this,
            handler: function() {
            	
            	this.storeWin.close(Ext.getBody());
            	this.layerWin.show(Ext.getBody());
            }
		});
		
		var win = new Ext.Window({
    		title: ' ',
            width:600,
            height:500,
            maximizable:true,
            plain: true,
            buttonAlign: 'center',
            closable: true,
            frame: true,
            layout: 'fit',
            border: false,
            modal: true,
            items: [this.storeGrid],
            tbar:toolbar
            
            
    	});
		return win;
	},
	
	/**********************************************/
	
	createLayerWin:function(items,name){
		
		var form = new Ext.form.FormPanel({
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
		
		
		var win = new Ext.Window({
    		title: ' ',
            width:600,
            height:500,
            maximizable:true,
            plain: true,
            buttonAlign: 'center',
            closable: true,
            frame: true,
            layout: 'fit',
            border: false,
            modal: true,
            items: [form]
            
            
    	});
		return win;
	},
	
	createLayerItems:function(){
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
                items:[]
			}]
		}];
		return items;
	},
    
	
	
	initComponent : function(){
		layerPanel.superclass.initComponent.call(this);
        this.panel = this.getPanel();
		
		this.add(this.panel);
	}
});