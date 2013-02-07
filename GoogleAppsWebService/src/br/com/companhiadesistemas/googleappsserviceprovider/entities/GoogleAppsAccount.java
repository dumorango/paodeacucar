package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.util.*;

@Deprecated
public class GoogleAppsAccount {

	public static final String USERNAME_ATTRIBUTE = "eruid";

	private ArrayList<String> aliases;
	private String dn;
	private ArrayList<String> ergroup;
	private String erlastaccessdate;
	private String erpassword;
	private String eruid;
	private String familyname;
	private String firstname;
	private String googlechangepasswordnextlogin;
	private String googleorgunit;
	private String objectClass = "erGoogleAppsAccount";
	private String quotalimit;
	private String eraccountstatus;
	
	public String getEraccountstatus() {
		return eraccountstatus;
	}

	public void setEraccountstatus(String eraccountstatus) {
		this.eraccountstatus = eraccountstatus;
	}

	public GoogleAppsAccount() {

	}
	
	public ArrayList<String> getAliases() {
		return aliases;
	}
	public String getDn() {
		return dn;
	}
	public ArrayList<String> getErgroup() {
		return ergroup;
	}

	public String getErlastaccessdate() {
		return erlastaccessdate;
	}

	public String getErpassword() {
		return erpassword;
	}

	public String getEruid() {
		return eruid;
	}

	public String getFamilyname() {
		return familyname;
	}

	public String getFirstname() {
		return firstname;
	}
	
	public String getGooglechangepasswordnextlogin() {
		return googlechangepasswordnextlogin;
	}

	public String getGoogleorgunit() {
		return googleorgunit;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public String getQuotalimit() {
		return quotalimit;
	}

	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public void setErgroup(ArrayList<String> ergroup) {
		this.ergroup = ergroup;
	}

	public void setErlastaccessdate(String erlastaccessdate) {
		this.erlastaccessdate = erlastaccessdate;
	}

	public void setErpassword(String erpassword) {
		this.erpassword = erpassword;
	}

	public void setEruid(String eruid) {
		this.eruid = eruid;
	}

	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}

	public void setFamilyName(String familyName) {
		this.familyname = familyName;
	}

	public void setFirstname(String givenName) {
		this.firstname = givenName;
	}

	public void setGooglechangepasswordnextlogin(
			String googlechangepasswordnextlogin) {
		this.googlechangepasswordnextlogin = googlechangepasswordnextlogin;
	}

	public void setGoogleorgunit(String googleorgunit) {
		this.googleorgunit = googleorgunit;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public void setPassword(String password) {
		this.erpassword = password;
	}

	public void setQuotalimit(String quotalimitinmb) {
		this.quotalimit = quotalimitinmb;
	}
}
