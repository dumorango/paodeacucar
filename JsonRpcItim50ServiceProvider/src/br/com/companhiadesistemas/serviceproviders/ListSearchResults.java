package br.com.companhiadesistemas.serviceproviders;

import java.util.*;
import com.ibm.itim.common.*;
import com.ibm.itim.dataservices.model.*;
import com.ibm.itim.remoteservices.exception.RemoteServicesException;
import com.ibm.itim.remoteservices.provider.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ListSearchResults implements
com.ibm.itim.remoteservices.provider.SearchResults {
	Iterator<Object> i;
	RequestStatus status;
	Collection objectClasses;
	GenericProvider provider;
	ListSearchResults(List<Object> l,GenericProvider provider){
		this.provider = provider;
		if(l==null || l.isEmpty()){
			this.status = new RequestStatus(
					RequestStatus.UNSUCCESSFUL,
					"Unsuccessful"
					);
		}else{
			this.status = new RequestStatus(
					RequestStatus.SUCCESSFUL,
					"Successful"
					);
		}
		this.i=l.iterator();
		this.objectClasses=this.getObjectClass(provider.spi);
		System.out.println("ListSearchResults - List: "+l);
	}
	
	public ListSearchResults() {
		this.status = new RequestStatus(
				RequestStatus.UNSUCCESSFUL,
				"Unsuccessful"
				);
		this.i = new ArrayList().iterator();
		this.objectClasses = new ArrayList();
	}
	public void close() throws RemoteServicesException {
		
	}

	public RequestStatus getRequestStatus() {
		System.out.println("ListSearchResults - getRequestStatus() - START - status:"+this.status.getStatusAsString());
		return this.status;
	}

	public boolean hasNext() throws RemoteServicesException {
		System.out.println("ListSearchResults - hasNext() - START");
		return i.hasNext();
		
	}

	public SearchResult next() throws RemoteServicesException {
		System.out.println("ListSearchResults - next() - START");
		AttributeValues avs;
		try {
			avs = provider.getEntityAsAttributeValues(i.next());
		} catch (Exception e) {
			return null;
		}
		String dn = avs.get("dn").getValueString();
		avs.remove("dn");
		AttributeValue objectClass = avs.get("objectClass");
		SearchResult sr = new SearchResult(
					dn
					,(objectClass!=null)?objectClass.getValues():this.objectClasses
					,avs
				);
		return (SearchResult) sr;
	}
	
	public Collection getObjectClass(ServiceProviderInformation spi) {
		Collection objectClasses = new ArrayList();
		if (spi != null) {
			String profileName = spi.getServiceProfileName();
			String tenantDN = spi.getProperties().getProperty(
					"com.ibm.itim.remoteservices.ResourceProperties.TENANT_DN");
			ServiceProfile serviceProfile = (ServiceProfile) ProfileLocator
					.getProfileByName(new DistinguishedName(tenantDN),
							profileName);
			String accountClassName = serviceProfile.getAccountClass();
			objectClasses.add(accountClassName);
		}
		return objectClasses;
	}
	

	
	
}
