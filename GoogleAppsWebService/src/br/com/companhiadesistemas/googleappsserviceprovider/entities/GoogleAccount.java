package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.util.*;


import br.com.companhiadesistemas.googleappsserviceprovider.GoogleAppsAdapter;
import br.com.companhiadesistemas.serviceproviders.integration.ItimEntity;
import br.com.companhiadesistemas.serviceproviders.jsonrpc.*;

import com.fasterxml.jackson.databind.annotation.*;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.contacts.ContactEntry;


@JsonSerialize(using=JsonRpcEntitySerializer.class)
@JsonDeserialize(using=JsonRpcEntityDeserializer.class)
@ItimEntity(objectClass="erGoogleAppsAccount"
,deserializerEnum = GoogleAccountDeserializerEnum.class
, serializerEnum = GoogleAccountSerializerEnum.class
, adapterClass = GoogleAppsAdapter.class)
public class GoogleAccount {
	
	private GenericEntry userEntry;
	private List<GenericEntry> nicknames;
	private List<GenericEntry> groups;
	private GenericEntry orgUnit;
	private ContactEntry profile;

	
	public GoogleAccount(){
		this.userEntry = new GenericEntry();
		this.nicknames = new ArrayList<GenericEntry>();
		this.orgUnit = new GenericEntry();
		this.groups = new ArrayList<GenericEntry>();
	}
	public List<GenericEntry> getGroups() {
		return groups;
	}

	public void setGroups(List<GenericEntry> groups) {
		this.groups = groups;
	}

	public GenericEntry getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(GenericEntry orgUnit) {
		this.orgUnit = orgUnit;
	}

	public List<GenericEntry> getNicknames() {
		return nicknames;
	}

	public void setNicknames(List<GenericEntry> nicknames) {
		this.nicknames = nicknames;
	}

	public GenericEntry getUserEntry() {
		return userEntry;
	}

	public void setUserEntry(GenericEntry userEntry) {
		this.userEntry = userEntry;
	}
	
	public ContactEntry getProfile() {
		return profile;
	}

	public void setProfile(ContactEntry contactEntry) {
		this.profile = contactEntry;
	}
}
