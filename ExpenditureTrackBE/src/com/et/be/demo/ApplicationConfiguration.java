package com.et.be.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.et.be.demo")
public class ApplicationConfiguration {

	/*
	 * Use the standard Mongo driver API to create a com.mongodb.MongoClient
	 * instance.
	 */
	public @Bean MongoClient mongoClient() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://ExtractUser:swCdVNfAozXEkNX3@gettingstarted-nioqq.mongodb.net");
		return new MongoClient(uri);
	}
	
     public @Bean MongoTemplate mongoTemplate() {
         return new MongoTemplate(mongoClient(),"extracttest");
     }
}
