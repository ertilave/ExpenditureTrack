package com.poc.tester;

import java.io.IOException;
import java.io.InputStream;
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
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;

@WebServlet("/PdfDemoServlet")
@MultipartConfig
public class PdfDemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public PdfDemoServlet() {
    }

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MongoClientURI uri = new MongoClientURI("mongodb+srv://ExtractUser:swCdVNfAozXEkNX3@gettingstarted-nioqq.mongodb.net");
        MongoClient mongoClient = new MongoClient(uri);
        //MongoDatabase database = mongoClient.getDatabase("test");
        
        response.getWriter().append("Listing databases<br/>");
        
        MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
        while(dbsCursor.hasNext()) {
        	response.getWriter().append(dbsCursor.next()+"\\\\r\\\\n");
        }
        
        mongoClient.close();
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    InputStream fileContent = filePart.getInputStream();
		
		//InputStream input = getServletContext().getResourceAsStream("/WEB-INF/app.properties");
		Properties props = new Properties();
        //props.load(input);
        
        props.put("1_Extract_Keyword", "Adios Premium ödemenizi Yapý Kredi ATM'lerinde barkodu");
        props.put("1_Extract_Class", "com.et.extract.parser.YkbExtract");
        
        String pdfText="";
		try {
			pdfText = PdfParser.getPdfContent(fileContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Extract extract = null;
		try {
			extract = ExtractFactory.getExtract(props, pdfText);
		} catch (Exception e) {
			throw new ServletException(e);
		}
        if (extract != null) {
        	extract.parseExtract(pdfText);
        } else System.out.println("extract is null."); //response.getWriter().append("Bank extract could not be determined.");
        
        extract.printExtract();
	}

}
