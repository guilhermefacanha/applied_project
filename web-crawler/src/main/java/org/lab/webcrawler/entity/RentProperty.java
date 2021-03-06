package org.lab.webcrawler.entity;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentProperty implements Serializable {
	private static final long serialVersionUID = 3616826123718273035L;
	
	private long id;
	private String title;
	private String characteristics;
	private String fullDescription;
	private double price;
	private String link;
	private Date creationDate;
	private Date soldDate;
	private Integer numberDays;
	
	private double bedrooms;
	private double bath;
	private double size_sqft;
	private double professionally_managed;
	private double no_pet_allowed;
	private double suit_laundry;
	private double park_stall;
	private double available_now;
	private double amenities;
	private double near_school;
	private double brand_new;
	private double furnished;
	private double loc_vancouver;
	private double loc_burnaby;
	private double loc_richmond;
	private double loc_surrey;
	private double loc_newwest;
	private double loc_abbotsford;
	private double loc_other;
	private double no_basement = -1;

	@SerializedName("GradientBoostingRegressor")
	private double gradient_boost;
	@SerializedName("XGBRegressor")
	private double xgbRegressor;
	@SerializedName("OLS Regression")
	private double linearRegression;
	@SerializedName("DMatrix Regression")
	private double dmatrixRegression;
	@SerializedName("XGB Linear Regression")
	private double xgbLinearRegression;
	
	@BsonIgnore
	public String getLinearRegressionStr() {
		return NumberFormat.getCurrencyInstance().format(linearRegression);
	}
	
	@BsonIgnore
	public String getXgbRegressorStr() {
		return NumberFormat.getCurrencyInstance().format(xgbRegressor);
	}
	
	@BsonIgnore
	public String getGradientBoostStr() {
		return NumberFormat.getCurrencyInstance().format(gradient_boost);
	}

	@BsonIgnore
	public String getDmatrixStr() {
		return NumberFormat.getCurrencyInstance().format(dmatrixRegression);
	}
	
	@BsonIgnore
	public String getXgbLinearStr() {
		return NumberFormat.getCurrencyInstance().format(xgbLinearRegression);
	}
	
	public void resetProperties() {
		loc_vancouver = 0;
		loc_burnaby = 0;
		loc_newwest = 0;
		loc_richmond = 0;
		loc_surrey = 0;
		loc_abbotsford = 0;
		loc_other = 0;
		
		professionally_managed = 0;
		no_pet_allowed = 0;
		suit_laundry = 0;
		park_stall = 0;
		available_now = 0;
		amenities = 0;
		brand_new = 0;
		no_basement = 1;
		furnished = 0;
	}
	
	@BsonIgnore
	public String getJson() {
		return new Gson().toJson(this);
	}
	public void resetPrices() {
		this.linearRegression = 0;
		this.gradient_boost = 0;
		this.xgbRegressor = 0;
		this.xgbLinearRegression= 0;
		this.dmatrixRegression = 0;
	}

	public RentProperty(long id, String link,String description, Date creation) {
		super();
		this.id = id;
		this.link = link;
		this.fullDescription = description;
		this.creationDate = creation;
	}
	
	@BsonIgnore
	public String getCreationDateStr() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			return dateFormat.format(creationDate);
		} catch (Exception e) {
			return "";
		}
	}
}
