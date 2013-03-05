package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class JsonArrayIterable implements Iterable<String>{

	JsonArrayIterator iterator;
	JsonArrayIterable(JsonParser jp) throws JsonParseException, IOException{
		iterator = new JsonArrayIterator(jp);
	}
	public Iterator<String> iterator() {
		return iterator;
	}
	
	
}
