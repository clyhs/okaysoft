Ext.namespace("main");

var last;
var model;
var maxWindows=15;
var id =1;
var view;
var contextPath ="/okaysoft";

var storeTheme=Ext.util.Cookies.get('theme');
if(storeTheme==null || storeTheme==''){
    storeTheme='ext-all';
}

mainView = Ext.extend(Ext.Viewport,{
	layout:'border',
	
	header:null,
	south:null,
	west:null,
	center:null,
	
	initComponent : function(){
		mainView.superclass.initComponent.call(this);
		this.header = new headerPanel();
		this.south  = new southPanel();
		this.west = new westPanel();
		this.center = new tab();
		this.add(this.header);
		this.add(this.west);
		this.add(this.center);
		this.add(this.south);
		this.doLayout();
		
	},
	
	getCenterPanel:function(){
		return this.center;
	},
	getWestPanel:function(){
		return this.west;
	}
});

Ext.onReady(function(){
    Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../extjs/images/default/s.gif';
	
	view = new mainView();
	
	Ext.Ajax.request({
		url : '/okaysoft/module/module!query.action?node=root&recursion=false',
		success : function(response, options) {
			var arr = eval(response.responseText);
			var activedPanelId = Ext.util.Cookies.get("activedPanelId");
			for (var i = 0; i < arr.length; i++) {
				var panel = new Ext.tree.TreePanel({
                    id : arr[i].id,
                    title : arr[i].text,
                    iconCls : arr[i].iconCls,
                    autoScroll : true,
                    border : false,
                    loader : new Ext.tree.TreeLoader({
                            dataUrl : contextPath + '/module/module!query.action?recursion=true'
                    }),
                    root : new Ext.tree.AsyncTreeNode({
                            text:arr[i].text,
                            id : arr[i].id,
                            iconCls : arr[i].iconCls,
                            expanded : true
                    }),
                    listeners : {
                            'click' : function(node, event){
                                node.toggle();
                            }
                    },
                    rootVisible : false
               });
				view.getWestPanel().add(panel);
				panel.on('expand', function(p) {
                    Ext.util.Cookies.set('activedPanelId', p.id, new Date(new Date().getTime()+(1000*60*60*24*7)), contextPath);
                });
                // 激活上次点击的panel
                if (arr[i].id == activedPanelId) {
                	view.getWestPanel().layout.activeItem = panel;
                }
				
			}
			view.getWestPanel().doLayout();
		}
	});
	
	
	refreshTheme();
	changeTime();
	Ext.get('loading-mask').fadeOut({
        remove : true
    });
});