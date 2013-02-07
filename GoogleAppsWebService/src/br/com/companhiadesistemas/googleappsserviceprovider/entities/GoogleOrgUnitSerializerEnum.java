package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.net.URLDecoder;

public enum GoogleOrgUnitSerializerEnum {
	GOOGLEORGUNITPATH,GOOGLEORGUNITNAME,DESCRIPTION,GOOGLEORGUNITINHERITANCE,DN,objectClass;
	@SuppressWarnings("deprecation")
	public Object map(GoogleOrgUnit orgUnit){
		switch(this){
		case GOOGLEORGUNITPATH:
			return getUid(orgUnit);
		case DN:
			return "googleorgunitpath="+getUid(orgUnit);
		case objectClass:
			return "ergoogleappsorgunit";
		case GOOGLEORGUNITNAME:
			return orgUnit.get("name");
		case DESCRIPTION:
			return orgUnit.get("description");
		default:
			return null;
		}
	}
	
	private String getUid(GoogleOrgUnit orgUnit){
		String org = orgUnit.get("orgUnitPath");
		if(org!=null)
			return URLDecoder.decode(org);
		return null;
	}
}
