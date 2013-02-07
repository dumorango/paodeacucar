package br.com.companhiadesistemas.googleappsserviceprovider.crosscutting;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
	@Pointcut("execution(* br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.*.*(..))") // expression 
	public static void authMethods() {}
	
	@Pointcut(
			"execution(public * br.com.companhiadesistemas.googleappsserviceprovider.googleapis.*.*(..))"
			//+ "&& ! execution(* br.com.companhiadesistemas.googleappsserviceprovider.googleapis.*.toString(..))"
			)
	public static void webServiceMethods() {}
	
}
