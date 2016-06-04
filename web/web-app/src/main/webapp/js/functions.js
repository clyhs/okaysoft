var contextPath ="/okaysoft";

function refreshCode(){
	var loginCode = Ext.getCmp('codePicture');
	loginCode.body.update('<img border="0" height="50" width="180" src="'+contextPath + '/security/jcaptcha.png?rand='+Math.random()+'"/>');
    fixPng();
}

function changeTime(){
    document.getElementById("time").innerHTML = new Date().format('Y年n月j日  H:i:s');
}

function refreshTheme(){
    var storeTheme=Ext.util.Cookies.get('theme');
    if(storeTheme==null || storeTheme==''){
            storeTheme='ext-all';
    }
    Ext.util.CSS.swapStyleSheet("theme", "/okaysoft"+"/extjs/css/"+storeTheme+".css");  
}

//用来保存在tab页面中打开的窗口
var openingWindows=new Array();
function refreshAll(){
	
    for(var i=0;i<openingWindows.length;i++){
        if(openingWindows[i]!=undefined && openingWindows[i].closed==false){
            openingWindows[i].refreshTheme();
        }
    }
    refreshTheme();
}

function process(str){
    str=str.replace("A", "-a");
    str=str.replace("B", "-b");
    str=str.replace("C", "-c");
    str=str.replace("D", "-d");
    str=str.replace("E", "-e");
    str=str.replace("F", "-f");
    str=str.replace("G", "-g");
    str=str.replace("H", "-h");
    str=str.replace("I", "-i");
    str=str.replace("J", "-j");
    str=str.replace("K", "-k");
    str=str.replace("L", "-l");
    str=str.replace("M", "-m");
    str=str.replace("N", "-n");
    str=str.replace("O", "-o");
    str=str.replace("P", "-p");
    str=str.replace("Q", "-q");
    str=str.replace("R", "-r");
    str=str.replace("S", "-s");
    str=str.replace("T", "-t");
    str=str.replace("U", "-u");
    str=str.replace("V", "-v");
    str=str.replace("W", "-w");
    str=str.replace("X", "-x");
    str=str.replace("Y", "-y");
    str=str.replace("Z", "-z");
    return str;
}