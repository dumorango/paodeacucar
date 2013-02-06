package br.com.companhiadesistemas.itimcustomreports.activites;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import br.com.companhiadesistemas.itimapi.log.DebugLogger;
import br.com.companhiadesistemas.itimapi.process.ActivityUtil;
import br.com.companhiadesistemas.itimapi.process.ProcessSearch;
import br.com.companhiadesistemas.itimapi.process.ProcessUtil;

import com.ibm.itim.workflow.model.ActivityEntity;
import com.ibm.itim.workflow.model.WorkflowException;
import com.ibm.itim.workflow.model.WorkflowProcess;
import com.ibm.itim.workflow.model.WorkflowProcessEntity;
import com.ibm.itim.workflow.query.InvalidQueryException;

public class ReportProcessByMonth {
	Logger LOGGER = DebugLogger.LOGGER;
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public static ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params) throws InvalidQueryException, WorkflowException, ParseException{
		DebugLogger.LOGGER.info("Rodando Relatótio: "+reportName+"\n\tParâmetros: "+params);
		ArrayList<ArrayList<String>> report = new ArrayList<ArrayList<String>>();
		report.add(new ArrayList(){{
			add("Serviço");
			add("Setembro");
			add("Outubro");
			add("Novembro");
		}});
		final String service_name = (String) params.get("service");
		HashMap<String,Action> actions = new HashMap<String,Action>();
		SimpleDateFormat sdf_setembro = new SimpleDateFormat("yyyy/MM/dd");
		String setembro= getSqlDate(sdf_setembro.parse("2012/09/01"));
		String outubro= getSqlDate(sdf_setembro.parse("2012/10/01"));
		String novembro= getSqlDate(sdf_setembro.parse("2012/11/01"));
		String dezembro = getSqlDate(sdf_setembro.parse("2012/12/01"));
		final List<WorkflowProcess> processes_setembro =  ProcessSearch.getProcessesBySQL("select * from process where"
				+" SUBJECT_SERVICE='"+service_name+"'"
				+" AND "
				+" STARTED>='"+setembro+"'"
				+" AND"
				+" STARTED<='"+outubro+"'"
				);
		final List<WorkflowProcess> processes_outubro =  ProcessSearch.getProcessesBySQL("select * from process where"
				+" SUBJECT_SERVICE='"+(String) params.get("service")+"'"
				+" AND "
				+" STARTED>='"+outubro+"'"
				+" AND"
				+" STARTED<='"+novembro+"'"
				);
		final List<WorkflowProcess> processes_novembro =  ProcessSearch.getProcessesBySQL("select * from process where"
				+" SUBJECT_SERVICE='"+(String) params.get("service")+"'"
				+" AND "
				+" STARTED>='"+novembro+"'"
				+" AND"
				+" ( STARTED<='"+dezembro+"' OR STATE = 'R' )"
				);
		report.add(new ArrayList(){{
			add(service_name);
			add(processes_setembro.size());
			add(processes_outubro.size());
			add(processes_novembro.size());
		}});
		return report;
	}
	private static String getSqlDate(Date date){
		SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return gmtFormat.format(date);
	}
}
