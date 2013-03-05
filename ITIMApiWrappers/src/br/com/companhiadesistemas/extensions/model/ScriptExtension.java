package br.com.companhiadesistemas.extensions.model;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptExtension {
	public String constructor();
}

