package data.dao;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.RentProperty;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import core.util.UtilFunctions;
import data.enumeration.StatsOp;

public class DataAnalysisDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	private static long dataSize;
	private static long dataSizeWithValue;
	private static double dataPriceAverage;
	private static List<RentProperty> properties = new ArrayList<>();
	private static String[] locationDistribution;

	private static String pricesArray;
	private static String bedroomsArray;

	private static int lbd, ubd, lpr, upr;

	Document queryValueFilter = Document.parse(
			"{'bedrooms':{'$gt':0,'$lt':6},'price':{'$gt':900,'$lt':6000},'bath':{'$gt':0,'$lt':6},'size_sqft':{'$gt':0}}");

	public void refreshData() {
		dataPriceAverage = (Double) getAggregate("price", StatsOp.AVG);
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
			dataPriceAverage = (Double) getAggregate("price", StatsOp.AVG);
		return dataPriceAverage;
	}

	public List<RentProperty> getProperties() {
		if (!UtilFunctions.isListaValida(properties))
			populateListProperties();
		return properties;
	}

	public String getPricesArray(int lowerLevelPr, int upperLevelPr) {
		if (pricesArray == null || lpr != lowerLevelPr || upr != upperLevelPr)
			populatePricesArray(lowerLevelPr, upperLevelPr);

		return pricesArray;
	}

	public String getBedroomsArray(int lowerLevelBd, int upperLevelBd) {
		if (bedroomsArray == null || lbd != lowerLevelBd || ubd != upperLevelBd)
			populateBedroomsArray(lowerLevelBd, upperLevelBd);

		return bedroomsArray;
	}

	// Populate Methods
	private void populateLocationDistribution() {
		locationDistribution = new String[3];
		String labels = "['Vancouver','Burnaby','Richmond','Surrey','New Westminster','Abbotsford','Other']";
		String[] split = labels.split(",");
		double[] values = new double[split.length];
		double[] averages = new double[split.length];
		for (RentProperty p : getProperties()) {
			if (p.getLoc_vancouver() == 1) {
				values[0] += 1;
				averages[0] += p.getPrice();
			} else if (p.getLoc_burnaby() == 1) {
				values[1] += 1;
				averages[1] += p.getPrice();
			} else if (p.getLoc_richmond() == 1) {
				values[2] += 1;
				averages[2] += p.getPrice();
			} else if (p.getLoc_surrey() == 1) {
				values[3] += 1;
				averages[3] += p.getPrice();
			} else if (p.getLoc_newwest() == 1) {
				values[4] += 1;
				averages[4] += p.getPrice();
			} else if (p.getLoc_abbotsford() == 1) {
				values[5] += 1;
				averages[5] += p.getPrice();
			} else if (p.getLoc_other() == 1) {
				values[6] += 1;
				averages[6] += p.getPrice();
			}
		}

		for (int i = 0; i < averages.length; i++) {
			averages[i] = averages[i] / values[i];
		}

		StringBuffer str = new StringBuffer("[");
		for (double d : values) {
			str.append(d + ",");
		}
		String locValues = str.substring(0, str.length() - 1) + "]";

		DecimalFormat df2 = new DecimalFormat("###.##");

		str = new StringBuffer("[");
		for (double d : averages) {
			str.append(df2.format(d) + ",");
		}
		String locPriceValues = str.substring(0, str.length() - 1) + "]";

		locationDistribution[0] = labels;
		locationDistribution[1] = locValues;
		locationDistribution[2] = locPriceValues;
	}

	public void populateDataSize() {
		dataSize = ResourcesDAO.getCollectionProperty().countDocuments();
	}

	public void populateDataSizeWithValue() {
		dataSizeWithValue = ResourcesDAO.getCollectionProperty().countDocuments(queryValueFilter);
	}

	public void populateListProperties() {
		properties = new ArrayList<>();
		ResourcesDAO.getCollectionProperty().find(queryValueFilter).into(properties);
	}

	/**
	 * Example on query mongo command line db.property.aggregate( [
	 * {$match:{bath:{$lt:50000, $gt:0}}}, {$group:{_id: null, bath:{$avg:"$bath"}}}
	 * ]);
	 * 
	 * 
	 */
	public Object getAggregate(String field, StatsOp op, String filterField, double lowerValue, double upperValue) {
		if (filterField == null)
			filterField = field;

		MongoCollection<Document> dbCollection = ResourcesDAO.getCollectionPropertyDocument();
		Document query = Document.parse(
				"{" + "$match: {" + filterField + ": {$lt: " + upperValue + ",$gt: " + lowerValue + "}" + "	}" + "}");
		Document aggregation = Document.parse(
				"{$group: {_id: null," + field + ": {$" + op.toString().toLowerCase() + ": \"$" + field + "\"}}}");
		AggregateIterable<Document> aggregate = dbCollection.aggregate(Arrays.asList(query, aggregation));
		Document result = aggregate.first();
		return result.get(field);
	}

	public Object getAggregate(String field, StatsOp op) {
		return getAggregate(field, op, field, 0, 50000);
	}

	public Object getAggregate(String field, StatsOp op, double lowerValue, double upperValue) {
		return getAggregate(field, op, field, lowerValue, upperValue);
	}

	private void populatePricesArray(int lowerLevelPr, int upperLevelPr) {
		lpr = lowerLevelPr;
		upr = upperLevelPr;
		StringBuffer str = new StringBuffer("[");
		for (RentProperty p : getProperties()) {
			if (p.getPrice() >= lpr && p.getPrice() <= upr)
				str.append(p.getPrice() + ",");
		}
		pricesArray = str.substring(0, str.length() - 1) + "]";
	}

	private void populateBedroomsArray(int lowerLevelBd, int upperLevelBd) {
		ubd = upperLevelBd;
		lbd = lowerLevelBd;
		StringBuffer str = new StringBuffer("[");
		for (RentProperty p : getProperties()) {
			if (p.getBedrooms() >= lbd && p.getBedrooms() <= ubd)
				str.append(p.getBedrooms() + ",");
		}
		bedroomsArray = str.substring(0, str.length() - 1) + "]";

	}

}
