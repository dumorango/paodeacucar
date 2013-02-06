function manualActivities(result){
	if(result.length>=0){
		relatorio = [];
		relatorio.push(["Nome do Serviço","Processo Root","Id do Processo",
		                "Nome da Atividade",
		                "Data de Inicio",
		                "Data de Conclusao",
		                "Solicitante","Solicitado Para",
		                "Roteado para","Concluido Por",
		                "Resultado"
						 ]);
		$.each(result,function(i,process){
			//var root = process.parentProcessId;
			var activities = getManualActivities(process);
			var root = new Object();
			if(process.rootProcessId>0)
				root = getProcessById(process.rootProcessId);
			$.each(activities,function(i,activity){
				/*relatorio.push([process.serviceName,root.id,root.name,process.id,process.requestor,process.requestee,process.state,process.resultSummar
							       ]);*/
				var routed = [];
				$.each(activity.history.routed,function(j,activityParticipant){
					routed[j] = activityParticipant.id;
				});
				
				relatorio.push([root.subjectService,
				                root.id,
				                //root.name,
				                process.id,
				                activity.name,
				                activity.timeStarted,
				                activity.timeCompleted,
				                process.requesterName,
				                process.requesteeName,
				                routed,
				                activity.history.completedBy,
				                activity.result
				                ]);
							});
			});
			return relatorio;
	}
}