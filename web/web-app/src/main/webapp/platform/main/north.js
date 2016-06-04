Ext.namespace("main");

headerPanel = function(){
	
	var hpanel = new Ext.Panel({
		region:"north",
	    id:"northPanel",
	    contentEl:"north",
	    height:60
	});
	
	headerPanel.superclass.constructor.call(this,hpanel);
};
Ext.extend(headerPanel, Ext.Panel);