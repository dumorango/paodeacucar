<%@page contentType="application/octet-stream"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" import="java.io.*,java.net.*,java.util.*,javax.servlet.* "%>

    
<%
BufferedInputStream filein = null;
BufferedOutputStream outputs = null;
String filename = request.getParameter("file");
try {
	File file = new File(filename);//specify the file path
	byte b[] = new byte[2048];
	int len = 0;
	filein = new BufferedInputStream(new FileInputStream(file));
	outputs = new BufferedOutputStream(response.getOutputStream());
	response.setHeader("Content-Length", ""+file.length());
	response.setContentType("application/force-download");
	response.setHeader("Content-Disposition","attachment;filename="+filename);
	response.setHeader("Content-Transfer-Encoding", "binary");
	while ((len = filein.read(b)) > 0) {
		outputs.write(b, 0, len);
		outputs.flush();
	}
	file.delete();
}
catch(Exception e){
out.println(e);
}

%>
