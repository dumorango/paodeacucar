package br.com.companhiadesistemas.gpaoffice.wkfapp;

import com.ibm.itim.dataservices.model.domain.*;
import com.ibm.itim.workflow.application.WorkflowApplication;
import com.ibm.itim.workflow.application.WorkflowExecutionContext;
import com.ibm.itim.workflow.engine.ExecutionContext;
import com.ibm.itim.workflow.engine.RetryException;
import com.ibm.itim.workflow.model.ActivityResult;
import com.ibm.itim.workflow.model.type.ProcessResult;
import com.ibm.itim.workflowextensions.AccountExtensions;
import com.ibm.itim.workflowextensions.WorkflowExtensionException;

public class GpaOfficeWorkflowApplication implements WorkflowApplication {

	public GpaOfficeWorkflowApplication() {
	}

	public void setContext(WorkflowExecutionContext ctx) {
		this.ctx = ctx;
	}

	public ActivityResult customModify(Person person, Service service,
			Account account) throws RetryException, WorkflowExtensionException {
		AccountExtensions ae = new AccountExtensions((ExecutionContext) ctx);
		ProcessResult pr = ae.modifyAccount(person, service, account);
		return new ActivityResult(pr.getStatus(), pr.getSummary(),
				pr.getDescription(), pr.getDetail());
	}

	protected WorkflowExecutionContext ctx;
}
