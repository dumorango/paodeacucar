package br.com.companhiadesistemas.googleappsserviceprovider;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.*;

import com.googlecode.jsonrpc4j.*;

public class OAuthTokenServiceServlet extends HttpServlet {
	
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
        	     jsonRpcServer.setRethrowExceptions(false);
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
    		//this.jsonRpcServer = new JsonRpcServer(new EntityService(),EntityService.class);
    	this.jsonRpcServer = new JsonRpcServer(new OAuthTokenService(),OAuthTokenServiceInterface.class);
        }

}
