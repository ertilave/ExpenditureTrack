package com.poc.tester;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.et.extract.parser.Extract;
import com.et.extract.parser.ExtractFactory;
import com.et.extract.parser.PdfParser;

/**
 * Servlet implementation class PdfDemoServlet
 */
@WebServlet("/PdfDemoServlet")
@MultipartConfig
public class PdfDemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public PdfDemoServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    InputStream fileContent = filePart.getInputStream();
		
		InputStream input = getServletContext().getResourceAsStream("/WEB-INF/app.properties");
		Properties props = new Properties();
        props.load(input);
        
        String pdfText="";
		try {
			pdfText = PdfParser.getPdfContent(fileContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Extract extract = ExtractFactory.getExtract(props, pdfText);
        if (extract != null) {
        	extract.parseExtract(pdfText);
        } else response.getWriter().append("Bank extract could not be determined.").append(request.getContextPath());
        
        extract.printExtract();
        doGet(request, response);
	}

}
