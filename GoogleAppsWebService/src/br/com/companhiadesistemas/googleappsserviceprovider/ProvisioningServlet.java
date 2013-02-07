package br.com.companhiadesistemas.googleappsserviceprovider;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import br.com.companhiadesistemas.serviceproviders.integration.IntegrationInterface;
import br.com.companhiadesistemas.serviceproviders.jsonrpc.JsonRpcService;

import com.googlecode.jsonrpc4j.*;

public class ProvisioningServlet extends HttpServlet {
	
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
        		 //String queryString = getQueryString(req);
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
    	//this.jsonRpcServer = new JsonRpcServer(new GoogleAppsWrapperService(),JsonRpcIntegrationInterface.class);
    	this.jsonRpcServer = new JsonRpcServer(new JsonRpcService(),IntegrationInterface.class);
     }

}
