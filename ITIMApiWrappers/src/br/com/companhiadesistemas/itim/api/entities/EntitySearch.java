package br.com.companhiadesistemas.itim.api.entities;

import java.util.*;

import com.ibm.itim.dataservices.model.*;
import com.ibm.itim.dataservices.model.domain.*;

public class EntitySearch {

	@SuppressWarnings("unchecked")
	public static Collection<PersonEntity> searchPersonByFilterAsCollection(String filter) throws Exception{
		return searchPersonByFilter(filter).toCollection();
	}
	
	public static SearchResults searchPersonByFilter(String filter) throws ModelCommunicationException, ObjectNotFoundException, PartialResultsException{
		PersonSearch ps = new PersonSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE);
		searchParams.setPageSize(10);
		CompoundDN cdn = new CompoundDN();
		cdn.append(new DistinguishedName("ou=GPA,DC=TIM"));
		SearchResults results = ps.searchByFilter(new CompoundDN(cdn),
	        filter,
	        searchParams);
		return results;
	}
	public static PersonEntity lookUpPerson(DistinguishedName dn) throws ModelCommunicationException, ObjectNotFoundException{
		PersonSearch ps = new PersonSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE);
		CompoundDN cdn = new CompoundDN();
		cdn.append(new DistinguishedName("ou=GPA,DC=TIM"));
		return ps.lookup(dn);
	}
	
	public static PersonEntity lookUpPerson(String dn) throws ModelCommunicationException, ObjectNotFoundException{
		return lookUpPerson(new DistinguishedName(dn));
	}
	public static SearchResults searchAccountByFilter(String filter) throws ObjectNotFoundException, ModelCommunicationException, PartialResultsException{
		AccountSearch as = new AccountSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE );
		SearchResults sr; 
		return as.searchByFilter(new CompoundDN(new DistinguishedName("ou=GPA,DC=TIM")),
					filter, 
					searchParams);
	}
	
	public static AccountEntity lookUpAccount(DistinguishedName dn) throws ModelCommunicationException, ObjectNotFoundException{
		AccountSearch as = new AccountSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE);
		CompoundDN cdn = new CompoundDN();
		cdn.append(new DistinguishedName("ou=GPA,DC=TIM"));
		return as.lookup(dn);
	}
	
	public static AccountEntity lookUpAccount(String dn) throws ModelCommunicationException, ObjectNotFoundException{
		return lookUpAccount(new DistinguishedName(dn));
	}
	
	public static ServiceEntity lookUpService(DistinguishedName dn) throws ModelCommunicationException, ObjectNotFoundException{
		ServiceSearch ss = new ServiceSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE);
		CompoundDN cdn = new CompoundDN();
		cdn.append(new DistinguishedName("ou=GPA,DC=TIM"));
		return ss.lookup(dn);
	}
	
	public static ServiceEntity lookUpService(String dn) throws ModelCommunicationException, ObjectNotFoundException{
		return lookUpService(new DistinguishedName(dn));
	}
	
	public static SearchResults getAccountsByServiceName(String serviceName) throws PartialResultsException, ObjectNotFoundException, ModelCommunicationException{
		Collection<ServiceEntity> services = searchServiceByFilter("(erServiceName="+serviceName+")");
		if(services.isEmpty()) return null;
		String serviceDN = ((ServiceEntity) services.iterator().next()).getDistinguishedName().toString();
		return searchAccountByFilter("(erService="+serviceDN+")");
		
	}
	
	public static SearchResults getAccountsByServiceNameAndOwner(String serviceName,PersonEntity owner) throws PartialResultsException, ObjectNotFoundException, ModelCommunicationException{
		Collection<ServiceEntity> services = searchServiceByFilter("(erServiceName="+serviceName+")");
		if(services.isEmpty()) return null;
		String serviceDN = ((ServiceEntity) services.iterator().next()).getDistinguishedName().toString();
		return searchAccountByFilter("(&(erService="+serviceDN+")(owner="+owner.getDistinguishedName().getAsString()+"))");
	}
	public static Collection<ServiceEntity> searchServiceByFilter(String filter) throws ObjectNotFoundException, ModelCommunicationException, PartialResultsException{
		ServiceSearch ss = new ServiceSearch();
		SearchParameters searchParams = new SearchParameters();
		searchParams.setScope(SearchParameters.ONELEVEL_SCOPE );
		SearchResults sr; 
		sr = ss.searchByFilter(new CompoundDN(new DistinguishedName("ou=GPA,DC=TIM")),
					filter, 
					searchParams);
		return sr.toCollection();
	}
	

}