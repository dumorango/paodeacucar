package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.util.HashMap;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import br.com.companhiadesistemas.googleappsserviceprovider.GoogleAppsAdapter;
import br.com.companhiadesistemas.serviceproviders.integration.ItimEntity;
import br.com.companhiadesistemas.serviceproviders.jsonrpc.JsonRpcEntityDeserializer;
import br.com.companhiadesistemas.serviceproviders.jsonrpc.JsonRpcEntitySerializer;

import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;

@JsonSerialize(using=JsonRpcEntitySerializer.class)
@JsonDeserialize(using=JsonRpcEntityDeserializer.class)
@ItimEntity(objectClass="erGoogleAppsOrgUnit"
,deserializerEnum = GoogleOrgUnitDeserializerEnum.class
, adapterClass = GoogleAppsAdapter.class, serializerEnum = GoogleOrgUnitSerializerEnum.class)
@SuppressWarnings("serial")
public class GoogleOrgUnit extends HashMap<String,String>{
	public GoogleOrgUnit(GenericEntry org){
		super(org.getAllProperties());
	}
}
