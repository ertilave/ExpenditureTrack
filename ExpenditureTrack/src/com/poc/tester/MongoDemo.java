package com.poc.tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class MongoDemo {

	public static void main(String[] args) {
		System.setProperty("DEBUG.MONGO", "false");
		System.setProperty("DB.TRACE", "false");
		
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
		rootLogger.setLevel(Level.OFF);
		
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://ExtractUser:swCdVNfAozXEkNX3@gettingstarted-nioqq.mongodb.net");
		MongoClient mongoClient = new MongoClient(uri);
		
		MongoDatabase database = mongoClient.getDatabase("extracttest");
		MongoCollection<Document> collection = database.getCollection("demotable");
		
		Document doc = new Document("name", "MongoDB")
	                .append("type", "database")
	                .append("count", 1)
	                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
	                .append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);
		
		System.out.println("Listing databases");

		MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
		while (dbsCursor.hasNext()) {
			System.out.println(dbsCursor.next());
		}
		
		System.out.println("Listing collections at extracttest");
		
		MongoIterable<String> collections = database.listCollectionNames();
        for (String collectionName: collections) {
            System.out.println(collectionName);
        }
        
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
        
        System.out.println("Count of:"+collection.count());
        
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        
        myDoc = collection.find(Filters.eq("name", "MongoDB")).first();
        System.out.println(myDoc.toJson());
        
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
       };

       collection.find(Filters.gt("i", 50)).forEach(printBlock);
		mongoClient.close();

	}

}
