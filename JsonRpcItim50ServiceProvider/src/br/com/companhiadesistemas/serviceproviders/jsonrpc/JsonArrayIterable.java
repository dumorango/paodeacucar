package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

public class JsonArrayIterable implements Iterable<String>{

	JsonArrayIterator iterator;
	JsonArrayIterable(JsonParser jp) throws JsonParseException, IOException{
		iterator = new JsonArrayIterator(jp);
	}
	public Iterator<String> iterator() {
		return iterator;
	}
	
	
}
