package br.com.companhiadesistemas.extensions.model;

import java.util.List;

import br.com.companhiadesistemas.itimapi.wrappers.CustomAccountSearchWrapper;

import com.ibm.itim.script.*;
import com.ibm.itim.script.extensions.model.AccountModelExtension;



public class CustomAccountModelExtension extends AccountModelExtension{

	public List getContextItems(){
		allBeans = super.getContextItems();
		allBeans.remove(1);
		allBeans.add(accountSearchConstructor);
		return allBeans;
	}
	
	private ContextItem accountSearchConstructor;
	private List allBeans;
	
	public void initialize(ScriptInterface si,ScriptContextDAO dao) throws ScriptException, IllegalArgumentException{
		super.initialize(si, dao);
		accountSearchConstructor = ContextItem.createConstructor("AccountSearch",CustomAccountSearchWrapper.class); 
	}
}
