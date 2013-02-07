package br.com.companhiadesistemas.serviceproviders;

import java.io.IOException;
import java.util.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.serviceproviders.integration.IntegrationInterface;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationLayersEnum;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;

import com.ibm.itim.common.AttributeChangeIterator;
import com.ibm.itim.common.AttributeChangeOperation;
import com.ibm.itim.common.AttributeChanges;
import com.ibm.itim.common.AttributeValue;
import com.ibm.itim.common.AttributeValueIterator;
import com.ibm.itim.common.AttributeValues;
import com.ibm.itim.dataservices.model.*;
import com.ibm.itim.dataservices.model.domain.*;
import com.ibm.itim.remoteservices.exception.RemoteServicesException;
import com.ibm.itim.remoteservices.provider.*;
import com.ibm.itim.remoteservices.provider.SearchResults;

@SuppressWarnings({ "rawtypes", "unchecked","deprecation" })
public class GenericProvider implements ServiceProvider{
	
	ServiceProviderInformation spi;
	IntegrationInterface wrapper;
	GenericProvider(ServiceProviderInformation spi) throws Exception {
		this.spi = spi;
		wrapper = IntegrationLayersEnum.getIntegrationLayerByProperties(spi.getProperties());
	}
	
	public RequestStatus add(String objectClass, AttributeValues attributes,
			String requestID){
		try {
			Object obj =  wrapper.add(spi.getProperties(),getAttributeValuesAsEntity(attributes));
			return (RequestStatus) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RequestStatus changePassword(String entityDN, byte newPassword[],
			String requestID) {
			try {
				Object obj =  wrapper.changePassword(
						spi.getProperties(),
						getAttributeValuesAsEntity(getEntityAttributeValues(entityDN))
						,newPassword
						);
				
				return (RequestStatus)obj;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
		
	}

	public RequestStatus delete(String entityDN, String requestID) {
		try {
			Object obj = wrapper.delete(spi.getProperties(),getAttributeValuesAsEntity(getEntityAttributeValues(entityDN)));
			return (RequestStatus) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ServiceProviderInformation getServiceProviderInfo() {
		return this.spi;
	}

	public RequestStatus modify(String entityDN,
			AttributeChanges attributeChanges, String requestID) {
			try {
				AttributeValues atual = getEntityAttributeValues(entityDN);
				Object obj = wrapper.modify(
						spi.getProperties(),
						getAttributeValuesAsEntity(atual),
						getAttributeValuesAsEntity(applyChanges(attributeChanges,atual))
						);
				return (RequestStatus) obj;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
	}

	public RequestStatus restore(String entityDN, byte newPassword[],
			String requestID) {
		try {
			Object obj = wrapper.restore(spi.getProperties(),getAttributeValuesAsEntity(getEntityAttributeValues(entityDN)));
			return (RequestStatus) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	public SearchResults search(SearchCriteria searchCriteria, String requestID) {
		try {
			return new ListSearchResults((List<Object>) wrapper.search(spi.getProperties(),getObjectClass(spi)),this);
		} catch (RemoteServicesException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public RequestStatus suspend(String entityDN, String requestID) {
		try {
			Object obj = wrapper.suspend(spi.getProperties(),getAttributeValuesAsEntity(getEntityAttributeValues(entityDN)));
			return (RequestStatus) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	public boolean test() throws RemoteServicesException {
		try{
			return wrapper.test(spi.getProperties(),getObjectClass(spi));
		}catch(Exception ex){
			throw new RemoteServicesException(ex);
		} catch (Throwable ex) {
			throw new RemoteServicesException(ex);
		}
	}
	
	public Collection<String> getObjectClass(ServiceProviderInformation spi) {
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
	
	DirectoryObject getDirectoryObject(String entityDN) throws ModelCommunicationException, ObjectNotFoundException{
		return new AccountSearch().lookup(new DistinguishedName(entityDN))
				.getDirectoryObject();
	}
	AttributeValues getEntityAttributeValues(String entityDN) throws ModelCommunicationException, ObjectNotFoundException{
		return getDirectoryObject(entityDN).getAttributes();
	}
	
	AttributeValues getEntityAsAttributeValues(Object entity) throws JsonGenerationException, JsonMappingException, IOException{
		AttributeValues avs = new AttributeValues();
		ObjectMapper mapper = new ObjectMapper();
		Map accountMap = mapper.convertValue(entity, Map.class);
		Iterator keys = accountMap.keySet().iterator();
		while(keys.hasNext()){
			String key = (String) keys.next();
			Object attribute = accountMap.get(key);
			if(attribute==null) continue;
			if(String.class.isInstance(attribute))
				avs.put(new AttributeValue(key,(String)attribute));
			else if(Collection.class.isInstance(attribute))
				avs.put(new AttributeValue(key,(Collection)attribute));
		}
		return avs;
	}
	
	Object getAttributeValuesAsEntity(AttributeValues avs) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map map = avs.getMap();
		HashMap entity = new HashMap();
		Iterator keys = map.keySet().iterator();
		while(keys.hasNext()){
			String key = (String) keys.next();
			AttributeValue attribute = (AttributeValue) map.get(key);
			if(spi.isMultiValuedAttribute(attribute.getName()))
				entity.put(key,attribute.getValues());
			else
				entity.put(key, attribute.getValueString());
		}
		entity.put("objectclass", (String)getObjectClass().iterator().next());
		return mapper.convertValue(entity, Object.class);
	}
	
	public Collection getObjectClass() {
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
	
	private static AttributeValues applyChanges(AttributeChanges changes,AttributeValues avs){
		for (AttributeChangeIterator it = changes.iterator(); it.hasNext();) {
			AttributeChangeOperation attributesChangeOp = it.next();
			if(attributesChangeOp.getAction().equalsIgnoreCase(AttributeChangeOperation.ACTION_ADD)){		
				for (
						AttributeValueIterator it2 = new AttributeValues(attributesChangeOp.getChangeData()).iterator();
						it2.hasNext();
					){
					AttributeValue attributeValueToRemove = it2.next();
					AttributeValue atual = avs.get(attributeValueToRemove.getName());
					if(atual==null){
						atual = new AttributeValue();
						atual.setName(attributeValueToRemove.getName());
						avs.put(atual);
					}
					atual.addValues(attributeValueToRemove.getValues());
				}
			}else if(attributesChangeOp.getAction().equalsIgnoreCase(AttributeChangeOperation.ACTION_REMOVE)){
				for (
						AttributeValueIterator it2 = new AttributeValues(attributesChangeOp.getChangeData()).iterator();
						it2.hasNext();
					){
					AttributeValue attributeValueToRemove = it2.next();
					avs.get(attributeValueToRemove.getName()).removeValues(attributeValueToRemove.getValues());
				}
					
			}else if(attributesChangeOp.getAction().equalsIgnoreCase(AttributeChangeOperation.ACTION_CHANGE)){
				for (
						AttributeValueIterator it2 = new AttributeValues(attributesChangeOp.getChangeData()).iterator();
						it2.hasNext();
					){
					AttributeValue attributeValueToRemove = it2.next();
					avs.get(attributeValueToRemove.getName()).setValues((attributeValueToRemove.getValues()));
				}
			}
		}
		return avs;
	}
}
