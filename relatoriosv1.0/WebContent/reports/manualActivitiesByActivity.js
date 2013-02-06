function manualActivitiesByActivity1(result,config){
	relatorio = [];
	if(!result)return null;
	if(result.length>=0){
		relatorio.push(["Ano","Mes","Dia","Servico","Atividade","Abertos","Fechados","SLA Medio em Minutos","SLA Medio em Horas","SLA Medio em Dias"
						 ]);
		var days = new Object();
		var counter = 0;
		$.each(result,function(i,process){
			counter++;
			var root = new Object();
			if(process.rootProcessId>0)
				root = getProcessById(process.rootProcessId);
			var activities = getManualActivities(process);
			var subjectService = root.subjectService;
			subjectService = (subjectService)?subjectService:"Outros";
			$.each(activities,function(i,activity){
				var date = getYMDdate((config.date_criteria=='started'?activity.timeStarted:root.timeCompleted));
				var day = date.day;
				if(!days[day]){
					days[day] = date;
					days[day].activities = new Object();
				}
				if(!days[day].activities[activity.name]){
					days[day].activities[activity.name] = {
							started:0
							,completed:0
							,service:subjectService//serviceName
							,sla:(activity.timeCompleted)?activity.timeCompleted - activity.timeStarted:0
					};
				}
				if(activity.timeStarted)
					days[day].activities[activity.name].started++;
				if(activity.timeCompleted){
					days[day].activities[activity.name].completed++;
					var lifetime = activity.timeCompleted - activity.timeStarted;
					days[day].activities[activity.name].sla=(days[day].activities[activity.name].sla+lifetime)/2;
					//alert("activity: "+activity.name+" started:"+activity.timeStarted+" completed:"+activity.timeCompleted+" sla:"+days[day].activities[activity.name].sla);
				}
			});	
		});
		//alert("days: "+JSON.stringify(days));
		//alert("Counter: "+counter);
		for(k in days){
			var day = days[k];
			for(m in day.activities){
				var activity = day.activities[m];
				relatorio.push([day.year,day.month,day.day,activity.service,m,activity.started+"",activity.completed+""
				                ,(activity.sla/(1000 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60 * 24))+""
				                ]);
			}
		}
		//alert("Relatorio: "+JSON.stringify(relatorio));
	}
	return relatorio;
}

function manualActivitiesByActivity(result,config){
	relatorio = [];
	if(result.length>=0){
		relatorio.push(["Ano","Mes","Dia","Servico","Atividade","Abertos","Fechados","SLA Medio em Minutos","SLA Medio em Horas","SLA Medio em Dias"
						 ]);
		var days = new Object();
		//alert(result.length);
		$.each(result,function(i,root){
			var children = getChildren(root);
			children.push(root);
			//alert("Processes root: "+root.id+"\nroot_name:"+root.name+"\nChildren: "+JSON.stringify(children));
			$.each(children,function(i,process){
				var activities = process.activities;
				var subjectService = root.subjectService;
				subjectService = (subjectService)?subjectService:"Outros";
				$.each(activities,function(i,activity){
					 if(activity.activityType=='M'){
						 var date = getYMDdate((config.date_criteria=='started')?root.timeStarted:root.timeCompleted);
						 //alert(JSON.stringify(config));
						var day = date.day;
						if(!days[day]){
							days[day] = date;
							days[day].activities = new Object();
						}
						if(!days[day].activities[activity.name]){
							days[day].activities[activity.name] = {
									started:0
									,completed:0
									,service:subjectService//serviceName
									,sla:(activity.timeCompleted)?activity.timeCompleted - activity.timeStarted:0
							};
						}
						if(activity.timeStarted)
							days[day].activities[activity.name].started++;
						if(activity.timeCompleted){
							days[day].activities[activity.name].completed++;
							var lifetime = activity.timeCompleted - activity.timeStarted;
							days[day].activities[activity.name].sla=(days[day].activities[activity.name].sla+lifetime)/2;
							//alert("activity: "+activity.name+" started:"+activity.timeStarted+" completed:"+activity.timeCompleted+" sla:"+days[day].activities[activity.name].sla);
						}
					}
				});
			});	
		});
		//alert("days: "+JSON.stringify(days));
		//alert("Counter: "+counter);
		//alert("Activity Counter: "+a_counter);
		for(k in days){
			var day = days[k];
			for(m in day.activities){
				var activity = day.activities[m];
				relatorio.push([day.year,day.month,day.day,activity.service,m,activity.started+"",activity.completed+""
				                ,(activity.sla/(1000 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60 * 24))+""
				                ]);
			}
		}
		//alert("Relatorio: "+JSON.stringify(relatorio));
	}
	return relatorio;
}
function getYMDdate(timeStarted){
	
	if(timeStarted)
		var date = new Date(timeStarted);
		return {
				day:date.getDate()+""//formatedDate.substr(0,2)
				,month:(date.getMonth()+1)+""//formatedDate.substr(3,2)
				,year:date.getFullYear()+""//formatedDate.substr(6,4)
				,date:date+""
		};
	return null;
}
