<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<link href="reports.css" rel="stylesheet" type="text/css"/>
<link href="css/pepper-grinder/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.waitingpopup.js"></script>
<script type="text/javascript" src="js/JSON.js"></script>
<script type="text/javaScript" src="js/jsonrpcjs-0.1.7/jsonrpcjs-0.1.7.min.js"></script>
<script type="text/javascript" src="reports.js"></script>
<script type="text/javascript" src="js/charts.js"></script>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1', {packages: ['corechart']});
    </script>


<title>Relatórios Gestão de Identidades</title>
</head>

<body>
<div id='main'>
<div id="banner">
<img id="logoLeft" src="/itim/self/images/Identity_Manager_Title-2.gif" alt="IBM" title="IBM"/>
	<div id="toolbar"">
		<div id="bar">
			<div id="helpLogoff">
				<a id="logoffLink" href="javascript: logoff();" style="float: right;">Efetuar Logoff</a>
			</div>  
			<div id="userInfo">
				Bem-vindo, <span id='userName'></span>
			</div> 
		</div> 
		<div id="breadcrumbBar">
			<div id="breadcrumbs">
			</div> 
		</div> 
	</div>

</div>
	<div id='tabs'>
		<ul>
			<li><a href="#process_content">Relatórios de Pedido</a></li>
			<li><a href="#entity_content">Relatórios de Entidade</a></li>
			<li><a href="#activity_content">Relatórios de Atividades</a></li>
		</ul>
		<div class="ui content" id='process_content' >
		<table class='divtable'>
		<tr>
		<td>
		<div id=process_sub class='sub'><div id='process_acc' class='acc'/></div>
		</td>
		<td>
		<div id=process_report class='report'>
				<div class='sub_report'>
					<span>Escolha no menu ao lado o tipo de relatório deseja gerar.</span>
				</div>
		</div>
		</td>
		</tr>
		</table>
		</div>
		<div class="ui content" id='entity_content'>
		<table class='divtable'>
		<tr>
		<td>
		<div id=entity_sub class='sub'><div id='entity_acc' class='acc'/></div>
		</td>
		<td>
		<div id=entity_report class='report'>
				<div class='sub_report'>
					<span>Escolha no menu ao lado o tipo de relatório deseja gerar.</span>
				</div>
		</div>
		</td>
		</tr>
		</table></div>
		
		<div class="ui content" id='activity_content'>
		<table class='divtable'>
		<tr>
		<td>
		<div id=activity_sub class='sub'><div id='activity_acc' class='acc'/></div>
		</td>
		<td>
		<div id=activity_report class='report'>
				<div class='sub_report'>
					<span>Escolha no menu ao lado o tipo de relatório deseja gerar.</span>
				</div>
		</div>
		</td>
		</tr>
		</table></div>
	</div>
</div>
<div id=login>
<form>
<label>Usuário:</label><br/><input type="text"/><br/>
<label>Senha:</label><br/><input type="password"/><br/>
</form>
</div>
<div id=auth_error>
</div>
<div id=wait>
Gerando Relatório
</div>
<%
System.out.println("Teste Debug");
%>

</body>
</html>




