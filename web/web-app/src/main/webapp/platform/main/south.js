Ext.namespace("main");

southPanel = function(){
	var spanel = new Ext.Panel({
		region:"south",
	    height:28,
	    border:false,
	    bbar:[{
	    	text:"退出系统",
	        iconCls:"logout",
	        handler:function(){
	        	
	        	window.location.href="../j_spring_security_logout";
	        	
	        }
	    },"-",{
	    	text:"在线用户",
	        iconCls:"onlineUser",
	        handler:function(){
	            //new OnlineChatWindow().show();
	            
	        }
	    },{
	    	text:"test",
	        iconCls:"test",
	        handler:function(){
	            //new OnlineChatWindow().show();
	        	view.getCenterPanel().openWindow('test','test',eval("structurePanel"));
	        }
	    },{
	    	text:"market",
	        iconCls:"market",
	        handler:function(){
	            //new OnlineChatWindow().show();
	        	view.getCenterPanel().openWindow('market','market',eval("marketPanel"));
	        }
	    },{
	    	xtype:'iconcombo',
            mode:"local",
            editable:false,
            textAlign:'right',
            style:'text-align: right;',
            value:storeTheme,
            width:105,
            triggerAction:"all",
            store : new Ext.data.SimpleStore({
                fields : ['id','text','icon'],
                data :[
                            ["ext-all","缺省浅蓝","default"],
                            ["xtheme-midnight","深蓝风格","midnight"],
                            ["xtheme-blue","紫色风格","blue"],
                            ["xtheme-tp","灰绿风格","tp"],
                            ["xtheme-pink","粉红风格","pink"],
                            ["xtheme-access","黑白风格","access"],
                            ["xtheme-orange","橙色风格","orange"],
                            ["xtheme-red5","红色风格","red5"]
                      ]
            }),
            valueField: 'id',
            displayField: 'text',
            iconClsField: 'icon',
            listeners:{
                scope:this,
                "select":function(d,b,c){
                    if(d.value!=""){
                        Ext.util.Cookies.set('theme', d.value, new Date(new Date().getTime()+(1000*60*60*24*7)), "/okaysoft/");
                        refreshAll();
                    }
                }
            }
	    }]
	});
	southPanel.superclass.constructor.call(this,spanel);
};
Ext.extend(southPanel, Ext.Panel);
