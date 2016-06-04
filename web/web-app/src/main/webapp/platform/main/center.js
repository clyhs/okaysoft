Ext.namespace("main");

tab = function(){
	
	this.openTab = function(node,event,panel){
		event.stopEvent();
		
		//alert(panel);
		
		this.openWindow(node.text,node.attributes.iconCls,panel);
	};
	
	this.openWindow = function(text,iconCls,panel){
		
		var exist=false;
	    this.items.each(function(item){
	        if(item.title==text && item.iconCls==iconCls){
	            this.setActiveTab(item);
	            exist=true;
	        }
	    },this);
	    if(exist){
	        return;
	    }
	    if(id>maxWindows){
	        if(this.getItem("windows"+(id-maxWindows))){
	            Ext.get("windows"+(id-maxWindows)).src = false;
	            this.remove("windows"+(id-maxWindows));
	        }
	    }
	   // Ext.Msg.wait('页面加载中...','请稍后');
        /*
	    if(url == null || url == "null" || url == ""){
	        url = "";
	    }*/
	    /*
	    n = tab.add({
	        'id':  "windows"+id,
	        'iconCls':iconCls,
	        'title': text,
	        closable: true,  //通过html载入目标页
	        scripts:true,
	        html: ''
	    });*/
	    var p = new panel();
		//Ext.Msg.alert('message',p);
		//p.id = Ext.id();
		p.id = "windows"+id;
		p.title = text;
		p.iconCls=iconCls;
		
		n = this.add(p);
		
	    this.setActiveTab(n);
	    id++;
	};
	
	this.closeWindow = function(text){
		this.items.each(function(item){
	        if(item.closable&&item.title==text)
	        {
	            this.remove(item);
	        }
	    });
	};
	var tpanel = new Ext.Panel({
		region:'center',
	    deferredRender:false,
	    activeTab:0,
	    resizeTabs:false,
	    minTabWidth: 115,
	    enableTabScroll:true,
	    items:[{
	        title:"我的桌面",
	        closable : false,
	        iconCls:'computer',
	        style : 'padding:4px 4px 4px 4px;',
	        xtype : 'portal',
	        region : 'center',
	        margins : '5 5 5 0',
	        layout:'fit',
	        items : [{
	            style : 'padding:0 0 10px 0',
	            items : [{
	                title : '系统运行情况',
	                autoScroll:false,
	                autoWidth:true,
	                height:470,
	                scripts:true,
	                html: 'hello world'
	            }]
	        }]
	    }]
	});
	tab.superclass.constructor.call(this,tpanel);
};
Ext.extend(tab, Ext.TabPanel);
