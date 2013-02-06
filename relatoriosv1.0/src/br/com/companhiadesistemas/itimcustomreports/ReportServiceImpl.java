package br.com.companhiadesistemas.itimcustomreports;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportServiceImpl implements ReportService{

	public ArrayList<ArrayList<String>> runReport(String reportName,
			HashMap<String, ?> params) throws Exception {
		ReportsEnum reportObject = ReportsEnum.valueOf(reportName.toUpperCase());
		return reportObject.runReport(reportName, params);
	}
}
