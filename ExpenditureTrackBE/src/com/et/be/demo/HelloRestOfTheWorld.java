package com.et.be.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.et.extract.parser.Extract;
import com.et.extract.parser.ExtractFactory;
import com.et.extract.parser.PdfParser;

@RestController
@RequestMapping(value = "/HelloRestOfTheWorld")
@MultipartConfig(fileSizeThreshold = 20971520)
public class HelloRestOfTheWorld {
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping(value = "/upload")
    public String uploadFile(@RequestParam("uploadedFile") MultipartFile uploadedFileRef) throws IOException{
    	// Get name of uploaded file.
    	// String fileName = uploadedFileRef.getOriginalFilename();
    	InputStream fileContent = uploadedFileRef.getInputStream();
    	System.out.println("Upload ok.");
    	
		Properties props = new Properties();        
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
			e.printStackTrace();
		}
        if (extract != null) {
        	extract.parseExtract(pdfText);
        } else System.out.println("extract is null."); //response.getWriter().append("Bank extract could not be determined.");
        
        //extract.printExtract();
    	return extract.toString();
    }
	
}
