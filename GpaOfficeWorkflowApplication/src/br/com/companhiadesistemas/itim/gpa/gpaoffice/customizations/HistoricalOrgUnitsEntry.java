package br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;


@Entry(objectClasses = { "top","historicalgoogleappsaccount","eraccountitem","ermanageditem"})
public class HistoricalOrgUnitsEntry {
	
	@Id
	private Name distinguisedName;
	@Attribute(name="eruid")
	private String eruid;
	
	@Attribute(name="historicalorgunits")
	private List<String> historicalorgunits;

	@Attribute(name="objectClass")
	private List<String> objectClassNames;
	
	@Attribute(name="erAccountReference")
	private String erAccountReference;

	public String getErAccountReference() {
		return erAccountReference;
	}

	public void setErAccountReference(String erAccountReference) {
		this.erAccountReference = erAccountReference;
	}

	public HistoricalOrgUnitsEntry(){
		setObjectClassNames(new ArrayList<String>( Arrays.asList("top","historicalgoogleappsaccount","eraccountitem","ermanageditem")));
	}

	public Name getDistinguisedName() {
		return distinguisedName;
	}

	public String getEruid() {
		return eruid;
	}

	public List<String> getHistoricalorgunits() {
		return historicalorgunits;
	}

	public List<String> getObjectClassNames() {
		return objectClassNames;
	}
	
	public void setDistinguisedName(Name distinguisedName) {
		this.distinguisedName = distinguisedName;
	}
	
	public void setEruid(String eruid) {
		this.eruid = eruid;
	}

	public void setHistoricalorgunits(List<String> historicalorgunits) {
		this.historicalorgunits = historicalorgunits;
	}

	public void setObjectClassNames(List<String> objectClassNames) {
		this.objectClassNames = objectClassNames;
	}
	
}
