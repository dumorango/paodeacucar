package br.com.companhiadesistemas.serviceproviders.integration;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import br.com.companhiadesistemas.serviceproviders.jsonrpc.JsonRpcClient;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;

@SuppressWarnings("rawtypes")
public enum IntegrationLayersEnum {
	
	LOCAL,JSONRPC;
	
	IntegrationInterface getIntegrationLayer(Map connectionProperties) throws JsonParseException, JsonMappingException, IOException{
		switch(this){
			case LOCAL:
				//TODO
				return null;
			case JSONRPC:
				return new JsonRpcClient(connectionProperties);
		}
		return null;
	}
	
	public static IntegrationInterface getIntegrationLayerByProperties(Map connectionProperties) throws Exception{
		
		HashMap<String,Object> integrationConfig;
		try{
			integrationConfig = getIntegrationConfig(connectionProperties);
			return IntegrationLayersEnum.valueOf(((String) integrationConfig.get("integrationLayer")).toUpperCase()).getIntegrationLayer(connectionProperties);
		}catch(Exception ex){
			throw new Exception("Houve um erro ao tentar recuperar as informações sobre a camada de integração no atributo "+integrationlayerproperty);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String,Object> getIntegrationConfig(Map<?,?> connectionProperties) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Logging.LOGGER.info("connectionProperties: "+connectionProperties);
		return mapper.readValue((String)connectionProperties.get(integrationlayerproperty),HashMap.class);
	}
	
	final static String integrationlayerproperty = "erwebservicehost";
}
