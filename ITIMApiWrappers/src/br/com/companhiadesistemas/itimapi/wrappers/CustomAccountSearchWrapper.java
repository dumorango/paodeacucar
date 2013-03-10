package br.com.companhiadesistemas.itimapi.wrappers;

import java.util.ArrayList;
import java.util.Collection;

import br.com.companhiadesistemas.itim.api.entities.EntitySearch;

import com.ibm.itim.dataservices.model.DirectoryObjectEntity;
import com.ibm.itim.dataservices.model.ModelCommunicationException;
import com.ibm.itim.dataservices.model.ObjectNotFoundException;
import com.ibm.itim.dataservices.model.PartialResultsException;
import com.ibm.itim.dataservices.model.SearchResults;
import com.ibm.itim.dataservices.model.SearchResultsIterator;
import com.ibm.itim.dataservices.model.domain.AccountEntity;
import com.ibm.itim.dataservices.model.domain.Supervisor;
import com.ibm.itim.script.ScriptContextDAO;
import com.ibm.itim.script.ScriptEvaluationException;
import com.ibm.itim.script.extensions.model.ContextStorageManager;
import com.ibm.itim.script.extensions.model.impl.AccountSearchBeanImpl;
import com.ibm.itim.script.wrappers.generic.AccountWrapper;
import com.ibm.itim.script.wrappers.generic.ProtectedAccountWrapper;

public class CustomAccountSearchWrapper extends AccountSearchBeanImpl{
	
	ScriptContextDAO dao = ContextStorageManager.getScriptContext();
	public void teste(){
		System.out.println(this.getClass()+" Teste");
	}
	
	@SuppressWarnings("rawtypes")
	public Object searchByFilter(String ldapFilter) throws ScriptEvaluationException{
		SearchResults result;
		try {
			result = EntitySearch.searchAccountByFilter(ldapFilter);
		SearchResultsIterator sri = result.iterator();
		ArrayList retorno_list = new ArrayList();
		for(int i=0;sri.hasNext();i++){
			//retorno_list.add(new ProtectedAccountWrapper(((DirectoryObjectEntity) sri.next()).getDirectoryObject()));
			retorno_list.add(dao.addContextItem((DirectoryObjectEntity)sri.next()));
		} 
		return retorno_list.toArray();
	} catch (Exception e) {
		e.printStackTrace();
		throw new ScriptEvaluationException(e.getMessage(),e);
	} 
	}
}