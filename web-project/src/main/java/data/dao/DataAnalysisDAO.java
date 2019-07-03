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

public class DataAnalysisDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static long dataSize;
	private static long dataSizeWithValue;
	private static double dataPriceAverage;
	private static List<RentProperty> properties = new ArrayList<>();
	private static String[] locationDistribution;

	private static String pricesArray;
	private static String bedroomsArray;

	public void refreshData() {
		dataPriceAverage = getAverage("price");
		populateDataSize();
		populateDataSizeWithValue();
		populateListProperties();
		populateLocationDistribution();
	}

	// getMethods
	public String[] getLocationDistribution() {
		if (locationDistribution == null)
			populateLocationDistribution();
		return locationDistribution;
	}

	public long getDataSize() {
		if (dataSize == 0)
			populateDataSize();

		return DataAnalysisDAO.dataSize;
	}

	public long getDataSizeWithValue() {
		if (dataSizeWithValue == 0)
			populateDataSizeWithValue();
		return DataAnalysisDAO.dataSizeWithValue;
	}

	public double getDataPriceAverage() {
		if (dataPriceAverage == 0.0)
			dataPriceAverage = getAverage("price");
		return dataPriceAverage;
	}

	public List<RentProperty> getProperties() {
		if (!UtilFunctions.isListaValida(properties))
			populateListProperties();
		return properties;
	}

	public String getPricesArray() {
		if (pricesArray == null)
			populatePricesArray();

		return pricesArray;
	}

	public String getBedroomsArray() {
		if (bedroomsArray == null)
			populateBedroomsArray();
		
		return bedroomsArray;
	}

	// Populate Methods
	private void populateLocationDistribution() {
		locationDistribution = new String[3];
		String labels = "['Vancouver','Burnaby','Richmond','Surrey','New Westminster','Abbotsford']";
		double[] values = new double[6];
		double[] averages = new double[6];
		for (RentProperty p : getProperties()) {
			if (p.getLoc_vancouver() == 1) {
				values[0] += 1;
				averages[0] += p.getPrice();
			}
			else if (p.getLoc_burnaby() == 1){
				values[1] += 1;
				averages[1] += p.getPrice();
			}
			else if (p.getLoc_richmond() == 1) {
				values[2] += 1;
				averages[2] += p.getPrice();
			}
			else if (p.getLoc_surrey() == 1) {
				values[3] += 1;
				averages[3] += p.getPrice();
			}
			else if (p.getLoc_newwest() == 1) {
				values[4] += 1;
				averages[4] += p.getPrice();
			}
			else if (p.getLoc_abbotsford() == 1) {
				values[5] += 1;
				averages[5] += p.getPrice();
			}
		}
		
		for (int i = 0; i < averages.length; i++) {
			averages[i] = averages[i] / values[i];
		}
		
		StringBuffer str = new StringBuffer("[");
		for (double d : values) {
			str.append(d + ",");
		}
		String locValues = str.substring(0, str.length()-1)+"]";
		
		
		str = new StringBuffer("[");
		for (double d : averages) {
			str.append(d + ",");
		}
		String locPriceValues = str.substring(0, str.length()-1)+"]";

		locationDistribution[0] = labels;
		locationDistribution[1] = locValues;
		locationDistribution[2] = locPriceValues;
	}

	public void populateDataSize() {
		dataSize = ResourcesDAO.getCollectionProperty().countDocuments();
	}

	public void populateDataSizeWithValue() {
		Document priceValueFilter = new Document("$gt", 0);
		Document query = new Document("price", priceValueFilter);
		dataSizeWithValue = ResourcesDAO.getCollectionProperty().countDocuments(query);
	}

	public void populateListProperties() {
		properties = new ArrayList<>();
		Document priceValueFilter = new Document("$gt", 0);
		Document query = new Document("price", priceValueFilter);
		ResourcesDAO.getCollectionProperty().find(query).into(properties);
	}

	/**
	 *  Example on query mongo command line
	 *  db.property.aggregate(
		[
			{$match:{bath:{$lt:50000, $gt:0}}}, 
			{$group:{_id: null, bath:{$avg:"$bath"}}}
		]);
	 * 
	 * 
	 * */
	public double getAverage(String field) {
		MongoCollection<Document> dbCollection = ResourcesDAO.getCollectionPropertyDocument();
		Document query = Document.parse("{" 
				+ "$match: {"
				+ 	field + ": {$lt: 50000,$gt: 0}"
				+ "	}" 
				+ "}");
		Document avg = Document
				.parse("{$group: {_id: null,"+ field + ": {$avg: \"$" +field+"\"}}}");
		AggregateIterable<Document> aggregate = dbCollection.aggregate(Arrays.asList(query, avg));
		Document result = aggregate.first();
		return result.getDouble(field);
	}

	private void populatePricesArray() {
		StringBuffer str = new StringBuffer("[");
		for (RentProperty p : getProperties()) {
			str.append(p.getPrice() + ",");
		}
		pricesArray = str.substring(0, str.length()-1)+"]";
	}
	
	private void populateBedroomsArray() {
		StringBuffer str = new StringBuffer("[");
		for (RentProperty p : getProperties()) {
			str.append(p.getBedrooms() + ",");
		}
		bedroomsArray = str.substring(0, str.length()-1)+"]";
		
	}

}
