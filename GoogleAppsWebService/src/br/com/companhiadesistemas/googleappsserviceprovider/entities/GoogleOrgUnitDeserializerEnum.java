package br.com.companhiadesistemas.googleappsserviceprovider.entities;

public enum GoogleOrgUnitDeserializerEnum {
	DN,GOOGLEORGUNIT;
	@SuppressWarnings("deprecation")
	public Object map(GoogleOrgUnit orgUnit){
		switch(this){
		case GOOGLEORGUNIT:
			return orgUnit.get("googleorgunit");
		default:
			return null;
		}
	}
}
