package data.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.RentProperty;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import core.util.UtilFunctions;

public class DataAnalysisDAO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static long dataSize;
	private static long dataSizeWithValue;
	private static double dataPriceAverage;
	private static List<RentProperty> properties = new ArrayList<>();
	
	public long getDataSize() {
		if(dataSize==0)
			populateDataSize();
		
		return DataAnalysisDAO.dataSize;
	}
	
	public long getDataSizeWithValue() {
		if(dataSizeWithValue==0)
			populateDataSizeWithValue();
		return DataAnalysisDAO.dataSizeWithValue;
	}
	
	public double getDataPriceAverage() {
		if(dataPriceAverage == 0.0)
			populateAverage();
		return dataPriceAverage;
	}
	
	public List<RentProperty> getProperties() {
		if(!UtilFunctions.isListaValida(properties))
			populateListProperties();
		return properties;
	}
	
	public void populateDataSize() {
		dataSize = ResourcesDAO.getCollectionProperty().countDocuments();
	}
	
	public void populateDataSizeWithValue() {
		Document priceValueFilter = new Document("$gt", 0);
		Document query = new Document("price",priceValueFilter);
		dataSizeWithValue = ResourcesDAO.getCollectionProperty().countDocuments(query);
	}
	
	public void populateListProperties() {
		Document priceValueFilter = new Document("$gt", 0);
		Document query = new Document("price",priceValueFilter);
		ResourcesDAO.getCollectionProperty().find(query).into(properties);
	}
	
	public void populateAverage() {
		MongoCollection<Document> dbCollection = ResourcesDAO.getCollectionPropertyDocument();
		Document query = Document.parse("{\r\n" + 
				"		$match: {\r\n" + 
				"			price: {\r\n" + 
				"				$gt: 0\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"	}"); 
		Document avg = Document.parse("{\r\n" + 
				"		$group: {\r\n" + 
				"			_id: null,\r\n" + 
				"			price: {\r\n" + 
				"				$avg: \"$price\"\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"	}");
		AggregateIterable<Document> aggregate = dbCollection.aggregate(Arrays.asList(query,avg));
		Document result = aggregate.first();
		dataPriceAverage = result.getDouble("price");
	}

	public void refreshData() {
		populateAverage();
		populateDataSize();
		populateDataSizeWithValue();
		populateListProperties();
	}

}
