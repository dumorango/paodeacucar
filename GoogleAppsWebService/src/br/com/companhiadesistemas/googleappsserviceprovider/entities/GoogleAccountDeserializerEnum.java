package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.net.URLEncoder;
import java.util.ArrayList;


import br.com.companhiadesistemas.serviceproviders.logging.Logging;

import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;


public enum GoogleAccountDeserializerEnum{
	ERUID,ERPASSWORD,ERACCOUNTSTATUS,GOOGLECHANGEPASSWORDNEXTLOGIN,FIRSTNAME,FAMILYNAME,QUOTALIMIT,ERGROUP,ALIASES,GOOGLEORGUNIT;
	
	GenericEntry user;
	GoogleAccountDeserializerEnum(){
		
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public GoogleAccount deserialize(GoogleAccount googleaccount,Object value){
		GenericEntry user = googleaccount.getUserEntry();
		switch(this){
			case ERUID:
				setUserProperty("userEmail",value,user);
				break;
			case ERPASSWORD:
				setUserProperty("password",value,user);
				break;
			case ERACCOUNTSTATUS:
				setUserProperty("isSuspended",String.valueOf(!((String) value).equalsIgnoreCase("0")),user);
				break;
			case GOOGLECHANGEPASSWORDNEXTLOGIN:
				setUserProperty("isChangePasswordAtNextLogin",value,user);
				break;
			case FIRSTNAME:
				setUserProperty("firstName",value,user);
				break;
			case FAMILYNAME:
				setUserProperty("lastName",value,user);
				break;
			case QUOTALIMIT:
				setUserProperty("quotaInGb",value,user);
				break;
			case ERGROUP :
				ArrayList<GenericEntry> groups = new ArrayList<GenericEntry>();
				for(String groupid:(Iterable<String>) value){
					GenericEntry group = new GenericEntry();
					group.addProperty("groupId", groupid);
					groups.add(group);
				}
				googleaccount.setGroups(groups);
				break;
			case ALIASES:
				ArrayList<GenericEntry> aliases = new ArrayList<GenericEntry>();
				for(String groupid:(Iterable<String>) value){
					GenericEntry alias = new GenericEntry();
					alias.addProperty("aliasEmail", groupid);
					aliases.add(alias);
				}
				googleaccount.setNicknames(aliases);
				break;
			case GOOGLEORGUNIT:
				GenericEntry org = new GenericEntry();
				org.addProperty("orgUnitPath",URLEncoder.encode((String) value));
				googleaccount.setOrgUnit(org);
				break;
			default:
		}
		googleaccount.setUserEntry(user);
		return googleaccount;
		
	}
	
	private void setUserProperty(String attName,Object value,GenericEntry user){
		user.addProperty(attName,(String) value);
	}
	
}
