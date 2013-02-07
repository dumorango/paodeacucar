package br.com.companhiadesistemas.itimapi.process;

import java.util.*;

import com.ibm.itim.workflow.model.*;
import com.ibm.itim.workflow.query.*;

public class ProcessSearch {
	public static List<WorkflowProcess> getProcessesBySQL(String sql) throws InvalidQueryException, WorkflowException{
		WorkflowProcessQuery wpq = new WorkflowProcessQuery();
		@SuppressWarnings("unchecked")
		List<WorkflowProcess> l = wpq.execute(new SqlQS(sql));
		return l;
	}
	
	public static List<WorkflowProcess> getProcessByServiceNameAndCompletedDate(String serviceName,String startDate,String endDate) throws InvalidQueryException, WorkflowException{
		String sql = "select * from process where" +
				" SUBJECT_SERVICE='"+serviceName+"'"
				+" and COMPLETED>='"+startDate+"'"
				+" and COMPLETED<='"+endDate+"'";
		return getProcessesBySQL(sql);
	}
	
}
