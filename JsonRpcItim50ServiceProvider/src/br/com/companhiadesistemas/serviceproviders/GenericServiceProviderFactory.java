package br.com.companhiadesistemas.serviceproviders;

import com.ibm.itim.remoteservices.provider.*;

public class GenericServiceProviderFactory implements ServiceProviderFactory{
	
	
	public ServiceProvider getServiceProvider(ServiceProviderInformation spi)
			throws ProviderConfigurationException {
		ServiceProvider sp = null;
		try {
			sp = new GenericProvider(spi);
		} catch (Exception e) {
			System.out.println("Houve um erro ao instaciar o GenericProvider\n\tErro: "+e.getMessage());
			e.printStackTrace();
		}
		return sp;
	}
	
	
}
