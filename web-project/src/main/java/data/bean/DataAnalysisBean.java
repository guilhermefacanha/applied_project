package data.bean;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import core.util.UtilFunctions;
import data.dao.DataAnalysisDAO;
import data.enumeration.StatsOp;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class DataAnalysisBean implements Serializable {

	private static final long serialVersionUID = -4768375840231656412L;

	@Inject
	DataAnalysisDAO dao;

	@Getter
	@Setter
	int upperLevelBd;
	@Getter
	@Setter
	int lowerLevelBd;
	@Getter
	@Setter
	int upperLevelPr;
	@Getter
	@Setter
	int lowerLevelPr;

	public boolean isBdPlot() {
		return upperLevelBd > 0;
	}

	public boolean isPrPlot() {
		return upperLevelPr > 0;
	}

	public String getDataSize() {
		return NumberFormat.getInstance().format(dao.getDataSize());
	}

	public String getDataWithValueSize() {
		return NumberFormat.getInstance().format(dao.getDataSizeWithValue());
	}

	public String getDataPriceAverage() {
		return NumberFormat.getCurrencyInstance().format(dao.getDataPriceAverage());
	}

	public void refreshData() {
		dao.refreshData();
	}

	public String getPricesArray() {
		if (upperLevelPr != 0)
			return dao.getPricesArray(lowerLevelPr, upperLevelPr);
		return "[]";
	}

	public String getBedroomsArray() {
		if (upperLevelBd != 0)
			return dao.getBedroomsArray(lowerLevelBd, upperLevelBd);

		return "[]";
	}

	public String getLocationLabelArray() {
		return dao.getLocationDistribution()[0];
	}

	public String getLocationValuesArray() {
		return dao.getLocationDistribution()[1];
	}

	public String getLocationPriceValuesArray() {
		return dao.getLocationDistribution()[2];
	}

	public String getBedroomAverage() {
		double number = (Double) dao.getAggregate("bedrooms", StatsOp.AVG);
		return UtilFunctions.formatNumber(number, 2, 2);
	}

	public String getBathroomAverage() {
		double number = (Double) dao.getAggregate("bath", StatsOp.AVG);
		return UtilFunctions.formatNumber(number, 2, 2);
	}
	
	public String getNumberPercent(String field) {
		double number = Double.valueOf(getNumber(field)) / dao.getDataSizeWithValue();
		return UtilFunctions.formatPercent(number, 2, 2);
	}

	public String getNumberStr(String field) {
		return NumberFormat.getInstance().format(getNumber(field));
	}
	
	public long getNumber(String field) {
		if (field.equals("furnished"))
			return dao.getProperties().stream().filter(p -> p.getFurnished() == 1).count();
		else if(field.equals("new"))
			return dao.getProperties().stream().filter(p -> p.getBrand_new() == 1).count();
		else if(field.equals("laundry"))
			return dao.getProperties().stream().filter(p -> p.getSuit_laundry() == 1).count();
		else if(field.equals("park"))
			return dao.getProperties().stream().filter(p -> p.getPark_stall() == 1).count();
		else if(field.equals("amenities"))
			return dao.getProperties().stream().filter(p -> p.getAmenities() == 1).count();
		else if(field.equals("school"))
			return dao.getProperties().stream().filter(p -> p.getNear_school() == 1).count();
		else if(field.equals("now"))
			return dao.getProperties().stream().filter(p -> p.getAvailable_now() == 1).count();
		else if(field.equals("nopet"))
			return dao.getProperties().stream().filter(p -> p.getNo_pet_allowed() == 1).count();
		else if(field.equals("managed"))
			return dao.getProperties().stream().filter(p -> p.getProfessionally_managed() == 1).count();
		
		
		return 0;
	}

	public String getMinDate() {
		Object value = dao.getAggregate("creationDate", StatsOp.MIN, "price", 0, 50000);
		Date dt = (Date) value;
		return UtilFunctions.getStringFromDate(dt, "dd MMM, yyyy");
	}

	public String getMaxDate() {
		Object value = dao.getAggregate("creationDate", StatsOp.MAX, "price", 0, 50000);
		Date dt = (Date) value;
		return UtilFunctions.getStringFromDate(dt, "dd MMM, yyyy");
	}
}
