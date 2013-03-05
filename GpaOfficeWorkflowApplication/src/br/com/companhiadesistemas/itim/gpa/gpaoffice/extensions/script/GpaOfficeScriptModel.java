package br.com.companhiadesistemas.itim.gpa.gpaoffice.extensions.script;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.ibm.itim.script.ContextItem;
import com.ibm.itim.script.ScriptContextDAO;
import com.ibm.itim.script.ScriptException;
import com.ibm.itim.script.ScriptExtension;
import com.ibm.itim.script.ScriptInterface;


public class GpaOfficeScriptModel implements ScriptExtension{
	
	
	private List allBeans;
	
	public List getContextItems() {
		return allBeans;
	}

	public void initialize(ScriptInterface arg0, ScriptContextDAO arg1)
			throws ScriptException, IllegalArgumentException {
		allBeans = new ArrayList(0);
		ContextItem bean = ContextItem.createConstructor("GpaOffice",new GpaOfficeScriptBean().getClass());
		allBeans.add(bean);
	}

}
