package br.com.companhiadesistemas.serviceproviders.integration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

public class IntegrationPointcuts {
	@Pointcut("execution( * br.com.companhiadesistemas.serviceproviders.integration.IntegrationService.*(..))") // expression 
	public static void receivedRequests() {} 
	
	@Pointcut("( receivedRequests()" +
			"&& ! execution(public * br.com.companhiadesistemas.serviceproviders.integration.IntegrationService.connect(..))" +
			"&& ! execution(public * br.com.companhiadesistemas.serviceproviders.integration.IntegrationService.search(..)) )")
	public static void entitiesReceivedRequests(){}
	
	@Pointcut(
			"execution(* br.com.companhiadesistemas.serviceproviders.integration.IntegrationInterface.search())")
	public static void adapterSearch() {}
	
	@Pointcut(
			"execution(* br.com.companhiadesistemas.serviceproviders.integration.IntegrationAdapter.*(..))")
	public static void adapterMethods() {}
	
	
}
