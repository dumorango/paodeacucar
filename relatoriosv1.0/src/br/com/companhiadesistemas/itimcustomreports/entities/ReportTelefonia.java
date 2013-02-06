package br.com.companhiadesistemas.itimcustomreports.entities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.itimapi.entities.EntitiesUtil;
import br.com.companhiadesistemas.itimapi.entities.EntitySearch;

import com.ibm.itim.dataservices.model.SearchResultsIterator;
import com.ibm.itim.dataservices.model.domain.PersonEntity;

public class ReportTelefonia {
	public static ArrayList<ArrayList<String>> runReport(String filter){
		try{
			EntitiesUtil util = new EntitiesUtil();
			ArrayList<ArrayList<String>> report = new ArrayList<ArrayList<String>>();
			ArrayList<String> header = new ArrayList<String>(){
				{
			            add("ID");
			            add("Nome Completo");
			            add("Cargo/Função");
			            add("Departamento/Empresa");
			            add("Centro de Custo (Alocação)");
			            add("Email");
			            add("Id do Supervisor");
			            add("Nome do Supervisor");
			            add("Status");
			      }
			};
			report.add(header);
			SearchResultsIterator results_iterator = EntitySearch.searchPersonByFilter(filter).iterator();
			while(results_iterator.hasNext()){
				PersonEntity pe = (PersonEntity) results_iterator.next();	System.out.println("Person: "+util.getAttributeValueString(pe,"cn"));
				PersonEntity supervisor = util.getSupervisor(pe);
				ArrayList<String> line = new ArrayList<String>();
				line.add(util.getAttributeValueString(pe,"uid"));
				line.add(util.getAttributeValueString(pe,"cn"));
				line.add(util.getFuncao(pe));
				line.add(util.getDepartamento(pe));
				line.add(util.getCentroDeCusto(pe));
				line.add(util.getMail(pe));
				line.add(util.getAttributeValueString(supervisor, "uid"));
				line.add(util.getAttributeValueString(supervisor,"cn"));
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
