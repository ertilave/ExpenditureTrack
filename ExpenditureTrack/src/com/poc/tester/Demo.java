package com.poc.tester;

import java.util.*;

import com.et.extract.parser.Extract;
import com.et.extract.parser.ExtractFactory;
import com.et.extract.parser.PdfParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;

public class Demo {

	public static void main(String[] args) throws Exception {

        String pdfText = PdfParser.getPdfContent("d:\\b.pdf");
        /*
        for (String s:pdfText.split("\\n")) {
        	System.out.print(s);
        }
        */
        
		Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
        
        Extract extract = ExtractFactory.getExtract(props, pdfText);
        if (extract != null) {
        	extract.parseExtract(pdfText);
        } else throw new Exception("Bank extract could not be determined.");
        
        extract.printExtract();
                
        
        MongoClientURI uri = new MongoClientURI("mongodb+srv://ExtractUser:swCdVNfAozXEkNX3@gettingstarted-nioqq.mongodb.net/test");
        MongoClient mongoClient = new MongoClient(uri);
        //MongoDatabase database = mongoClient.getDatabase("test");
        
        System.out.println("Listing databases");
        
        MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
        while(dbsCursor.hasNext()) {
            System.out.println(dbsCursor.next());
        }
        
        mongoClient.close();
               
	}

}
