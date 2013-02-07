package br.com.companhiadesistemas.serviceproviders.integration;


import java.util.*;

@SuppressWarnings({ "rawtypes" })
public interface IntegrationInterface {

	public boolean test(Map connectionProperties,Collection<String> objectClasses) throws Exception; 
			
	public Object add(Map connectionProperties,Object account) throws Exception;
	
	public Object modify(Map connectionProperties,Object oldAccount,Object newAccount) throws Exception;
	
	public Object changePassword(Map connectionProperties,Object account,byte newPassword[]) throws Exception;
	
	public Object suspend(Map connectionProperties,Object account) throws Exception;
	
	public Object restore(Map connectionProperties,Object account) throws Exception;
	
	public Object delete(Map connectionProperties,Object account)throws Exception;
	
	public ArrayList<?> search(Map connectionProperties,Collection<String> objectClasses) throws Exception;
	
}
