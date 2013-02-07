package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.util.common.xml.XmlWriter;

public enum GoogleAccountSerializerEnum {
	ERUID,ERPASSWORD,ERACCOUNTSTATUS,GOOGLECHANGEPASSWORDNEXTLOGIN,FIRSTNAME,FAMILYNAME,QUOTALIMIT,ERGROUP,ALIASES,GOOGLEORGUNIT,DN,GOOGLEPROFILE;
	
	@SuppressWarnings("deprecation")
	public Object map(GoogleAccount account){
		switch(this){
			case ERUID:
				return account.getUserEntry().getProperty("userEmail");
			case ERPASSWORD:
				return account.getUserEntry().getProperty("password");
			case ERACCOUNTSTATUS:
				return (Boolean.valueOf(account.getUserEntry().getProperty("isSuspended")))?"1":"0";
			case GOOGLECHANGEPASSWORDNEXTLOGIN:
				return String.valueOf(account.getUserEntry().getProperty("isChangePasswordAtNextLogin"));
			case FIRSTNAME:
				return account.getUserEntry().getProperty("firstName");
			case FAMILYNAME:
				return account.getUserEntry().getProperty("lastName");
			case QUOTALIMIT:
				return account.getUserEntry().getProperty("quotaInGb");
			case ERGROUP:
				return new GenericEntryIterable(account.getGroups(),"groupId");
			case ALIASES:
				return new GenericEntryIterable(account.getNicknames(),"aliasEmail");
			case GOOGLEORGUNIT:
				String orgUnitPath = account.getOrgUnit().getProperty("orgUnitPath");
				return (orgUnitPath==null)?"":URLDecoder.decode(orgUnitPath);
			case GOOGLEPROFILE:
				HashMap profileMap = new HashMap();
				//profileMap.put("fullname", account.getProfile().getName().getFullName().getValue());
				return profileMap;
			case DN:
				return "eruid="+account.getUserEntry().getProperty("userEmail");
			default:
				return null;
				
		}
	}
	
	class GenericEntryIterable implements Iterable<String>{
		
		GenericEntryIterator iterator;
		GenericEntryIterable(List<GenericEntry> genericEntryList,String attName){
			iterator = new GenericEntryIterator(genericEntryList,attName);
		}
		public Iterator<String> iterator() {
			return iterator;
		}
		
	}
	
	class GenericEntryIterator implements Iterator<String>{
		
		Iterator<GenericEntry> genericEntryList;
		String attName;
		
		GenericEntryIterator(List<GenericEntry> genericEntryList,String attName){
			this.genericEntryList = genericEntryList.iterator();
			this.attName = attName;
		}
		public boolean hasNext() {
			return genericEntryList.hasNext();
		}

		public String next() {
			return genericEntryList.next().getProperty(attName);
		}

		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
