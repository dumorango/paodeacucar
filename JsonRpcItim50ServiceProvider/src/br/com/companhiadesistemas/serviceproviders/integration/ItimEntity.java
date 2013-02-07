package br.com.companhiadesistemas.serviceproviders.integration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItimEntity {
	public String objectClass();
	public Class<?> serializerEnum();
	public Class<?> deserializerEnum();
	public Class<?> adapterClass();
}
