package org.lab.webcrawler.dao.test;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.RentProperty;

import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;

public class MongoDbConnectionTest {
	
	String jsonTest = "{\n" + 
			"   \"name\" : \"MongoDB\",\n" + 
			"   \"type\" : \"database\",\n" + 
			"   \"count\" : 1,\n" + 
			"   \"versions\": [ \"v3.2\", \"v3.0\", \"v2.6\" ],\n" + 
			"   \"info\" : { x : 203, y : 102 }\n" + 
			"  }";
	
	public Document getJsonDocTest() {
		 return new Document("name", "MongoDB")
				 	.append("uuid", UUID.randomUUID().toString())
	                .append("type", "database")
	                .append("count", 1)
	                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
	                .append("info", new Document("x", 203).append("y", 102));
	}
	
	@Test
	public void testConnection() {
		Assert.assertTrue(ResourcesDAO.getCollectionTest().countDocuments() > 0);
	}
	
	@Test
	public void testInsertSimpleQueryDocument() {
		Document jsonDocTest = getJsonDocTest();
		String uuid = jsonDocTest.getString("uuid");
		ResourcesDAO.getCollectionTest().insertOne(jsonDocTest);
		FindIterable<Document> find = ResourcesDAO.getCollectionTest().find(eq("uuid", uuid));
		Assert.assertTrue(find.first() != null);
	}

	@Test
	public void testInsertRentProperty() {
		long count1 = ResourcesDAO.getCollectionPropertyTest().countDocuments();
		RentProperty property = RentProperty.builder()
											.characteristics("2bdrm")
											.creationDate(new Date())
											.fullDescription("apartment test")
											.id(new Random().nextLong())
											.link("link")
											.price(new Random().nextDouble())
											.title("title test")
											.build();

		ResourcesDAO.getCollectionPropertyTest().insertOne(property);
		long count2 = ResourcesDAO.getCollectionPropertyTest().countDocuments();

		Assert.assertTrue(count2 > count1);
	}

}
