package br.com.companhiadesistemas.itimcustomreports;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;
import br.com.companhiadesistemas.itimcustomreports.entities.ReportSIService;

import com.googlecode.jsonrpc4j.JsonRpcServer;

public class ReportSIServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JsonRpcServer jsonRpcServer;
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    	this.doPost(req, resp);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        	try {
        	     this.jsonRpcServer.setAllowExtraParams(true);
        	     jsonRpcServer.setAllowLessParams(true);
        	     jsonRpcServer.setRethrowExceptions(true);
				 jsonRpcServer.handle(req, resp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
	public String getQueryString(HttpServletRequest request) throws IOException{
		if(request.getMethod().equalsIgnoreCase("GET")){
			return request.getQueryString();
		}else if(request.getMethod().equalsIgnoreCase("POST")){
			return request.getReader().readLine();
		}
		return null;
	}

    public void init(ServletConfig config) {
       		this.jsonRpcServer = new JsonRpcServer(new ReportSIService(),ReportService.class);
        }

}
