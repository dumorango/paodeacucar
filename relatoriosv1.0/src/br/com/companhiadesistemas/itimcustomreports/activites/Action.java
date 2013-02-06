package br.com.companhiadesistemas.itimcustomreports.activites;

class Action {
	String year;
	String month;
	String day;
	String processName;
	String activityName;
	String actor;
	int activitiesCompleted = 1;
	
	String getKey(){
		return year+month+day+processName+activityName+actor;
	}
}