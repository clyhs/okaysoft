Ext.namespace("main");

westPanel = function(){
	var wpanel = new Ext.Panel({
		region : 'west',
	    id : 'west-panel', 
	    title : '功能菜单',
	    iconCls : 'menu',
	    split : true,
	    width : 200,
	    autoScroll : true,
	    layout : 'accordion',
	    collapsible : true,
	    margins : '0 0 0 2',
	    layoutConfig: {
	        animate: true
	    },
	    items : []
	});
	westPanel.superclass.constructor.call(this,wpanel);
};
Ext.extend(westPanel, Ext.Panel);