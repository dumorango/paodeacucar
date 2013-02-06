<%@  page contentType="application/octet-stream"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.net.*,java.util.*,javax.servlet.* "%>
<%@ page import=" org.json.*" %>
<%@ page import=" java.io.FileWriter" %>
<%@ page import=" java.io.File" %>
<%@ page import=" java.io.IOException" %>
<%@ page import=" com.csvreader.CsvWriter" %>
<%@ page import=" java.net.URLDecoder"%>

    
 <%!
 public void writeCSVByArray(JSONArray array,String file,boolean append) throws JSONException,IOException{
	 CsvWriter csvOutput = new CsvWriter(new FileWriter(file, append), ';');
	 File f = new File(file);
	 System.out.println("File path: "+f.getAbsolutePath());
	 for(int i=0;i<array.length();i++){
		 JSONArray linha = (JSONArray)array.get(i);
		 for(int j=0;j<linha.length();j++){
			 JSONArray elementArray = linha.optJSONArray(j);
			 if(elementArray==null){
			 	csvOutput.write(linha.optString(j, "\"\"").replaceAll("\r\n"," ").replaceAll("\n", " ").replaceAll("null",""));
			 } else{
				 String elementStr = "";
				 for(int k=0;k<elementArray.length();k++)
				 	elementStr += elementArray.optString(k)+(k<elementArray.length()-1?",":"");
				 csvOutput.write(elementStr);
			 }	
		 }
		 csvOutput.endRecord();
	 }
	 csvOutput.close();
 }
 
public String getQueryString(HttpServletRequest request) throws IOException{
	if(request.getMethod().equalsIgnoreCase("GET")){
		return request.getQueryString();
	}else if(request.getMethod().equalsIgnoreCase("POST")){
		return request.getReader().readLine();
	}
	return null;
}
 %>   
    
<%
String queryString = getQueryString(request);
System.out.println("queryString: "+queryString);
queryString = URLDecoder.decode(queryString,"UTF-8");
System.out.println("queryString: "+queryString);
JSONObject jsonrpc = new JSONObject(queryString);
String filename = jsonrpc.getString("file");
System.out.println("file: "+filename);
JSONArray array = jsonrpc.getJSONArray("array");
System.out.println("array: "+array);
writeCSVByArray(array,filename,jsonrpc.optString("append").equalsIgnoreCase("true"));


%>
