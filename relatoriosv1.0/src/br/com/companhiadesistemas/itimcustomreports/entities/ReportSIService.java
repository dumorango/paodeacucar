package br.com.companhiadesistemas.itimcustomreports.entities;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.companhiadesistemas.itimcustomreports.ReportService;

public class ReportSIService implements ReportService {
	
	public ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params){
		return ReportSI.runReport((String) params.get("filter"));
		
	}
	public ArrayList<ArrayList<String>> runReport(String filter) {
		return runReport(filter,null);
	}
}
