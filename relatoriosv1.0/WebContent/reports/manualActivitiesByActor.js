function manualActivitiesByActor(result){
	relatorio = [];
	if(result==null)return null;
	if(result.length>=0){
		relatorio.push(["Processo Root","Ano","Mes","Dia","Servico","Atividade","Atuante","Abertos","Fechados"
						 ,"SLA Medio em Minutos","SLA Medio em Horas","SLA Medio em Dias"
						 ]);
		var days = new Object();
		var root = new Object();
		$.each(result,function(i,process){
			if(process.rootProcessId>0)
				root = getProcessById(process.rootProcessId);
			var subjectService = root.subjectService;
			subjectService = (subjectService)?subjectService:"Outros";
			var actors = getManualActivities(process);
			$.each(actors,function(i,activity){
				var date = getYMDdate(activity.timeStarted);
				var completedBy = activity.history.completedBy;
				var day = date.day;
				if(!days[day]){
					days[day] = date;
					days[day].actors = new Object();
				}
				if(!days[day].actors[completedBy]){
					days[day].actors[completedBy] = {
							started:0
							,completed:0
							,service:subjectService
							,activity:activity.name
							,sla:(activity.timeCompleted)?activity.timeCompleted - activity.timeStarted:0
					};
				}
				if(activity.timeStarted)
					days[day].actors[completedBy].started++;
				if(activity.timeCompleted){
					days[day].actors[completedBy].completed++;
					var lifetime = activity.timeCompleted - activity.timeStarted;
					days[day].actors[completedBy].sla=(days[day].actors[completedBy].sla+lifetime)/2;
				}
			});
		});
		//alert("days: "+JSON.stringify(days));
		for(k in days){
			var day = days[k];
			for(m in day.actors){
				var actor = day.actors[m];
				relatorio.push([root.id,day.year,day.month,day.day,actor.service,actor.activity,m,actor.started+"",actor.completed+""
				                ,(actor.sla/3600)+""
				                ,(actor.sla/(3600*60))+""
				                ,(actor.sla/(3600*60*24))+""
				                ]);
			}
		}
		//alert("Relatorio: "+JSON.stringify(relatorio));
	}
	return relatorio;
}

function manualActivitiesByActor(result,config){
	relatorio = [];
	if(result.length>=0){
		relatorio.push(["Ano","Mes","Dia","Servico","Atividade","Atuante","Abertos","Fechados"
						 ,"SLA Medio em Minutos","SLA Medio em Horas","SLA Medio em Dias"
						 ]);
		var days = new Object();
		$.each(result,function(i,root){
			var children = getChildren(root);
			children.push(root);
			$.each(children,function(i,process){
				var subjectService = root.subjectService;
				subjectService = (subjectService)?subjectService:"Outros";
				var activities = process.activities;
				$.each(activities,function(i,activity){
					//alert("Manual Activity: "+JSON.stringify(activity));
					if(activity.activityType=='M'){
						var date = getYMDdate((config.date_criteria=='started')?root.timeStarted:root.timeCompleted);
						var history = (activity.history)?activity.history:new Object();
						var completedBy = (history.completedBy)?history.completedBy:"Sem Atuante";
							var day = date.day;
							if(!days[day]){
								days[day] = date;
								days[day].actors = new Object();
							}
							if(!days[day].actors[completedBy]){
								days[day].actors[completedBy] = {
										activities : {}
								};
							}
							if(!days[day].actors[completedBy].activities[activity.name]){
								days[day].actors[completedBy].activities[activity.name] = {
										started:0
										,completed:0
										,service:subjectService
										,name:activity.name
										,sla:(activity.timeCompleted)?activity.timeCompleted - activity.timeStarted:0
								};
							}
							if(activity.timeStarted)
								days[day].actors[completedBy].activities[activity.name].started++;
							if(activity.timeCompleted){
								days[day].actors[completedBy].activities[activity.name].completed++;
								var lifetime = activity.timeCompleted - activity.timeStarted;
								//alert("Atividade: "+activity.name+" lifetime: "+lifetime+ "slaAtual: "+days[day].actors[completedBy].sla);
								days[day].actors[completedBy].activities[activity.name].sla=(days[day].actors[completedBy].activities[activity.name].sla+lifetime)/2;
								//alert("Novo SLA: "+days[day].actors[completedBy].sla);
							}
					}
				});
			});
		});
		//alert("days: "+JSON.stringify(days));
		for(k in days){
			var day = days[k];
			for(m in day.actors){
				var actor = day.actors[m];
				for(activity_name in actor.activities){
					var activity = actor.activities[activity_name];
					relatorio.push([day.year,day.month,day.day,activity.service,activity.name,m,activity.started+"",activity.completed+""
					                ,(activity.sla/(1000 * 60))+""
					                ,(activity.sla/(1000 * 60 * 60))+""
					                ,(activity.sla/(1000 * 60 * 60 * 24))+""
				                ]);
				}
			}
		}
		//alert("Relatorio: "+JSON.stringify(relatorio));
	}
	return relatorio;
}
function getYMDdate(timeStarted){
	
	if(timeStarted){
		var date = new Date(timeStarted);
		return {
				day:date.getDate()+""//formatedDate.substr(0,2)
				,month:(date.getMonth()+1)+""//formatedDate.substr(3,2)
				,year:date.getFullYear()+""//formatedDate.substr(6,4)
				,date:date+""
		};
	}
	return null;
}
