

var model;
var loginTitle = "登录系统";
var requestCode;

Ext.QuickTips.init();
Ext.BLANK_IMAGE_URL = '/okaysoft/extjs/images/default/s.gif';
Ext.form.Field.prototype.msgTarget='side';

LoginWindow = Ext.extend(Ext.Window,{
	id:"loginWindow",
    iconCls:"login-icon",
    bodyStyle:"background-color: white",
    border:true,
    closable:false,
    resizable:false,
    buttonAlign:"center",
	title : loginTitle,
    width : 480,		
    height:270,
    collapsible : true,
    createForm:function(){
    	var form = new Ext.form.FormPanel({
    		bodyStyle : 'padding-top:6px',
            defaultType : 'textfield',
            labelAlign : 'right',
            columnWidth:0.75,
            border:false,
            layout:'form',
            border:false,
            labelWidth : 55,
            buttonAlign: 'center',
            defaults:{
            	allowBlank : false,
                anchor:"90%"
            },
            items:[{
                cls : 'j_username',
                style : 'padding-left:18px',
                name : 'j_username',
                id : 'j_username',
                fieldLabel : '帐 号',
                blankText : '帐号不能为空'
            },{
                cls : 'j_password',
                style : 'padding-left:18px',
                name : 'j_password',
                id : 'j_password',
                fieldLabel : '密 码',
                blankText : '密码不能为空',
                inputType : 'password'
            }, {
                cls : 'j_rand',
                style : 'padding-left:18px',
                name : 'j_captcha',
                id:'j_captcha',
                fieldLabel : '验证码',
                blankText : '验证码不能为空'
            },{
                xtype:'panel',
                layout:'table',
                hideLabel:true,
                border:false,
                layoutConfig:{columns:3},
                items:[
                        {
                                width:55,
                                xtype:'panel',
                                border:false,
                                text:'      '
                        },{
                                width:180,
                                xtype:'panel',
                                border:false,
                                id:"codePicture",
                                html:''
                        },{
                                width:55,
                                xtype:'panel',
                                border:false,
                                bodyStyle:'font-size:12px;padding-left:12px',
                                html:'<a href="javascript:refreshCode()">看不清</a>'
                        }]
                 }],
                 keys:[{
                     key : Ext.EventObject.ENTER,
                     fn : function() {
                        this.login;
                     },
                     scope : this
                 }]
    	});
    	return form;
    },
    createPanel:function(){
    	
    	var panel = [{
            xtype:"panel",
            border:false,
            bodyStyle:"padding-left:20px",
            html:'',
            height:60
        },{
            xtype:"panel",
            border:false,
            layout:"column",
            buttonAlign:"center",
            items:[
                this.createForm(),
                {
                    xtype:"panel",
                    border:false,
                    columnWidth:0.25,
                    html:''
                }
            ],
            buttons: [{
                text: '登录',
                iconCls:'save',
                scope: this,
                handler: function() {
                    this.login();
                }
            },{
                text: '重置',
                iconCls:'reset',
                scope: this,
                handler: function() {
                    
                }
            }],
            keys:[{
                 key : Ext.EventObject.ENTER,
                 fn : function() {
                    this.login();
                 },
                 scope : this
             }]
        }]
    	
    	
    	return panel;
    },
    login:function(){
    	window.location.href='/okaysoft/platform/main.html';
    },
    
    
    
    initComponent: function(){
    	this.panel = this.createPanel();
    	this.items = [this.panel]
    	LoginWindow.superclass.initComponent.call(this);
    }
});

Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.BLANK_IMAGE_URL = '/okaysoft/extjs/images/default/s.gif';
	Ext.form.Field.prototype.msgTarget='side';
	var win = new LoginWindow();
	win.show();
    setTimeout(function(){
        Ext.get('loading-mask').fadeOut({
            remove: true
        });
    }, 10);
    
    fixPng();
    
});