package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.*;

public class JsonArrayIterator implements Iterator<String>{
	
	JsonParser jp;
	JsonToken currentToken;
	
	JsonArrayIterator(JsonParser jp) throws JsonParseException, IOException{
		this.jp = jp;
		this.currentToken = jp.nextToken();
	}
	public boolean hasNext() {
		return currentToken!=JsonToken.END_ARRAY;
	}

	public String next() {
		try {
			if(hasNext()){
				String next = jp.getText();
				currentToken = jp.nextToken();
				return next;
			}
			return null;
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void remove() {
	
	}

}
