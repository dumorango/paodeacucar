package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;


public class CustomArraySerializer extends JsonSerializer<ArrayList<?>>{

	@Override
	public void serialize(ArrayList<?> arrayList, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartArray();
		for(Object obj:arrayList)
			jgen.writeObject(obj);
		jgen.writeEndArray();
	}



}
