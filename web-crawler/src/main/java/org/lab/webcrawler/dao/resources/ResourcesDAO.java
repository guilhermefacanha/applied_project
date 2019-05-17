package org.lab.webcrawler.dao.resources;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.lab.webcrawler.entity.RentProperty;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Class responsible for providing Connection objects for DAO layer
 */
public class ResourcesDAO {

	private static MongoClientURI connectionString = new MongoClientURI(
			"mongodb://adminuser:admin123@ds149365.mlab.com:49365/rent-analysis");
	private static MongoClient client;
	private static MongoDatabase database;
	private static MongoCollection<Document> collectionTest;
	private static MongoCollection<RentProperty> collectionPropertyTest;
	private static MongoCollection<RentProperty> collectionProperty;
	

	static {
		try {
			CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
	                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			client = new MongoClient(connectionString);
			database = client.getDatabase("rent-analysis");
			database = database.withCodecRegistry(pojoCodecRegistry);
			collectionTest = database.getCollection("test");
			collectionProperty = database.getCollection("property", RentProperty.class);
			collectionPropertyTest = database.getCollection("property_test", RentProperty.class);
		} catch (Exception e) {
			System.out.println("ERROR CONNECTING TO MONGODB " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static MongoCollection<Document> getCollectionTest() {
		return collectionTest;
	}

	public static MongoCollection<RentProperty> getCollectionProperty() {
		return collectionProperty;
	}
	
	public static MongoCollection<RentProperty> getCollectionPropertyTest() {
		return collectionPropertyTest;
	}

	public static void dispose() {
		try {
			client.close();
		} catch (Exception e) {
		}
	}

}