
function bufferedManualActivitiesByActivity(result,config){
	relatorio = [];
	if(result.length>=0){
		relatorio.push(["Ano","Mes","Dia","Atividade","Abertos","Fechados","SLA Medio em Minutos","SLA Medio em Horas","SLA Medio em Dias"
						 ]);
		var days = new Object();
				$.each(result,function(i,activity){
					 if(activity.activityType=='M'){
						 var date = getYMDdate((config.date_criteria=='started')?activity.timeStarted:activity.timeCompleted);
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
									//,service:subjectService//serviceName
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
		for(k in days){
			var day = days[k];
			for(m in day.activities){
				var activity = day.activities[m];
				relatorio.push([day.year,day.month,day.day,m,activity.started+"",activity.completed+""
				                ,(activity.sla/(1000 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60))+""
				                ,(activity.sla/(1000 * 60 * 60 * 24))+""
				                ]);
			}
		}
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
