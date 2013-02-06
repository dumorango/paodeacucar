function manualActivitiesByService(result){
	relatorio = [];
	if(!result)return null;
	if(result.length>=0){
		relatorio.push(["Ano","Mes","Dia","Servico","Abertos","Fechados"
						 ]);
		var days = new Object();
		$.each(result,function(i,root){
			var children = getChildren(root);
			children.push(root);
			$.each(children,function(i,process){
				var activities = process.activities;
				var subjectService = root.subjectService;
				subjectService = (subjectService)?subjectService:"Outros";
				$.each(activities,function(i,activity){
					if(activity.activityType=='M'){
						//alert("Manual Activity: "+JSON.stringify(activity));
						var date = getYMDdate((config.date_criteria=='started')?root.timeStarted:root.timeCompleted);
						var day = date.day;
						if(!days[day]){
							days[day] = date;
							days[day].services = new Object();
							
						}
						if(!days[day].services[subjectService]){
							days[day].services[subjectService] = {
									started:0
									,completed:0
							};
						}
						if(activity.timeStarted)
							days[day].services[subjectService].started++;
						if(activity.timeCompleted)
							days[day].services[subjectService].completed++;
					}
				});
			});
		});
		//alert("days: "+JSON.stringify(days));
		for(k in days){
			var day = days[k];
			for(m in day.services){
				var service = day.services[m];
				relatorio.push([day.year,day.month,day.day,m,service.started+"",service.completed+""]);
			}
		}
		//alert("Relatorio: "+JSON.stringify(relatorio));
	
	return relatorio;
	}
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
