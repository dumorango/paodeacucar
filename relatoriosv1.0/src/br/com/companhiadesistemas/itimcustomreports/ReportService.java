package br.com.companhiadesistemas.itimcustomreports;

import java.util.ArrayList;
import java.util.HashMap;

public interface ReportService {
	public ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params) throws Exception;
}
