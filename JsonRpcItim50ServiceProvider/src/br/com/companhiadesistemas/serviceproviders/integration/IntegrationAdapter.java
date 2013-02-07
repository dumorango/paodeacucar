package br.com.companhiadesistemas.serviceproviders.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("rawtypes")
public abstract class IntegrationAdapter<T>{
	
	public abstract T add(Map connectionProperties,T account) throws Exception;
	
	public abstract T modify(Map connectionProperties,T oldAccount,T newAccount) throws Exception;
	
	public abstract T changePassword(Map connectionProperties,T account,byte newPassword[]) throws Exception;
	
	public abstract T suspend(Map connectionProperties,T account) throws Exception;
	
	public abstract T restore(Map connectionProperties,T account) throws Exception;
	
	public abstract T delete(Map connectionProperties,T account)throws Exception;
	
	public abstract Collection<?> search(Map connectionProperties) throws Exception;
	
	public abstract boolean test(Map connectionProperties) throws Exception;
}
