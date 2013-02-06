package br.com.companhiadesistemas.itimcustomreports.activites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.ibm.itim.workflow.model.*;
import com.ibm.itim.workflow.query.InvalidQueryException;

import br.com.companhiadesistemas.itimapi.log.DebugLogger;
import br.com.companhiadesistemas.itimapi.process.*;



public class ReportActiviesByActor {
	
	Logger LOGGER = DebugLogger.LOGGER;
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public static ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params) throws InvalidQueryException, WorkflowException{
		DebugLogger.LOGGER.info("Rodando Relatótio: "+reportName+"\n\tParâmetros: "+params);
		ArrayList<ArrayList<String>> report = new ArrayList<ArrayList<String>>();
		System.out.println("startDate class: "+params.get("init_date").getClass());
		report.add(new ArrayList(){{
			add("Ano");
			add("Mês");
			add("Dia");
			add("Nome do Processo");
			add("Nome da Atividade");
			add("Nome do Atuante");
			add("Número de Atuações");
		}});
		HashMap<String,Action> actions = new HashMap<String,Action>();
		Date startDate = new Date((Long) params.get("init_date"));
		Date endDate = new Date ((Long) params.get("final_date"));
		DebugLogger.LOGGER.info("\t startDate: "+startDate);
		DebugLogger.LOGGER.info("\t endDate: "+endDate);
		String sqlStart = getSqlDate(startDate);
		String sqlEnd = getSqlDate(endDate);
		List<WorkflowProcess> processes =  ProcessSearch.getProcessesBySQL("select * from process where"
				+" SUBJECT_SERVICE='"+(String) params.get("service")+"'"
				+" AND "
				+" STARTED<='"+sqlEnd+"'"
				+" AND"
				+" ( COMPLETED>='"+sqlStart+"' OR STATE = 'R' )"
				);
		int n_manualactivities = 0;
		DebugLogger.LOGGER.info("\t endDate: "+endDate);
		for(WorkflowProcess wkf:processes){
			DebugLogger.LOGGER.info("\tWorkflow: Id: "+wkf.getId()+" Nome: "+wkf.getName());
			for(ActivityEntity ae:ProcessUtil.getManualActivities(new WorkflowProcessEntity(wkf))){
				Date timeCompleted = ae.getValueObject().getTimeCompleted();
				DebugLogger.LOGGER.info("\t\t Atividade: "+ae.getValueObject().getName()+
											"\n\t\t\t timeCompleted:"+timeCompleted);
				if(timeCompleted!=null && timeCompleted.before(endDate)
						&& timeCompleted.after(startDate)){
					n_manualactivities++;
					Action action = new Action();
					action.actor = ActivityUtil.getManualActivityActorId(ae);
					action.activityName = ae.getValueObject().getName();
					action.processName = wkf.getSubjectService();
					action.year = new SimpleDateFormat("yyyy").format(ae.getValueObject().getTimeCompleted());
					action.month =  new SimpleDateFormat("MM").format(ae.getValueObject().getTimeCompleted());
					action.day =   new SimpleDateFormat("dd").format(ae.getValueObject().getTimeCompleted());
					String key = action.getKey();
					if(actions.containsKey(key)){
						actions.get(key).activitiesCompleted++;
						DebugLogger.LOGGER.info("\t\t\tAção: "+action.getKey()+" Já exitente, incrementando contador.");
					}else{
						actions.put(key,action);
						DebugLogger.LOGGER.info("\t\t\tAção: "+action.getKey()+" Não exitente, adicionando ação.");
					}
				}
			}	
		}
		DebugLogger.LOGGER.info("Em "+processes.size()+" processos, e "+n_manualactivities+" atividades manuais. Foram encontradas "+actions.size()+ " ações diferentes. Criando relatório: ");
		for(Action action:actions.values()){
			ArrayList<String> line = new ArrayList<String>();
			line.add(action.year);
			line.add(action.month);
			line.add(action.day);
			line.add(action.processName);
			line.add(action.activityName);
			line.add(action.actor);
			line.add(String.valueOf(action.activitiesCompleted));
			report.add(line);
		}
		return report;
	}
	
	private static String getSqlDate(Date date){
		SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return gmtFormat.format(date);
	}
}


