package br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.*;


@Entry(objectClasses = { "top","historicalgoogleappsaccount","eraccountitem","ermanageditem"})
public class HistoricalOrgUnitsEntry {
	
	public HistoricalOrgUnitsEntry(){
		setObjectClassNames(new ArrayList<String>( Arrays.asList("top","historicalgoogleappsaccount","eraccountitem","ermanageditem")));
	}
	@Id
	private Name distinguisedName;
	
	public Name getDistinguisedName() {
		return distinguisedName;
	}

	public void setDistinguisedName(Name distinguisedName) {
		this.distinguisedName = distinguisedName;
	}

	public String getEruid() {
		return eruid;
	}

	public void setEruid(String eruid) {
		this.eruid = eruid;
	}

	public List<String> getHistoricalorgunits() {
		return historicalorgunits;
	}

	public void setHistoricalorgunits(List<String> historicalorgunits) {
		this.historicalorgunits = historicalorgunits;
	}

	@Attribute(name="eruid")
	private String eruid;
	
	@Attribute(name="historicalorgunits")
	private List<String> historicalorgunits;
	
	@Attribute(name="objectClass")
	private List<String> objectClassNames;

	public List<String> getObjectClassNames() {
		return objectClassNames;
	}

	public void setObjectClassNames(List<String> objectClassNames) {
		this.objectClassNames = objectClassNames;
	}
	
}
