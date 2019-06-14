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
		StringBuffer str = new StringBuffer("[");
		for(RentProperty p : dao.getProperties()) {
			str.append(p.getPrice()+",");
		}
		str.replace(str.lastIndexOf(",")-1, str.lastIndexOf(","), "]");
		
		return str.toString();
	}
}
