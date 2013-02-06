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

public class ReportProcessTotalByStartDate {
	Logger LOGGER = DebugLogger.LOGGER;
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public static ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params) throws InvalidQueryException, WorkflowException{
		DebugLogger.LOGGER.info("Rodando Relatótio: "+reportName+"\n\tParâmetros: "+params);
		ArrayList<ArrayList<String>> report = new ArrayList<ArrayList<String>>();
		System.out.println("startDate class: "+params.get("init_date").getClass());
		report.add(new ArrayList(){{
			add("Total");
		}});
		HashMap<String,Action> actions = new HashMap<String,Action>();
		Date startDate = new Date((Long) params.get("init_date"));
		Date endDate = new Date ((Long) params.get("final_date"));
		String actorId = (String)params.get("actor");
		DebugLogger.LOGGER.info("\t startDate: "+startDate);
		DebugLogger.LOGGER.info("\t endDate: "+endDate);
		String sqlStart = Util.getSqlDate(startDate);
		String sqlEnd = Util.getSqlDate(endDate);
		List<WorkflowProcess> processes =  ProcessSearch.getProcessesBySQL("select * from process where"
				+" STARTED>='"+sqlStart+"'"
				+" AND"
				+" COMPLETED<='"+sqlEnd+"'"
				);
		ArrayList line = new ArrayList();
		line.add(String.valueOf(processes.size()));
		report.add(line);
		return report;
	}
}
