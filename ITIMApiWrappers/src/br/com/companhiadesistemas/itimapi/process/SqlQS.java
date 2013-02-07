package br.com.companhiadesistemas.itimapi.process;

import com.ibm.itim.workflow.query.WorkflowQueryStatement;

class SqlQS implements WorkflowQueryStatement{
	String sql;
	public SqlQS(String sql){
		this.sql = sql;
	}
	public String getStatement(){
		return sql;
	}
}