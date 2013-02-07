package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import com.google.gdata.data.appsforyourdomain.generic.*;

@Deprecated
public class GoogleAppsGroup{
		
	public String getErgroupid() {
		return ergroupid;
	}

	public void setErgroupId(String groupId) {
		this.ergroupid = groupId;
	}

	public String getErgroupname() {
		return ergroupname;
	}

	public void setErgroupname(String groupName) {
		this.ergroupname = groupName;
	}

	public String getErgroupdescription() {
		return ergroupdescription;
	}

	public void setErgroupdescription(String groupDescription) {
		this.ergroupdescription = groupDescription;
	}

	public String getEmailPermission() {
		return emailPermission;
	}

	public void setEmailPermission(String emailPermission) {
		this.emailPermission = emailPermission;
	}
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}
	
	public String getObjectclass() {
		return objectclass;
	}

	public void setErgroupid(String ergroupid) {
		this.ergroupid = ergroupid;
	}

	private String ergroupid;
	private String ergroupname;
	private String ergroupdescription;
	private String emailPermission;
	private String dn;
	private final String objectclass = "ergoogleappsgroup";
	
	static final String UID_ATTRIBUTE = "ergroupid"; 
	
	public GoogleAppsGroup(){
		
	}
	
	public GoogleAppsGroup(GenericEntry e){
		System.out.println("Properties: "+e.getAllProperties());
		ergroupid = e.getProperty("groupId").split("@")[0];
		ergroupname = e.getProperty("groupName");
		dn = UID_ATTRIBUTE+"="+ergroupid;
	}
	
	

}
