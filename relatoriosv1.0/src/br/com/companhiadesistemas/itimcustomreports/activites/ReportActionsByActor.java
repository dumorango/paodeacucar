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



public class ReportActionsByActor {
	
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
			add("Horário de Atuação");
			add("Id do Processo Root");
		}});
		HashMap<String,Action> actions = new HashMap<String,Action>();
		Date startDate = new Date((Long) params.get("init_date"));
		Date endDate = new Date ((Long) params.get("final_date"));
		String actorId = (String)params.get("actor");
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
						String activity_actor_id = ActivityUtil.getManualActivityActorId(ae);
						if(activity_actor_id!=null && activity_actor_id.contains(actorId)){
							ArrayList<String> line = new ArrayList<String>();
							line.add(new SimpleDateFormat("yyyy").format(ae.getValueObject().getTimeCompleted()));
							line.add(new SimpleDateFormat("MM").format(ae.getValueObject().getTimeCompleted()));
							line.add(new SimpleDateFormat("dd").format(ae.getValueObject().getTimeCompleted()));
							line.add(wkf.getSubjectService());
							line.add(ae.getValueObject().getName());
							line.add(activity_actor_id);
							line.add(new SimpleDateFormat("HH:mm:ss").format(ae.getValueObject().getTimeCompleted()));
							line.add(String.valueOf(wkf.getId()));
							report.add(line);
						}
				}
			}	
		}
		DebugLogger.LOGGER.info("Em "+processes.size()+" processos, e "+n_manualactivities+" atividades manuais. Foram encontradas "+(report.size()-1)+ " ações diferentes para o usuário "+actorId+" Criando relatório: ");
		return report;
	}
	
	private static String getSqlDate(Date date){
		SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return gmtFormat.format(date);
	}
}


