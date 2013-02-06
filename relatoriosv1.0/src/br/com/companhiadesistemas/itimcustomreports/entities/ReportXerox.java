package br.com.companhiadesistemas.itimcustomreports.entities;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.directory.SearchResult;

import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.itimapi.entities.EntitiesUtil;
import br.com.companhiadesistemas.itimapi.entities.EntitySearch;

import com.ibm.itim.dataservices.model.domain.PersonEntity;
import com.ibm.itim.dataservices.model.SearchResults;
import com.ibm.itim.dataservices.model.SearchResultsIterator;



public class ReportXerox {
	public static ArrayList<ArrayList<String>> runReport(String filter){
		try{
			EntitiesUtil util = new EntitiesUtil();
			ArrayList<ArrayList<String>> report = new ArrayList<ArrayList<String>>();
			ArrayList<String> header = new ArrayList<String>(){
				{
			            add("ID");
			            add("Nome Completo");
			            add("Centro de Custo (Alocação)");
			            add("Nome do Gestor");
			            add("Status");
			      }
			};
			report.add(header);
			SearchResultsIterator results_iterator = EntitySearch.searchPersonByFilter(filter).iterator();
			while(results_iterator.hasNext()){
				PersonEntity pe = (PersonEntity) results_iterator.next();
				ArrayList<String> line = new ArrayList<String>();
				line.add(util.getAttributeValueString(pe,"uid"));
				line.add(util.getAttributeValueString(pe,"cn"));
				line.add(util.getCentroDeCusto(pe));
				line.add(util.getAttributeValueString(util.getSupervisor(pe),"cn"));
				line.add((util.isActive(pe)?"ATIVO":"INATIVO"));
				report.add(line);
			}
				return report;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
