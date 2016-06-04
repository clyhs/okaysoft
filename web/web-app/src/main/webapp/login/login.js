

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
                                html:'<img border="0" height="50" width="180" src="/okaysoft/security/jcaptcha.png?rand='+Math.random()+'"/>'
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
    	//window.location.href='/okaysoft/platform/main.html';
    	//var loginTip=Ext.Msg.wait("正在登录......", '请稍候');
        var j_captcha=Ext.getCmp('j_captcha').getValue();
        var j_username=Ext.getCmp('j_username').getValue();
        var j_password=Ext.getCmp('j_password').getValue();
        if(j_username.toString().trim()==""||j_password.toString().trim()==""||j_captcha.toString().trim()==""){
            Ext.getCmp('j_username').validate();
            Ext.getCmp('j_password').validate();
            Ext.getCmp('j_captcha').validate();
            //loginTip.hide();
            return false;
        }
        var url = 'j_spring_security_check';
        j_password=hex_md5(j_password+'{用户信息}');
        //alert(j_password);
        Ext.Ajax.request({
            url : url,
            params : {
                j_captcha  : j_captcha,
                j_username : j_username,
                j_password : j_password
            },
            method : 'POST',
            success:function(response, opts){
                
            	if(response.getResponseHeader('login_success') || response.responseText.length > 20) {
                    Ext.getCmp("loginWindow").hide();
                    //防止用户登录成功之后点击浏览器的后退按钮回到登录页面
                    //在浏览器的历史记录里面不记录登录页面
                    location.replace("/okaysoft/platform/main.jsp");
                    return;
                }  
            	refreshCode();
                Ext.getCmp('j_password').setValue("");
                Ext.getCmp('j_captcha').setValue("");
                Ext.getCmp('j_password').focus();
                //loginTip.hide();
                if(response.getResponseHeader('checkCodeError')) {
                	Ext.ux.Toast.msg('登陆失败：','验证码错误，请重新登录!');  
                    return;
                } 
                 
                
            },
            failure: function(response, opts) {
                location.replace("/okaysoft/login.jsp");
            },
            scope:this
        });
    },
    
    
    
    initComponent: function(){
    	this.panel = this.createPanel();
    	this.items = [this.panel]
    	LoginWindow.superclass.initComponent.call(this);
    }
});

