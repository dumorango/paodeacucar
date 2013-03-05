package br.com.companhiadesistemas.itim.api.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ibm.itim.common.AttributeValue;
import com.ibm.itim.common.AttributeValues;
import com.ibm.itim.dataservices.model.DirectoryObjectEntity;
import com.ibm.itim.dataservices.model.domain.PersonEntity;

public class EntitiesUtil {

	public static PersonEntity getSupervisor(PersonEntity person){
		try{
			return EntitySearch.lookUpPerson(person.getDirectoryObject().getAttributes().get("ersupervisor").getValueString());
		}catch(Exception ex){
			return null;
		}
	}
	
	public static String getAttributeValueString(DirectoryObjectEntity entity,String attName){
		try{
			
			return getAttribute(entity,attName).getValueString();
		}catch(NullPointerException ex){
		}
		return null;
	}
	
	public static String getAttributeValuesAsJSON(AttributeValues avs) throws JsonGenerationException, JsonMappingException, IOException{
		//System.out.println("getAttributeValuesAsJSON: avs: "+avs);
		ObjectMapper mapper = new ObjectMapper();
		Map map = avs.getMap();
		HashMap entity = new HashMap();
		Iterator keys = map.keySet().iterator();
		while(keys.hasNext()){
			String key = (String) keys.next();
			AttributeValue attribute = (AttributeValue) map.get(key);
			entity.put(key,attribute.getValues());
		}
		//System.out.println("getAttributeValuesAsJSON - entity: "+entity);
		return mapper.writeValueAsString(entity);
	}
	
	public static AttributeValue getAttribute(DirectoryObjectEntity entity,String attName){
		try{
			return entity.getDirectoryObject().getAttributes().get(attName);
		}catch(NullPointerException ex){
			return null;
		}
	}
	
	public boolean isActive(PersonEntity pe){
		try{
			return getAttributeValueString(pe,"erpersonstatus").equalsIgnoreCase("0");
		}catch(NullPointerException ex){
			return false;
		}
		
	}
	
	public String getMail(PersonEntity person){
		try{
			return ((DirectoryObjectEntity) EntitySearch.getAccountsByServiceNameAndOwner("Rede 2008", person).toCollection().iterator().next())
			.getDirectoryObject().getAttribute("mail").getValueString();
		}catch(Exception ex){
			return null;
		}
	}
	
	public boolean isTerceiro(PersonEntity person){
		return getAttribute(person,"objectclass").isValueExist("gpaterceiros");
	}
	
	public String getFuncao(PersonEntity person){
		return (isTerceiro(person)?getAttributeValueString(person,"gpafuncao"):getAttributeValueString(person,"gpajobdesc"));
	}
	
	public String getDepartamento(PersonEntity person){
		return (isTerceiro(person)?getAttributeValueString(person,"gpaempresa"):getAttributeValueString(person,"gpadeptdesc"));
	}
	
	public String getCentroDeCusto(PersonEntity person){
		return (isTerceiro(person)?getAttributeValueString(person,"gpaarea"):getAttributeValueString(person,"gpacbd-ccustoaloc"));
	}
	
	public String toIso8859_1(String stringToConvert){
		return StringUtils.newStringIso8859_1(StringUtils.getBytesUtf8(
				stringToConvert
			));
	}
}
