package br.com.companhiadesistemas.itimcustomreports;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.utils.oracleretailreports.GenerateLists;


/**
 * Servlet implementation class OracleRetailReportsServlet
 */
public class OracleRetailReportsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper;

    /**
     * Default constructor. 
     */
    public OracleRetailReportsServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.getOutputStream().print(mapper.writeValueAsString(GenerateLists.getCodeAndDescriptionList(
					request.getParameter("type")// RDF,AIP Mercadologica,Operacional MFP Mercadologica,OperacionalAC51 IAP Mercadologica
					,request.getParameter("siglaService") 
					,request.getParameter("nivelSeguranca")
					)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		mapper = new ObjectMapper();
		super.init(config);
	}
}
