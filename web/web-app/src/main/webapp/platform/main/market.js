Ext.namespace("main");
marketPanel = Ext.extend(Ext.Panel, {
	closable:true,
	autoScroll:true,
	layout:'fit',
	border:false,
	
	initComponent:function(){
		this.panel = new Ext.Panel({
						margins: '0 3 3 0',
						border:false,
				        layout: 'fit',
						items: {
				            html: 'market'
				        }
					});
		
		marketPanel.superclass.initComponent.call(this);
		
		this.add(this.panel);
	}
	
});