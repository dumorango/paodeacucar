package br.com.companhiadesistemas.itimcustomreports.entities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.itimapi.entities.EntitiesUtil;
import br.com.companhiadesistemas.itimapi.entities.EntitySearch;
import br.com.companhiadesistemas.itimcustomreports.ReportService;

import com.ibm.itim.dataservices.model.domain.PersonEntity;


public class ReportXeroxService implements ReportService {
	
	public ArrayList<ArrayList<String>> runReport(String reportName,HashMap<String,?> params){
		return ReportXerox.runReport((String) params.get("filter"));
		
	}

	public ArrayList<ArrayList<String>> runReport(String filter) {
		return runReport(filter,null);
	}
}
