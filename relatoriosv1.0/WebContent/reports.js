var autorizados = [];
function preparePage(){
	$.getJSON('reports.json', function(data) {
		$('#main').hcenter();
		$('#tabs').tabs();
		var acc;
		$.each(data.process,function(i,group){
				acc = $('#process_content').find('.acc').append('<h3><a href="#">'+group.name+'</a></h3><div/>');
				setLinkGroups(group.members,'process');
			});
		acc.accordion();
		$.each(data.entity,function(i,group){
			$('#entity_content').find('.acc').append('<h3><a href="#">'+group.name+'</a></h3><div/>');
			setLinkGroups(group.members,'entity');
		});
		$('#entity_content').find('.acc').accordion({ autoHeight: false });
		$.each(data.activity,function(i,group){
			$('#activity_content').find('.acc').append('<h3><a href="#">'+group.name+'</a></h3><div class:"acc_menu"/>');
			
			setLinkGroups(group.members,'activity');
		});
		$('#activity_content').find('.acc').accordion({ autoHeight: false });
	});
	$.getJSON("autorizados.json",function(data){
		autorizados = data.autorizados;
	});
}
function setLinkGroups(groups,type,pai){
	var div = $('#'+type+'_content').find('.acc:last').find('div:last'); 
	var report_div = $('#'+type+'_content').find('.report:last').find('div:last');
	$.each(groups,function(i,group){
		ol = div.append('<ol/>');
		subdiv = ol.append('<li/>').append('<div/>').children().last().hide().css("padding-left","5%");
		//alert("group.members: "+group.members);
		ol.find('li:last').text(group.name);
		if(group.members!=undefined){
			ol.find('li:last').click(function(){
				$(this).next('div').slideToggle();
			});
			setLinkGroups(group.members,type,group.name);
		}else{
			ol.find('li:last').click(function(){
				$('li').removeClass('ui-selected',100);
				$(this).addClass( "ui-selected", 100 );
				var nome = group.name;
				if(pai)nome=pai+' - '+nome;
				var div = report_div.html('<div class="header_report"><b>'+nome+'</span></b></div>');
				div.append('<div class="report_button"><a href="#">Gerar Relatório</a></div>').find('a:last').button().fitRight().click(
						function(){
							openReports(nome,group,type);
						});
			});
		}
	 });

}
function syncCallWS(sql,operationLevel,parentLevel){
	var jsonValue = {"method":"getProcessBySql",
			"params":
			{
				"sql":sql
			}
		};
	if(operationLevel)
		jsonValue.params.operationLevel = operationLevel;
	if(parentLevel)
		jsonValue.params.parentLevel = parentLevel;
	$('#wait').dialog('open').dialog('moveToTop');
	var retorno = [];
	
			$.ajax({
				url:'/api/process',
				dataType:'json',
				type:'POST',
				data:encodeURIComponent(JSON.stringify(jsonValue)),
				async: false,
				success:function(response){
					if(response.error){
						$('#auth_error').html(response.error.message).dialog({
							title: 'Erro'
						});
					}else{
						retorno =  response;
					}
				}
			});
	return retorno;
}

function syncCallWSV2(params){
	var jsonValue = {"method":"getProcessBySql",
			"params":
				params
		};
	$('#wait').dialog('open').dialog('moveToTop');
	var retorno = [];
			$.ajax({
				url:'/api/process.v2',
				dataType:'json',
				type:'POST',
				data:encodeURIComponent(JSON.stringify(jsonValue)),
				async: false,
				success:function(response){
					if(response.error){
						$('#auth_error').html(response.error.message).dialog({
							title: 'Erro'
						});
					}else{
						retorno =  response;
					}
				}
			});
	return retorno;
}
function getProcessById(process_id){
	params = {"sql":"select * from process where id="+process_id
				,"getActivities":true
				,"getActivitiesHistory":true
			};
	return syncCallWSV2(params).result[0];
}
function getProcessesByParentId(parent_id){
	params = {"sql":"select * from process where parent_id="+parent_id
				,"getActivities":true
				,"getActivityHistory":true
			};
	return syncCallWSV2(params).result;
}
function syncCallEntityWS(jsonValue){
	var retorno = [];
	$.ajax({
		url:'/api/entity',
		dataType:'json',
		type:'POST',
		data:encodeURIComponent(JSON.stringify(jsonValue)),
		async: false,
		success:function(response){
			if(response.error){
				$('#auth_error').html(response.error.message).dialog({
					title: 'Erro'
				});
			}else{
				retorno =  response;
			}
		}
	});
return retorno;
}


function callWebService(jsonValue,tratamento){
	
	$('#main').append('<div>Gerando Relatorio..</div>').children().last().dialog({
		title: 'Aguarde...'
		,modal: true
		,open: function(){
			$.ajax({
				url:'/api/process',
				dataType:'json',
				type:'POST',
				data:jsonValue,
				async: false,
				success:function(response){
					if(response.error){
						$('#auth_error').html(response.error.message).dialog({
							title: 'Erro'
						});
					}else{
						tratamento(response);
					}
				}
			});
			$(this).dialog('close');
		}
	});

}
var csv_max_request = 100;
function writeCSV(file,array,complete,append){
	//if(append==null)append = false;
	//var array_f = [];
	//Divide recursivamente em varios pedidos a cada 100 elementos
	/*if(array.length>csv_max_request){
		array_f = array.slice(csv_max_request,array.length-1); 
		array = array.slice(0,csv_max_request);
		this.complete = function(){
			writeCSV(file,array_f,complete,true);
		};
	}*/
	query = {'file':file+".csv",'array':array,'append':append};
	$.ajax({
		type: 'POST',
		url:'writeCSV.jsp',
		dataType:'json',
		data:encodeURI(JSON.stringify(query)),
		complete: complete
	});
}

//Regras de Ciclo de Vida
function relatorio_RecertificacaoExito(from,to,func){
	function tratamento(response){
		result = response.result;
		relatorio = new Array();
		//if(relatorio.length==0)return;
		relatorio.push(["Processo Root","Id do Processo","Solicitado Para","Data de Envio","Data de Conclusão"
		                ,"Revalidacao - Roteado Para"
		                ,"Revalidacao - Concluído Por"
		                ]);
		for(var i=0;i<result.length;i++){
			var process = result[i];
			var aprovacao = process.activities.AprovacaoRecertTerceiro;
			if(aprovacao==null)aprovacao = process.activities.AprovacaoRecertLC;
			relatorio.push([process.parentId,process.id,process.requesteeName,process.started,process.completed
			                ,aprovacao.routed
			                ,aprovacao.completedBy
			                ]);
		}
		func(relatorio);
	}
	request = {
			method:"getProcessBySql",
			params:{
				"sql":"select * from itimuser.process where name='recertificaTerceiros' and RESULT_SUMMARY='SS' and started>='"+from+"' and started<='"+to+"'"
			}
	};
	callWebService(JSON.stringify(request),tratamento);

}
function relatorio_RecertificacaoFalha(from,to,func){
	function tratamento(response){
		if(response!=null){
						result = response.result;
			relatorio = new Array();
			relatorio.push(["Processo Pai","Id do Processo","Data de Início","Data de Conclusão","Solicitado Para"
			                ,"SuspendePessoa - Resultado"
			                ,"Aprovacao - Resultado"
			                ,"Aprovacao - Roteado Para"
			                ,"Aprovacao - Concluido Por"
							,"Bloqueia Usuário - Resultado"
							,"Bloqueia Usuário - Data de Início" 
							,"Bloqueia Usuário - Data de Conclusão"
							,"Bloqueia Usuario - Roteado Para" 
							,"Bloqueia Usuario - Concluído por"
			                ]);
			for(var i=0;i<result.length;i++){
				process = result[i];
				//alert('Process: '+Object.toJSON(process));
				var aprovacao = process.activities.AprovacaoRecertTerceiro;
				if(!aprovacao) aprovacao = process.activities.AprovacaoRecertLC;
				if(!aprovacao) aprovacao = new Object();
				var notifica = process.activities.NotificaSuspensao;
				//alert('notifica:'+ Object.toJSON(notifica));
				if(notifica==null)process.activities.NotificaSuspensaoLC;
				//alert('notifica:'+ Object.toJSON(notifica));
				if(notifica==null)notifica = new Object();
				suspende = process.activities.SuspendePessoa;
				if(suspende==null)suspende = new Object();
				relatorio.push([process.parentId,process.id,process.started,process.completed,process.requesteeName
				                ,suspende.resultSummary
				                ,aprovacao.resultSummary
				                ,aprovacao.routed
				                ,aprovacao.completedBy
				                ,notifica.completedBy
				                ,notifica.started
				                ,notifica.completed
				                ,notifica.routed
				                ,notifica.completedBy
				                ]);
			}
		}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where name='recertificaTerceiros' and RESULT_SUMMARY<>'SS'  and started>='"+from+"' and started<='"+to+"'\"}}",tratamento);

}
function relatorio_ProrrogacaoExito(from,to,func){
	function tratamento(response){
		result = response.result;
		relatorio = new Array();
		relatorio.push(["Processo Pai","Id do Processo","Solicitado Para"		     
						,"Requisita Nova Data - Resultado"
						,"Requisita Nova Data - Data de Início" 
						,"Requisita Nova Data - Data de Conclusão"
						,"Requisita Nova Data - Roteado Para" 
						,"Requisita Nova Data - Concluído por"
						,"Nova Data"
						,"Alteracão da Data - Resultado"
		                ]);
		for(var i=0;i<result.length;i++){
			var process = result[i];
			var requisitaNovaData = process.activities.requisitaNovaData;
			if(requisitaNovaData==null)process.activities.requisitaNovaDataCorreta;
			if(requisitaNovaData==null)requisitaNovaData = new Object();
			modify = process.activities.ldapModifyPerson;
			if(modify==null)modify = new Object();
			relatorio.push([process.parentId,process.id,process.requesteeName
			                ,requisitaNovaData.resultSummary
			                ,requisitaNovaData.started
			                ,requisitaNovaData.completed
			                ,requisitaNovaData.routed
			                ,requisitaNovaData.completedBy
			                ,zuluToDate(requisitaNovaData.newData.gpaterminoserv)
			                ,modify.resultSummary
			                ]);
		};
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where name='prorrogaContrato' and RESULT_SUMMARY='SS'  and started>='"+from+"' and started<='"+to+"'\"}}",tratamento);

}
function relatorio_ProrrogacaoFalha(from,to,func){
	function tratamento(response){
		if(response!=null){
			result = response.result;
			relatorio = new Array();
			relatorio.push(["Processo Pai","Id do Processo","Solicitado Para"
			                //,"SuspendePessoa - Resultado"
							,"Requisita Nova Data - Resultado"
							,"Requisita Nova Data - Data de Início" 
							,"Requisita Nova Data - Data de Conclusão"
							,"Requisita Nova Data - Roteado Para" 
							,"Requisita Nova Data - Concluído por"
							,"Requisita Nova Data - Prazo"
			                ]);
			for(var i=0;i<result.length;i++){
				var process= result[i];
				//alert('Process: '+Object.toJSON(process));
				var requisitaNovaData = process.activities.requisitaNovaData;
				//alert('notifica:'+ Object.toJSON(notifica));
				if(requisitaNovaData==null)process.activities.requisitaNovaDataCorreta;
				//alert('notifica:'+ Object.toJSON(notifica));
				if(requisitaNovaData==null)requisitaNovaData = new Object();
				suspende = process.activities.ldapModifyPerson;
				
				if(suspende==null)suspende = new Object();
				relatorio.push([process.parentId,process.id,process.requesteeName
				                //,suspende.resultSummary
				                ,requisitaNovaData.resultSummary
				                ,requisitaNovaData.started
				                ,requisitaNovaData.completed
				                ,requisitaNovaData.routed
				                ,requisitaNovaData.completedBy
				                ,requisitaNovaData.dueDate
				                ]);
			}
		}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where name='prorrogaContrato' and RESULT_SUMMARY<>'SS'  and started>='"+from+"' and started<='"+to+"'\"}}",tratamento);

}
//Tickets
function relatorio_Rede(from,to,func){
	relatorio_tickets(from,to,"Rede",func);
}
function relatorio_RestauraSenha(from,to,func){
	relatorio_tickets(from,to,"Restaura Senha",func);
}
function relatorio_AcessoParaSAP(from,to,func){
	relatorio_tickets(from,to,"Acesso para SAP",func);
}

function relatorio_Acesso_para_Emulador(from,to,func){
	relatorio_tickets(from,to,"Acessos para Mainframe",func);
}
function relatorio_Pacotes_e_Ferramentas(from,to,func){
	relatorio_tickets(from,to,"Acessos para Pacotes",func);

}
function relatorio_Sistemas_Coorporativos(from,to,func){
	relatorio_tickets(from,to,"Acessos para Sistemas Corporativos",func);
}
function relatorio_TI(from,to,func){
	relatorio_tickets(from,to,"TI",func);
}

function getAllCustomData(processes){
	if(processes){
		var customDataArr = [];
		for(var i=0;i<processes.length;i++){
			var process = processes[i];
			var customDataAtual = process.customData;
			if(customDataAtual){
				$.each(customDataAtual,function(i,customData){
					if(customDataArr.indexOf(customData.label)<0){
						customDataArr.push(customData.label);
					}
				});
			}
		}
		return customDataArr;
	}
}

function relatorio_tickets(from,to,serviceName,func){
	function tratamento(response){
		var result = response.result;
		var relatorio = new Array();
		if(result.length>0){
			var customData = getAllCustomData(result);
			relatorio.push(["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"].concat(customData));
			
			for(var i=0;i<result.length;i++){
				var process = result[i];
				try{
				linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
				$.each(customData,function(i,label){
					var value;
					$.each(process.customData,function(k,customData){
						if(customData.label===label)
							value = customData.value;
					});
					linha.push(value);
				});
				relatorio.push(linha);
				}catch(e){}
			}
		}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where subject_service='"+serviceName+"' and started>='"+from+"' and started<='"+to+"'  and parent_id=0\"}}",tratamento);
}
//Sistemas Integrados
function getAttributes(result,getJson){
	var atributos = new Object();
	$.each(result,function(i,process){
		for(k in getJson(process)){
			atributos[k] = "true";
		}
	});
	return getKeys(atributos);
}
function relatorio_integrados_add(serviceName,from,to,func){
	function tratamento(response){
		result = response.result;
		relatorio = new Array();
		if(result.length>0){
			var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
			var attributes = getAttributes(result,function (process){return process.account;});
			cabecalho = cabecalho.concat(attributes);
			relatorio.push(cabecalho);
			$.each(result,function(i,process){
				new_attributes = getKeys(process.account).without(attributes);
				linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
				$.each(attributes,function(i,attribute){linha.push(process.account[attribute]);});
				relatorio.push(linha);
			});
			}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where subject_service='"+serviceName+"' and name='accountAddProcessName' and started>='"+from+"' and started<='"+to+"' and parent_id=0\"}}",tratamento);	
}
function relatorio_integrados_modify(serviceName,from,to,func){
	function tratamento(response){
		result = response.result;
		relatorio = new Array();
		if(result.length>0){
			var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
			attributes = getAttributes(result,function (process){return process.changes;});
			$.each(attributes,function(i,attribute){
				cabecalho.push(attribute);
			});
			relatorio.push(cabecalho);
			$.each(result,function(i,process){
				linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
				$.each(attributes,function(i,attribute){linha.push(process.changes[attribute]);});
				relatorio.push(linha);
			});
		}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where subject_service='"+serviceName+"' and name='accountModifyProcessName' and started>='"+from+"' and started<='"+to+"' and parent_id=0\"}}",tratamento);	
}
function relatorio_integrados_delete(serviceName,from,to,func){
	function tratamento(response){
		//var response_json = eval("("+response.responseText.trim()+")");
		result = response.result;
		relatorio = new Array();
		if(result.length!=0){
			var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
			attributes = getAttributes(result,function getJson(process){return process.entity;});
			for(var i=0;i<attributes.length;i++)
				cabecalho.push(attributes[i]);
			relatorio.push(cabecalho);
			$.each(result,function(i,process){
				linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
				$.each(attributes,function(i,attribute){linha.push(process.entity[attribute]);});
				relatorio.push(linha);
			});
		}
		func(relatorio);
	}
	callWebService("{\"method\":\"getProcessBySql\",\"params\":{\"sql\":\"select * from itimuser.process where subject_service='"+serviceName+"' and name='accountDeleteProcessName' and started>='"+from+"' and started<='"+to+"' and parent_id=0\"}}",tratamento);	
}
//Por Serviço
//Oracle RDF
function relatorio_OracleRDF_add(from,to,func){
	relatorio_integrados_add('Oracle RDF',from,to,func);
}
function relatorio_OracleRDF_add(result){
		relatorio = new Array();
		if(result.length>0){
			var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
			var attributes = getAttributes(result,function (process){return process.account;});
			cabecalho = cabecalho.concat($.grep(attributes,function(att){
				return att!='errpasnivelseguranca';
			}).concat('Operacional','Mercadologica'));
			relatorio.push(cabecalho);
			$.each(result,function(i,process){
				new_attributes = getKeys(process.account).without(attributes);
				linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
				var operacional,mercadologica;
				$.each(attributes,function(i,attribute){
					if(attribute=='errpasnivelseguranca'){
						var errpasnivelseguranca = process.account[attribute].replace(/&#61;/gi,'=')
						//alert("errpasnivelseguranca: "+errpasnivelseguranca);
						var params = {method:'getUnitList',params:{'nivelSeguranca':errpasnivelseguranca}};
						
						$.ajax({
							url:'/OracleRetailOperationsWS',
							async: false,
							dataType:'json',
							type:'POST',
							data: encodeURIComponent(JSON.stringify(params)),
							success:function(response){
								//alert('Response: '+JSON.stringify(response));
								if(!response.error){
									mercadologica = response.result.Mercadologica;
									operacional = response.result.Operacional;
								}
							}
						});
					}else{
						linha.push(process.account[attribute]);
					}
					});
				linha.push(operacional);
				linha.push(mercadologica);
				relatorio.push(linha);
			});
			}
		return relatorio;
}
function relatorio_OracleRDF_modify(from,to,func){
	relatorio = new Array();
	if(result.length>0){
		var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
		var attributes = getAttributes(result,function (process){return process.changes;});
		cabecalho = cabecalho.concat($.grep(attributes,function(att){
			return att!='errpasnivelseguranca';
		}).concat('Operacional','Mercadologica'));
		relatorio.push(cabecalho);
		$.each(result,function(i,process){
			new_attributes = getKeys(process.account).without(attributes);
			linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
			var operacional,mercadologica;
			$.each(attributes,function(i,attribute){
				if(attribute=='errpasnivelseguranca'){
					var errpasnivelseguranca = process.account[attribute].replace(/&#61;/gi,'=')
					//alert("errpasnivelseguranca: "+errpasnivelseguranca);
					var params = {method:'getUnitList',params:{'nivelSeguranca':errpasnivelseguranca}};
					
					$.ajax({
						url:'/OracleRetailOperationsWS',
						async: false,
						dataType:'json',
						type:'POST',
						data: encodeURIComponent(JSON.stringify(params)),
						success:function(response){
							//alert('Response: '+JSON.stringify(response));
							if(!response.error){
								mercadologica = response.result.Mercadologica;
								operacional = response.result.Operacional;
							}
						}
					});
				}else{
					linha.push(process.account[attribute]);
				}
				});
			linha.push(operacional);
			linha.push(mercadologica);
			relatorio.push(linha);
		});
		}
	return relatorio;
}
function relatorio_OracleRDF_delete(from,to,func){
	relatorio = new Array();
	if(result.length>0){
		var cabecalho = ["Nome do Serviço","Id do Processo","Data de Inicio","Data de Conclusao","Solicitante","Solicitado Para"];
		var attributes = getAttributes(result,function (process){return process.entity;});
		cabecalho = cabecalho.concat($.grep(attributes,function(att){
			return att!='errpasnivelseguranca';
		}).concat('Operacional','Mercadologica'));
		relatorio.push(cabecalho);
		$.each(result,function(i,process){
			new_attributes = getKeys(process.account).without(attributes);
			linha = [process.serviceName,process.id,process.started,process.completed,process.requestorName,process.requesteeName];
			var operacional,mercadologica;
			$.each(attributes,function(i,attribute){
				if(attribute=='errpasnivelseguranca'){
					var errpasnivelseguranca = process.entity[attribute].replace(/&#61;/gi,'=')
					//alert("errpasnivelseguranca: "+errpasnivelseguranca);
					var params = {method:'getUnitList',params:{'nivelSeguranca':errpasnivelseguranca}};
					
					$.ajax({
						url:'/OracleRetailOperationsWS',
						async: false,
						dataType:'json',
						type:'POST',
						data: encodeURIComponent(JSON.stringify(params)),
						success:function(response){
							//alert('Response: '+JSON.stringify(response));
							if(!response.error){
								mercadologica = response.result.Mercadologica;
								operacional = response.result.Operacional;
							}
						}
					});
				}else{
					linha.push(process.account[attribute]);
				}
				});
			linha.push(operacional);
			linha.push(mercadologica);
			relatorio.push(linha);
		});
		}
	return relatorio;
}
//SAP
function relatorio_SAP_add(from,to,func){
	relatorio_integrados_add('SistemaSAP Mandante 100',from,to,func);
}
function relatorio_SAP_modify(from,to,func){
	relatorio_integrados_modify('SistemaSAP Mandante 100',from,to,func);
}
function relatorio_SAP_delete(from,to,func){
	relatorio_integrados_delete('SistemaSAP Mandante 100',from,to,func);
}
//REDE 2008
function relatorio_Rede2008_add(from,to,func){
	relatorio_integrados_add('Rede 2008',from,to,func);
}
function relatorio_Rede2008_modify(from,to,func){
	relatorio_integrados_modify('Rede 2008',from,to,func);
}
function relatorio_Rede2008_delete(from,to,func){
	relatorio_integrados_delete('Rede 2008',from,to,func);
}

//Por Operação
function relatorio_operation_restoreAccount(from,to,func){
	relatorio_operation(from,to,func,"accountRestoreProcessName","RESTOREACCOUNT");
}
function relatorio_operation_suspendAccount(from,to,func){
	relatorio_operation(from,to,func,"accountSuspendProcessName","SUSPENDACCOUNT");
}
function relatorio_operation_deleteAccount(from,to,func){
	relatorio_operation(from,to,func,"accountDeleteProcessName","DELETEACCOUNT");
}
function relatorio_operation_createAccount(from,to,func){
	relatorio_operation(from,to,func,"accountAddProcessName","CREATEACCOUNT");
}
function relatorio_operation_modifyAccount(from,to,func){
	relatorio_operation(from,to,func,"accountModifyProcessName","MODIFYACCOUNT");
}
function relatorio_operation(from,to,func,processName,activityName){
	function tratamento(response){
		result = response.result;
		if(result.length>=0){
			relatorio = [];
			relatorio.push(["Nome do Serviço","Processo Root","Nome do Processo Root","Id do Processo","Solicitante","Solicitado Para","Iniciado","Concluido","Status","Resultado"
			                 //,activityName+" - Resultado"
							 ]);
			$.each(result,function(i,process){
				//var activity = process.activities[activityName];
				//if(activity==null)activity = new Object();
				var root = getRootProcess(process);
				relatorio.push([process.serviceName,root.id,root.name,process.id,process.requestorName,process.requesteeName,process.started,process.completed,process.state,process.resultSummary
						       //,activity.resultSummary
						       ]);
						});
		}
		func(relatorio);
	}
	var request = {
			method:"getProcessBySql",
			params:{
				"sql":"select * from itimuser.process where name='"+processName+"' and started>='"+from+"' and started<='"+to+"'"
				,"parentLevel":1
			}
	};
	callWebService(JSON.stringify(request),tratamento);	
	
}

//Atividades Manuais

function relatorio_atividadesManuais(from,to,func){
	function tratamento(response){
		result = response.result;
		if(result.length>=0){
			relatorio = [];
			relatorio.push(["Nome do Serviço","Processo Root","Nome do Processo Root","Id do Processo",
			                "Nome da Atividade",
			                "Data de Inicio",
			                "Data de Conclusao",
			                "Solicitante","Solicitado Para",
			                "Roteado para","Concluido Por",
			                "Resultado"
							 ]);
			$.each(result,function(i,process){
				var root = getRootProcess(process);
				var activities = getManualActivities(process);
				$.each(activities,function(i,activity){
					/*relatorio.push([process.serviceName,root.id,root.name,process.id,process.requestorName,process.requesteeName,process.state,process.resultSummar
								       ]);*/
					relatorio.push([process.serviceName,
					                root.id,
					                root.name,
					                process.id,
					                activity.name,
					                activity.started,
					                activity.completed,
					                process.requestorName,
					                process.requesteeName,
					                activity.routed,
					                activity.completedBy,
					                activity.resultSummary
					                ]);
								});
				});
				
		}
		func(relatorio);
	}
	request = {
			method:"getProcessBySql",
			params:{
				"sql":"select * from itimuser.process where started>='"+from+"' and started<='"+to+"' and parent_id=0"
				,"operationLevel":"FULL"
			}
	};
	callWebService(JSON.stringify(request),tratamento);	
}

function getActivities(process){
	var activities = new Array();
	var p_activities = process.activities;
	if(!p_activities)return activities;
	for(var i=0;i<p_activities.length;i++){
		var activity = p_activities[i];
		if(activity.type=='O'){
			$.each(activity.children,function(i,process){
				activities = activities.concat(getActivities(process));
			});	
		}else{
			if(activity!=null)
				activities.push(activity);
		}
	}
	//alert("Activities: "+JSON.stringify(activities));
	return activities;
}
function getManualActivities(process){
	var activities = getActivities(process);
	$.each(activities,function(i,activity){
		//alert("Activity: "+JSON.stringify(activity));
		if(!activity)
			activities.pop(activity);
		else if(activity.activityType!='M')
			activities.pop(activity);
	});
	return activities;
}


function getChildren(process,level){
	var children = [];
	if(level==0) return children;
	children = getProcessesByParentId(process.id);
	//var subchildren = [];
	$.each(children,function(i,child){
		children = children.concat((getChildren(child,(level)?level-1:null)));
	});
	//children = children.concat(subchildren);
	return children;
}

//Entidades
//Auxiliares
function writeTable(relatorio){
	$('response').update("");
	relatorio.each(function(linha){
		$('response').insert(Object.toJSON(linha)+"<br/>");
	});
	
}

function getCommonDialog(){
	var common_dialog = {
			modal: true
			,minWidth: 400
			,show: 'puff'
			,hide: 'explode'
	};
	return common_dialog;
}

//Abre a janela de criação de relatórios
function openReports(nome,group,type){
	if(group.type) type = group.type;
	concluido = function(relatorio,config){
		if(!relatorio)return;
		var table = $('#main').append('<div/>').find('div:last');
		putReportAsTable(table,relatorio);
		table.dialog({
			title:("Relatorio - "+nome),
			width:$(window).width(),
			height:$(window).height(),
			buttons:[{
				text:"Baixar como CSV",
				click:function(){
					var csv_link_div = $('#main').append('<div>Gerando CSV...</div>').find('div:last').dialog({
						title: "Baixar CSV"
					});
					writeCSV(nome,relatorio,function(){
						csv_link_div.html("<a href='downloadFile.jsp?file="+nome+".csv'>Clique Aqui para baixar o CSV</a>");
					});
					}
				},
				/*{
					text:"Visualizar em Gráfico",
					click:function(){
						var chart_div = $('#main').append('<div id="teste"/>').find('div:last');
						drawVisualization('teste',relatorio,config);
						chart_div.dialog({
							title:("Gráfico - "+nome),
							width:$(window).width(),
							height:$(window).height()
						});
					}
				}*/
				]
		});
	};
	show_report = function(config){
		var result = config.source(config);
		var relatorio = config.func(result,config);
		var table = $('#main').append('<div/>').find('div:last');
		putReportAsTable(table,relatorio);
		table.dialog({
			title:("Relatorio - "+nome),
			width:$(window).width(),
			height:$(window).height(),
			buttons:
				[{
					text:"Atualizar",
					click:function(){
						var result = config.source(config);
						var relatorio = config.func(result,config);
						putReportAsTable(table,relatorio);
					}
					}
				,
				{
				text:"Baixar como CSV",
				click:function(){
					var csv_link_div = $('#main').append('<div>Gerando CSV...</div>').find('div:last').dialog({
						title: "Baixar CSV"
					});
					writeCSV(nome,relatorio,function(){
						csv_link_div.html("<a href='downloadFile.jsp?file="+nome+".csv'>Clique Aqui para baixar o CSV</a>");
					});
					}
				}]
		});
	};
	$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
	
	if(type=='process'){
		var process_dialog = getCommonDialog();
		process_dialog.title = "Criar Relatório - "+nome;
		process_dialog.buttons=[{text:"Criar",click:function(){
			$(this).dialog({
				close:function(){
					func = eval(group.func);
					if(func.toString().indexOf('Oracle')>-1){
						var sql = "select * from itimuser.process where started>='"
							+formatedDateToSql($(this).find('input:first').val())
							+"' and started<='"
							+formatedDateToSql($('input:last').val())
							+"'";
						$.each(group.filters,function(i, filter){
							sql += " and " + filter+ " ";
						});
						
						var response = syncCallWS(sql);
						result = response.result;
						var report = func(result);
						concluido(report);
					}else{
						func(formatedDateToSql($(this).find('input:first').val()),formatedDateToSql($('input:last').val()),concluido);
					}
				}
			});
			$(this).dialog('close');
			
			}
		}];
		var dialog = $('#main').append("<div style='text-align: center;'></div>").find('div:last').dialog(process_dialog);
		dialog.append("<p>Data Inicial: <input/></p>")
		.append("<p>Data Final: <input/></p>")
	.find('p > input').datepicker();
	}else if(type=='entity'){
		var entity_dialog = getCommonDialog();
		entity_dialog.title = "Criar Relatório - "+nome;
		entity_dialog.buttons = [{text:"Criar",click:function(){
			$(this).dialog({
				close:function(){
					var func;
					if(group.file)
						$.getScript('reports/'+group.file)
						.done(function(script,textStatus){
						//alert(script);
							eval(script);
							func = eval(group.func);
							var response = syncCallEntityWS(group.filter);
							result = response.result;
							var report = func(result);
							concluido(report);
						});
				}
			});
			$(this).dialog('close');
			
			}
		}];;
		var dialog = $('#main').append("<div style='text-align: center;'></div>").find('div:last').dialog(entity_dialog);
	}else if(type=='activity'){
		var activity_dialog = getCommonDialog();
		activity_dialog.title = "Criar Relatório - "+nome;
		activity_dialog.width=600;
		activity_dialog.buttons = [{text:"Criar",click:function(){
			var date_criteria = $('input:last').prop('checked')?'completed':'started';
			$(this).dialog({
				
				close:function(){
						var func;
						if(group.file)
							$.getScript('reports/'+group.file)
							.done(function(script,textStatus){
							//alert(script);
								eval(script);
								func = eval(group.func);
								//var response = syncCallWSV2(params);
								//var result2 = response.result;
								var service = $('p:last').find('input:last').val();
								var end = getDateByFormatedDateAndTime(
										$('p:last').prev().find('input:first').val(),
										$('p:last').prev().find('input:last').val()
										);
								var start = getDateByFormatedDateAndTime(
										$('p:last').prev().prev().find('input:first').val(),
										$('p:last').prev().prev().find('input:last').val()
										);
								var config = {
										start:start,
										end:end,
										//service:((service=='')?null:service)
										service:service
										,date_criteria : date_criteria
										,func : func
										,source : eval(group.source)
								};
								var result = [];
								if(group.loadType=='buffered'){
									//result = getBufferedActivitiesByDateAndService(config);
									show_report(config);
								}else{
									result = getProcessesByDateAndService(config);
									var report = func(result,config);
									concluido(report);
								}
								//result = response.result;
								
							});
				}
			});
			$(this).dialog('close');
			
			}
		}];;
		var dialog = $('#main')
			.append("<div style='text-align: center;'></div>")
			.find('div:last').dialog(activity_dialog);
		dialog
			.append("<p>Data Inicial: <input/> Horario: <input value='00:00:00.000'/></p>")
			.append("<p>Data Final: <input/> Horario: <input value='23:59:59.999'/></p>")
			.append("<p>Nome do Servico: <input/></p>")
			.append("Criterio:<br/> Data de Inicio: <input name='criterio' type='radio' value='started'/> Data de Termino:<input name='criterio' type='radio' value='completed'/>")
			.find('p:last').prev().find('input:first').datepicker()
			.parent().prev().find('input:first').datepicker();
	}else if(type=='java'){
		var entity_dialog = getCommonDialog();
		entity_dialog.title = "Criar Relatório - "+nome;
		entity_dialog.buttons = [{text:"Criar",click:function(){
			$(this).dialog({
				close:function(){
					try{
					
					var service = $('.actor_form:last').find('.service:last').val();
					var date_criteria = $('.actor_form:last').find('.date_criteria:first').prop('checked')?'completed':'started';
					
					var end = getDateByFormatedDateAndTime(
							$('.actor_form:last').find('.final_date:first').val()
							,$('.actor_form:last').find('.final_date:first').next().val()
							).getTime();
					var start = getDateByFormatedDateAndTime(
							$('.actor_form:last').find('.init_date:first').val()
							,$('.actor_form:last').find('.init_date:first').next().val()
							).getTime();
					}catch(e){}
						
					var config = {
							start:start
							,end:end
							,config:group.config
							,service:service
							,date_criteria : date_criteria
							,group:group
					};
					config = getFormValues();
					config.config = group.config;
					config.group = group;
					var rpc = new jsonrpc.JsonRpc('ReportServlet');
					rpc.call(
							'runReport'
							,group.reportName
							,config
							,function (result) {
								concluido(result,config);
							}
						);
					/*	$.ajax({
					url:group.servlet,
						async: false,
						dataType:'json',
						type:'POST',
						data: $.param(group.params),
						success:function(response){					
							retorno = response;
						}
					});*/
					//concluido(retorno);
				}
			});
			$(this).dialog('close');
			
			}
		}];;
		if(group.form){
			var dialog = $('#main').append("<div style='text-align: center;'></div>")
			.find('div:last').dialog(entity_dialog);
			dialog.load('html/forms/'+group.form);
		}
	}
	
}

function getBufferedProcessByDate(params){
	var retorno = [];
	var start = getDateByFormatedDate(params.start);
	var end = getDateByFormatedDate(params.end);
	var current = start;
	
	while(current<end){
		$.ajax({
			url:'reports/buffer/'+current.getTime()+'.json',
			async:false,
			dataType: 'json',
			success:function(response){
				 (response);
				retorno = retorno.concat(response);
			},
			error:function(response){
				alert("Data não sincronizada: "+current);
			}
			
		});
		current = new Date(current.getTime()+(1000*60*60*24));
	}
	return retorno;
}

function getBufferedActivitiesByDateAndService(config){
	var buffer_path = "http://10.100.110.47:9081/reportsbuffer/ReportsBufferWebService/";
	var start = clearHours(config.start);
	var end = config.end;
	var current = start;
	var retorno = [];
	while(current<end){
		$.ajax({
			url:buffer_path,
			async:false,
			//dataType: 'json',
			data: encodeURIComponent(JSON.stringify({
				method:'getActivitiesByDate'
				,params: {
					date:current.getTime()
				}
			})),
			success:function(response){
				response = eval(response);
				if(response.result){
					for(activity in response.result)
						retorno.push(response.result[activity]);
				}
				else
					alert("Data n�o sincronizada: "+current);
			},
			error:function(response){
				alert("Data n�o sincronizada: "+current);
			}
			
		});
		current = new Date(current.getTime()+(1000*60*60*24));
	}
	return retorno;
}

function clearHours(date){
	date.setHours(0);
	date.setMinutes(0);
	date.setSeconds(0);
	date.setMilliseconds(0);
	return date;
}

function getProcessesByDateAndService(params){
	var dia_em_ms = (1000*60*60*24);
	var interval = (1000*60*60*12);
	var limite = 7;
	var retorno = [];
	var start = params.start;
	var end = params.end;
	var service = params.service;
	var date_criteria = params.date_criteria;
	var current = start;
	if((end-start)>(limite*dia_em_ms)){
		alert("Escolha um intervalo de data menor que "+limite+" dias.");
		return;
	}
	while(current<end){
		var next = new Date(current.getTime()+interval);
		next = (next<end)?next:end;
		var params = {"sql":"select * from itimuser.process where "+date_criteria+">='"
			+dateToSql(current)
			+"'"
			+" and "+date_criteria+"<='"
			+dateToSql(next)+"'"
			+((service)?" and subject_service='"+service+"'":"")
			+" and parent_id=0",
			"getActivities":true,
			"getActivityHistory":true};
		retorno = retorno.concat(syncCallWSV2(params).result);
		current = next;
	}
	return retorno;
}

function getDateByFormatedDate(formatedDate){
	var month = formatedDate.substr(0,2);
	var day = formatedDate.substr(3,2);
	var year = formatedDate.substr(6,4);
	return new Date(year,month-1,day,0,0,0,0);
}

function getDateByFormatedDateAndTime(formatedDate,time){
	var date = getDateByFormatedDate(formatedDate);
	if(time==null||time=="")
		return date;
	var time_arr = time.split(":");
	var hour = ((time_arr.length>0)?time_arr[0]:"00");
	var min = ((time_arr.length>1)?time_arr[1]:"00");
	var seg = ((time_arr.length>2)?time_arr[2]:"00.000");
	date.setHours(hour);
	date.setMinutes(min);
	var segArr = seg.split('.');
	date.setSeconds((segArr.length>0)?segArr[0]:"00");
	date.setMilliseconds((segArr.length>1)?segArr[1]:"000");
	return date;
}

function formatedDateToSql(formatedDate){
	return formatedDate.substr(6,10)+"-"+formatedDate.substr(0,2)+"-"+formatedDate.substr(3,2); 
}

function dateToSql(date){
	var year = date.getUTCFullYear();
	var month = ((date.getUTCMonth()+1)<10)?"0"+(date.getUTCMonth()+1):(date.getUTCMonth()+1);
	var day = (date.getUTCDate()<10)?"0"+date.getUTCDate():date.getUTCDate();
	var hours = (date.getUTCHours()<10)?"0"+date.getUTCHours():date.getUTCHours();
	var minutes =  (date.getUTCMinutes()<10)?"0"+date.getUTCMinutes():date.getUTCMinutes();
	var seconds = (date.getUTCSeconds()<10)?"0"+date.getUTCSeconds():date.getUTCSeconds();
	var milliseconds = (date.getUTCMilliseconds()<10)?"00"+date.getUTCMilliseconds():
		(date.getUTCMilliseconds()<100)? "0"+date.getUTCMilliseconds(): date.getUTCMilliseconds();
	
	return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds+"."+milliseconds;
		
}
$(document).ready(function(){
	preparePage();
	login();
}); 

getKeys = function(obj){
	keys = new Array();
	for(k in obj){
		keys.push(k);
	}
	return keys;
};

Array.prototype.without = function(arr){
	if($.isArray(arr)){
		for(var i=0;i<arr.length;i++){
			this.unshift(arr[i]);
		}
	}else{
		if(this.indexOf(arr)==-1)
			this.unshift(arr);
	}
	return this;
};

function putReportAsTable(base,array){
	base.html("");
	table = base.append("<table class='imagetable'>").find('table');
	for(var i=0;i<array.length;i++){
			//if(i>50)return;
			tr = table.append('<tr/>').find('tr:last');
			$.each(array[i],function(k,elemento){
				if($.isArray(elemento))elemento = elemento.join("<br/>");
				else elemento = (elemento)?(elemento+"").replace(/</," ").replace(/>/," "):"";
				if(i==0)tr.append("<th/>").find('th:last').html(elemento);
				else tr.append("<td/>").find('td:last').html(elemento);
			});
	}
};
function login(){
	var autorizado = false;
	$('#login').dialog({
			modal: true,
			title:("Login"),
			width:250,
			height:280,
			buttons:[{
				text:"Entrar",
				click:function(){
					//$(this).waitingpopup();
					var user = $("#login").find('input:first').val();
					var pass = $("#login").find('input:last').val();
					$.each(autorizados,function(i,user_autorizado){
						if(user_autorizado==user){
							autorizado = true;
							return;
						}
					});
						if(autorizado){
							$.ajax({
								url:'/api/process.v2'
								//url:'http://10.100.110.47:9081/customapiws/JSONRPCServlet'
								//,cache:false
								,type:"GET"
								,dataType:"json"
								,data:encodeURIComponent(JSON.stringify({method:'auth',params:{'user':user,'pass':pass}}))
								,success:function(response){
									/*/LOGIN DESATIVADO
									if(response.error){
										$('#auth_error').html(response.error.message).dialog({
											title: 'Erro'
										});
									}else if(response.result.login){
										$('#login').dialog('close');
										$('#userName').html($("#login").find('input:first').val());
									}else{
										$('#auth_error').dialog({
											title: 'Erro'
										});
									}
									//*/
									$('#login').dialog('close');
									$('#userName').html($("#login").find('input:first').val());
								},
								error:function(response){
									alert(" login response: "+JSON.stringify(response));
								}
							});
							return;
						}else{
							$('#auth_error').html("Acesso Negado").dialog({
								title: 'Erro'
							});
						}
					
			
					
				}
			}],
			resizable: false
		});		
	$('#login').parent().find('a').hide();
}
function logoff(){
	$.ajax({
		url:'/api/process.v2',
		dataType:'json',
		data:JSON.stringify({method:'logoff',params:{}}),
		success:function(response){
			if(response.error){
				$('#auth_error').html(response.error.message).dialog({
					title: 'Erro'
				});
			}else if(!response.result.login){
				var div = $('#main').append("<div/>").find('div:last');
				div.dialog({
					modal:true,
					title:"Logoff Efetuado",
					buttons:[{
						text:"Logar Novamente",
						click:function(){
							$(this).dialog("close");
							login();
							}
						}]
				});
				div.parent().find('a').hide();
			}else{
				$('#auth_error').dialog({
					title: 'Erro'
				});
			}
		}
	});
}

function zuluToDate(zulu){
	try{
		return zulu.substr(6,2)+"/"+zulu.substr(4,2)+"/"+zulu.substr(0,4)+" "+zulu.substr(8,2)+":"+zulu.substr(10,2);
	}catch(e){
		return "";
	}
}
function getRootProcess(process){
	var atual = process;
	while(atual.parent){
		atual = atual.parent;
	}
	return atual;
}

function encode_utf8( s )
{
  return unescape( encodeURIComponent( s ) );
}

function decode_utf8( s )
{
  return decodeURIComponent( escape( s ) );
}

function getFormValues(){
	var ret = new Object();
	$('.form:last').find('*').each(function(){
		var val;
		if($(this).hasClass('date'))
			val = getDateByFormatedDateAndTime($(this).find('input:first').val(),$(this).find('input:last').val()).getTime();
		else if (!$(this).hasClass('hasDatepicker'))
			val = $(this).val();
		var id = $(this).attr('id');
		if(id && val)
			ret[id]=val;
	});
	return ret;
}
/*
function setEscape(str){
	return str.replace(/\n/,"\\n");
}
function writeCSVPage(report){
	var conteudo = "";
	for(var i=0;i<report.length;i++){
		var linha = report[i];
		$.each(linha,function(i,elemento){
			if($.isArray(elemento)){
				elemento.join(' ');
			}
			elemento = elemento.replace('\n',' ');
			elemento = elemento.replace('\r\n',' ');
			elemento = elemento.replace('\\',' ');
		});
		conteudo += linha.join(',')+'\n';
	}
	myWindow=window.open('','','width=700,height=500');
	myWindow.document.write(conteudo);
	myWindow.focus();
}*/

