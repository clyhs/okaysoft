Ext.namespace("main");

structurePanel = Ext.extend(Ext.Panel, {
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
				            html: 'structure'
				        }
					});
		
		structurePanel.superclass.initComponent.call(this);
		
		this.add(this.panel);
	}
	
});