package com.et.extract.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfParser {
	
	public static String getPdfContent(InputStream file) throws Exception{
		Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
		
		//System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		PDDocument document = null;
		
		document = PDDocument.load(file);
		AccessPermission ap = document.getCurrentAccessPermission();
        if( ! ap.canExtractContent() )
        {
            throw new IOException( "You do not have permission to extract text" );
        }
		
        PDFTextStripper stripper;
        stripper = new PDFTextStripper();
        
        boolean sort = true;
        boolean separateBeads = true;
        int startPage = 1;
        int endPage = document.getNumberOfPages()-1;
        
        stripper.setSortByPosition( sort );
        stripper.setShouldSeparateByBeads( separateBeads );
        stripper.setStartPage( startPage );
        stripper.setEndPage( endPage );
        
        String txt = stripper.getText(document);
        IOUtils.closeQuietly(document);	
        
        return txt;
	}
	
	public static String getPdfContent(String fileName) throws Exception{
		
		InputStream input = new FileInputStream(new File(fileName));
		return getPdfContent(input);
	}
	
}
