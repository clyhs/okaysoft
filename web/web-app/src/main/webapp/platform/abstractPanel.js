Ext.namespace("platform");

platform.abstractPanel = Ext.extend(Ext.Panel,{
	
	closable:true,
	autoScroll:true,
	layout:'fit',
	start:0,
	pageSize:17,
	
	chartData:'',
    chartWin:'',
    chartPanel:'',
    dataType:'',
    chartType:'',
    chartType2:'',
	
	/*************** Part I **********************/
	initPopWin:function(width,height,buttons){
		var win = new Ext.Window({
    		title: ' ',
            width:width,
            height:height,
            maximizable:true,
            plain: true,
            buttonAlign: 'center',
            closable: true,
            frame: true,
            layout: 'fit',
            border: false,
            modal: true,
            items: [this.formpanel],
            buttons:buttons
            
    	});
		return win;
	},
	/*初始化弹出窗口表单*/
	resetPopWin : function() {
		if (this.win)
			this.formpanel.form.reset();
	},
	/*关闭弹出窗口*/
	closePopWin : function(el) {
		if (this.win)
			this.win.close(el);
		this.win = null;
	},
	/*创建弹出窗口 private*/
	createPopWin : function(width,height,el,items,buttons) {
	
		if (!this.win) {
			if (!this.formpanel) {
				this.formpanel = this.createForm(items);// 子类
			}
			this.win = this.initPopWin(width,height,buttons);
			this.win.on("close", function() {
						this.formpanel = null;
						this.win = null;
					}, this);
		}
		//this.repeatPrimary = true;
		this.win.show(el);
	},
	
	/*打开录入弹出窗口 isSave判断是保存还是修改*/
	openPopWin:function(width,height,el,items,buttons) {
		this.createPopWin(width,height,el,items,buttons);
		this.resetPopWin();
	},
	
	save:function(){
		if (this.formpanel.form.isValid()) {
			this.formpanel.form.submit({
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
			alert('false');
		}
	},
	
	extraModifyParameters: function(){
        return "";
    },        
    extraDetailParameters: function(){
        return "";
    },      
    extraCreateParameters: function(){
        return "";
    },
    
    getFieldList: function(field){
        var recs = this.grid.getSelectionModel().getSelections();
        var list = [];
        if(recs.length > 0){
            for(var i = 0 ; i < recs.length ; i++){
            	
                var rec = recs[i];
                //alert(rec.get(field));
                list.push(rec.get(field))
            }
        }
        return list;
    },
    
    getIdList: function(){
        return this.getFieldList('id');
    },
    getToolbar:function(){
    	var toolbar = new Ext.Toolbar();
    	this.getCommands(toolbar,this.getCommandsStr(),this.getTipStr(),this.getCallbackStr());
        return toolbar;
    },
    getCommands:function(toolbar,commands,tips,callbacks){
    	if(commands==undefined || tips==undefined || callbacks==undefined){
            return;
        }
    	
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
    
    getBBar: function(){
    	
        return new Ext.ux.PageSizePlugin({
            rowComboSelect : true,
            pageSize : this.pageSize,
            store : this.store,
            displayInfo : true
        });
    	/*
    	return new Ext.PagingToolbar({
			pageSize: this.pageSize,
			store: this.store,
			grid: this.grid,
			displayInfo: true,
			displayMsg: '当前显示 {0} - {1}条记录&nbsp;&nbsp;共有 {2} 条记录',
			emptyMsg: "没有记录"
		})*/
    },
    getContextMenu: function(commands,tips,callbacks){
        //右键菜单
        var contextmenu=new Ext.menu.Menu({
            id:'theContextMenu',
            items:[]
        });
        this.getCommands(contextmenu,commands,tips,callbacks);
        return contextmenu;
    },
    
    /*************** Part II **********************/
    
    createTreeSelector:function(_id,_nodeText,_url,_rootNodeID,_rooNodeText,_label,_field,_anchor,_justLeaf){
    	var config={
    		id:_id,
    		store : new Ext.data.SimpleStore({
    			fields : [],
    			data : [[]]
    		}),
    		editable : false,
    		mode : 'local',
    		fieldLabel:_label,
    		emptyText : "请选择",
    		triggerAction : 'all',
    	    anchor: _anchor,
    	    maxHeight : 280,
    	    tpl : "<tpl for='.'><div style='height:280px'><div id='tree_"+_field+"'></div></div></tpl>",
    	    selectedClass : '',
    		onSelect : Ext.emptyFn,
    	    //以下方法用于修正点击树的加号后，下拉框被隐藏
    	    onViewClick : function(doFocus) {     
    	        var index = this.view.getSelectedIndexes()[0], s = this.store, r = s.getAt(index);     
    	        if (r) {     
    	             this.onSelect(r, index);     
    	        } else if (s.getCount() === 0) {     
    	            this.collapse();     
    	        }     
    	        if (doFocus !== false) {     
    	            this.el.focus();     
    	        }     
    	    }     
    	};
    	var comboxWithTree = new Ext.form.ComboBox(config);

    	var tree = new Ext.tree.TreePanel({
    		id:'selectTree',
    		height:280,
    		autoScroll: true,
    		split: true,
    		loader: new Ext.tree.TreeLoader({url:_url}),
                root:
                    new Ext.tree.AsyncTreeNode({
                        id:_rootNodeID,
                        text:_rooNodeText,
                        expanded: true
                    }),
                rootVisible: _rootNodeID!="root"
    	});
    	tree.on('click', function(node,e) {
            var editField = Ext.getCmp(_field);//根据要修改的域的ID取得该域
            if(node.id!=null && node.id!=''){
                if(_justLeaf && !node.isLeaf()){
                    //如果指定只能选叶子节点，当在点击了非叶子节点时没有反应
                }else{
                    comboxWithTree.setValue(node.text);
                    comboxWithTree.id=node.id;
                    comboxWithTree.collapse();
                    if(editField){
                        editField.setValue(node.id); //把树结点的值赋给要修改的域
                    }
                }
            }
            
        });
        comboxWithTree.on('expand', function() {
            tree.render('tree_'+_field);
            tree.getRootNode().expand(false,true);
        });
        comboxWithTree.setValue(_nodeText);
        return comboxWithTree
    },
	
    
    /*************** Part III **********************/
    
    getSingleChartTypeItems:function(){
    	var chartTypeItems=[
    	    {boxLabel: 'Column2D',id:'Column2D', name : "chartType", inputValue: 'Column2D',checked:this.type=='Column2D'},
            {boxLabel: 'Column3D',id:'Column3D', name : "chartType", inputValue: 'Column3D',checked:this.type=='Column3D'},
            {boxLabel: 'Line',id:'Line', name : "chartType", inputValue: 'Line',checked:this.type=='Line'},
            {boxLabel: 'Area2D',id:'Area2D', name : "chartType", inputValue: 'Area2D',checked:this.type=='Area2D'},
            {boxLabel: 'Bar2D',id:'Bar2D', name : "chartType", inputValue: 'Bar2D',checked:this.type=='Bar2D'},
            {boxLabel: 'Pie2D',id:'Pie2D', name : "chartType", inputValue: 'Pie2D',checked:this.type=='Pie2D'}
            
        ];
    	return chartTypeItems;
    },
    getSingleChartTypeItems2:function(){
    	var chartTypeItems=[
    	    
            {boxLabel: 'Pie3D',id:'Pie3D', name : "chartType", inputValue: 'Pie3D',checked:this.type=='Pie3D'},
            {boxLabel: 'Pareto2D',id:'Pareto2D', name : "chartType", inputValue: 'Pareto2D',checked:this.type=='Pareto2D'},
            {boxLabel: 'Pareto3D',id:'Pareto3D', name : "chartType", inputValue: 'Pareto3D',checked:this.type=='Pareto3D'},
            {boxLabel: 'Doughnut2D',id:'Doughnut2D', name : "chartType", inputValue: 'Doughnut2D',checked:this.type=='Doughnut2D'},
            {boxLabel: 'Doughnut3D',id:'Doughnut3D', name : "chartType", inputValue: 'Doughnut3D',checked:this.type=='Doughnut3D'}
        ];
    	return chartTypeItems;
    },
    
    getChartTypeItems:function(){
		var chartTypeItems=[
            {boxLabel: 'MS2D',id:"MSColumn2D",name : "chartType", inputValue: 'MSColumn2D',checked:this.type=='MSColumn2D'},
            {boxLabel: 'MS3D',id:"MSColumn3D", name : "chartType", inputValue: 'MSColumn3D',checked:this.type=='MSColumn3D'},
            {boxLabel: 'MSB2D',id:"MSBar2D", name : "chartType", inputValue: 'MSBar2D',checked:this.type=='MSBar2D'},
            {boxLabel: 'MSB3D',id:"MSBar3D", name : "chartType", inputValue: 'MSBar3D',checked:this.type=='MSBar3D'},
            {boxLabel: 'MSA',id:"MSArea", name : "chartType", inputValue: 'MSArea',checked:this.type=='MSArea'},
            {boxLabel: 'SA2D',id:"StackedArea2D", name : "chartType", inputValue: 'StackedArea2D',checked:this.type=='StackedArea2D'},
            {boxLabel: 'S3D',id:"StackedColumn3D", name : "chartType", inputValue: 'StackedColumn3D',checked:this.type=='StackedColumn3D'},
            {boxLabel: 'S2D',id:"StackedColumn2D", name : "chartType", inputValue: 'StackedColumn2D',checked:this.type=='StackedColumn2D'},
            {boxLabel: 'SB2D',id:"StackedBar2D", name : "chartType", inputValue: 'StackedBar2D',checked:this.type=='StackedBar2D'}
            ];
		return chartTypeItems;
	},
	
	getChartTypeItems2:function(){
		var chartTypeItems=[
            {boxLabel: 'SB3D',id:"StackedBar3D", name : "chartType", inputValue: 'StackedBar3D',checked:this.type=='StackedBar3D'},
            {boxLabel: 'Mk',id:"Marimekko", name : "chartType", inputValue: 'Marimekko',checked:this.type=='Marimekko'},
            {boxLabel: 'SS2D',id:"ScrollStackedColumn2D", name : "chartType", inputValue: 'ScrollStackedColumn2D',checked:this.type=='ScrollStackedColumn2D'},
            {boxLabel: 'S2D',id:"ScrollColumn2D", name : "chartType", inputValue: 'ScrollColumn2D',checked:this.type=='ScrollColumn2D'},
            {boxLabel: 'SA2D',id:"ScrollArea2D", name : "chartType", inputValue: 'ScrollArea2D',checked:this.type=='ScrollArea2D'},
            {boxLabel: 'SC2D',id:"ScrollCombi2D", name : "chartType", inputValue: 'ScrollCombi2D',checked:this.type=='ScrollCombi2D'},
            {boxLabel: 'SL2D',id:"ScrollLine2D", name : "chartType", inputValue: 'ScrollLine2D',checked:this.type=='ScrollLine2D'},
            {boxLabel: 'MSL',id:"MSLine", name : "chartType", inputValue: 'MSLine',checked:this.type=='MSLine'},
            {boxLabel: 'ZL',id:"ZoomLine", name : "chartType", inputValue: 'ZoomLine',checked:this.type=='ZoomLine'}
            ];
		return chartTypeItems;
	},
	getChartPanel : function(chartData,type){
        var chartPanel = new Ext.ux.Chart.Fusion.Panel({ 
              autoWidth:true,
              chartURL : "/okaysoft/FusionCharts/"+type+".swf", 
              chartData : chartData, 
              mediaCfg : { 
                 params : { 
                     scale : "exactfit" 
                 } 
              }
          }); 
          return chartPanel;
    },
    
    getChartData : function(){ 
        var tip=Ext.Msg.wait("正在查询数据......", '请稍候');
        var xml="";
        Ext.Ajax.request({
            url : this.chartDataURL+this.category,
            method : 'POST',
            //同步方法
            async: false,
            params : {
                queryString : this.queryString
            },
            success:function(response, opts){
                xml=response.responseText;
            },
            scope:this
        });
        tip.hide();
        return xml;
    },
    
    /*type=MSColumn3D*/
	
    getChartWindow : function(title,iconCls){
        var window = new Ext.Window({ 
                title : title, 
                maximizable:true,
                buttonAlign: 'center',
                width:1000,
                height:500,
                iconCls:iconCls,
                plain: true,
                closable: true,
                frame: true,
                layout: 'form',
                border: false,
                modal: true,
                buttons: [{
                    text: '关闭',
                    iconCls:'cancel',
                    scope: this,
                    handler: function() {
                        window.close();
                    }
                }],
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
    
    /*地图*/
    
    
    
	initComponent : function(){
		platform.abstractPanel.superclass.initComponent.call(this);
		
	}
	
});


/*
var TreeSelector = function(_id,_nodeText,_url,_rootNodeID,_rooNodeText,_label,_field,_anchor,_justLeaf) {
	var config={
		id:_id,
		store : new Ext.data.SimpleStore({
			fields : [],
			data : [[]]
		}),
		editable : false,
		mode : 'local',
		fieldLabel:_label,
		emptyText : "请选择",
		triggerAction : 'all',
        anchor: _anchor,
		maxHeight : 280,
        tpl : "<tpl for='.'><div style='height:280px'><div id='tree_"+_field+"'></div></div></tpl>",
		selectedClass : '',
		onSelect : Ext.emptyFn,
                //以下方法用于修正点击树的加号后，下拉框被隐藏
                onViewClick : function(doFocus) {     
                    var index = this.view.getSelectedIndexes()[0], s = this.store, r = s.getAt(index);     
                    if (r) {     
                      this.onSelect(r, index);     
                    } else if (s.getCount() === 0) {     
                      this.collapse();     
                    }     
                    if (doFocus !== false) {     
                      this.el.focus();     
                    }     
                }     
	};
	var comboxWithTree = new Ext.form.ComboBox(config);

	var tree = new Ext.tree.TreePanel({
		id:'selectTree',
		height:280,
		autoScroll: true,
		split: true,
		loader: new Ext.tree.TreeLoader({url:_url}),
            root:
                new Ext.tree.AsyncTreeNode({
                    id:_rootNodeID,
                    text:_rooNodeText,
                    expanded: true
                }),
            rootVisible: _rootNodeID!="root"
	});
        
	tree.on('click', function(node,e) {
                var editField = Ext.getCmp(_field);//根据要修改的域的ID取得该域
                if(node.id!=null && node.id!=''){
                    if(_justLeaf && !node.isLeaf()){
                        //如果指定只能选叶子节点，当在点击了非叶子节点时没有反应
                    }else{
                        comboxWithTree.setValue(node.text);
                        comboxWithTree.id=node.id;
                        comboxWithTree.collapse();
                        if(editField){
                            editField.setValue(node.id); //把树结点的值赋给要修改的域
                        }
                    }
                }
                
	});
	comboxWithTree.on('expand', function() {
        tree.render('tree_'+_field);
        tree.getRootNode().expand(false,true);
	});
    comboxWithTree.setValue(_nodeText);
	return comboxWithTree
};*/