package data.bean;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.lab.webcrawler.entity.RentProperty;

import data.dao.DataAnalysisDAO;

@ViewScoped
@Named
public class DataAnalysisBean implements Serializable {

	private static final long serialVersionUID = -4768375840231656412L;

	@Inject
	DataAnalysisDAO dao;

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
		return dao.getPricesArray();
	}
	
	public String getBedroomsArray() {
		return dao.getBedroomsArray();
	}
	
	public String getLocationLabelArray() {
		return dao.getLocationDistribution()[0];
	}
	
	public String getLocationValuesArray() {
		return dao.getLocationDistribution()[1];
	}
	
	public String getBedroomAverage() {
		return NumberFormat.getInstance().format(dao.getAverage("bedrooms"));
	}
	
	public String getNumberSuitLaundry() {
		return NumberFormat.getInstance().format(
				dao.getProperties().stream().filter(p -> p.getSuit_laundry()==1).count());
	}
	
	public String getNumberParkStall() {
		return NumberFormat.getInstance().format(
				dao.getProperties().stream().filter(p -> p.getPark_stall()==1).count());
	}
}
