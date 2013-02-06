package br.com.companhiadesistemas.itimcustomreports;

import java.util.ArrayList;
import java.util.HashMap;

import com.ibm.itim.workflow.model.WorkflowException;
import com.ibm.itim.workflow.query.InvalidQueryException;

import br.com.companhiadesistemas.itimcustomreports.activites.ReportActionsByActor;
import br.com.companhiadesistemas.itimcustomreports.activites.ReportActiviesByActor;
import br.com.companhiadesistemas.itimcustomreports.activites.ReportProcessByMonth;
import br.com.companhiadesistemas.itimcustomreports.activites.ReportProcessTotalByStartDate;

public enum ReportsEnum {
	BYACTOR,BYPROCESSESBYMONTH,BYACTIONSFORACTOR,REPORTTOTALPROCESSBYSTARTDATE;
	ArrayList<ArrayList<String>> runReport(String reportName,
			HashMap<String, ?> params) throws Exception{
		switch(this){
			case BYACTOR:
				return ReportActiviesByActor.runReport(reportName, params);
			case BYPROCESSESBYMONTH:
				return ReportProcessByMonth.runReport(reportName, params);
			case BYACTIONSFORACTOR:
				return ReportActionsByActor.runReport(reportName, params);
			case REPORTTOTALPROCESSBYSTARTDATE:
				return ReportProcessTotalByStartDate.runReport(reportName, params);
			default:
				return null;
		}
	}
}
