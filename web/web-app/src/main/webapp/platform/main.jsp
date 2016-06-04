<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>okaysoft platform</title>
<script type="text/javascript" src="../js/jsloader.js"></script>
<!-- 
<script type="text/javascript" src="/okaysoft/dwr/engine.js"></script>
<script type="text/javascript" src="/okaysoft/dwr/util.js"></script>
<script type="text/javascript" src="/okaysoft/dwr/interface/treeNodeManager.js"></script> -->

<script type="text/javascript" src="mainPage.js"></script>
</head>
<body id="apdplat_main">
	<div id="loading-mask"></div>
	<div id="loading">
		<div class="loading-indicator"></div>
	</div>
	<div id="north">
		<div id="app-header">
			<div id="header-left">
				<img id="logo" height="50" style="max-width:220px;" src="/okaysoft/images/logo.png"/>
			</div>
			<div id="header-main">
				<div id="topInfoPanel" style="float: left; padding-bottom: 4px">
					<div id="welcomeMsg">欢迎 admin</div>

				</div>
				<div class="clear"></div>
				<ul id="header-topnav">
					
				</ul>
			</div>
			<div id="header-right">
				<div id="currentTime">
					<span id="time"></span>
				</div>
				<div id="setting">
					<a href="#" onclick='triggerHeader();'>
						<img id="trigger-image" src="images/trigger-up.png" />
					</a>
				</div>
				<div id="search" style="width: 260px; float: right; padding-top: 8px;">&nbsp;</div>
			</div>
		</div>
	</div>

	<div id="west"></div>
	<div id="south"></div>
	<div id="main"></div>
</body>
</html>