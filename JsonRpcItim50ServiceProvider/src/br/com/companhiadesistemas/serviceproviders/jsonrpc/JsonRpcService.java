package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.util.*;

import br.com.companhiadesistemas.serviceproviders.integration.IntegrationService;

@SuppressWarnings("rawtypes")
public class JsonRpcService extends IntegrationService {

	public boolean connect( Map connectionProperties) throws Exception {
		return false;
	}

	public Object add(Map connectionProperties, Object account)
			throws Exception {
		return null;
	}

	public Object modify(Map connectionProperties, Object oldAccount,
			Object newAccount) throws Exception {

		return null;
	}

	public Object changePassword(Map connectionProperties, Object account,
			byte[] newPassword) throws Exception {
		return null;
	}

	public Object suspend(Map connectionProperties, Object account)
			throws Exception {
		return null;
	}

	public Object restore(Map connectionProperties, Object account)
			throws Exception {
		return null;
	}

	public Object delete(Map connectionProperties, Object account)
			throws Exception {
		return null;
	}

	public ArrayList<?> search(Map connectionProperties) throws Exception {
		return null;
	}

	public boolean test(Map connectionProperties,
			Collection<String> objectClasses) throws Exception {
		return false;
	}

	public ArrayList<?> search(Map connectionProperties,
			Collection<String> objectClasses) throws Exception {
		return null;
	}
	
	
}
