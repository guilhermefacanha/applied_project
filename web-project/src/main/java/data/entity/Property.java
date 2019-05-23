package data.entity;

import java.io.Serializable;
import java.text.NumberFormat;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class Property implements Serializable {

	private static final long serialVersionUID = 6691679449665407258L;

	private double price;
	private double bedrooms;
	private double bath;
	private double size_sqft;
	private double professionally_managed;
	private double no_pet_allowed;
	private double suit_laundry;
	private double park_stall;
	private double available_now;
	private double amenities;
	private double brand_new;
	private double loc_vancouver;
	private double loc_burnaby;
	private double loc_richmond;
	private double loc_surrey;
	private double loc_newwest;
	private double loc_abbotsford;
	private double no_basement = 1;
	private double gradient_boost;
	private double gradient_xgb;
	private double regression;
	
	public String getRegressionStr() {
		return NumberFormat.getCurrencyInstance().format(regression);
	}
	public String getGradientXgbStr() {
		return NumberFormat.getCurrencyInstance().format(gradient_xgb);
	}
	public String getGradientBoostStr() {
		return NumberFormat.getCurrencyInstance().format(gradient_boost);
	}
	
	public void resetProperties() {
		loc_vancouver = 0;
		loc_burnaby = 0;
		loc_newwest = 0;
		loc_richmond = 0;
		loc_surrey = 0;
		loc_abbotsford = 0;
		
		professionally_managed = 0;
		no_pet_allowed = 0;
		suit_laundry = 0;
		park_stall = 0;
		available_now = 0;
		amenities = 0;
		brand_new = 0;
		no_basement = 1;
	}
	
	public String getJson() {
		return new Gson().toJson(this);
	public void resetPrices() {
		this.regression = 0;
		this.gradient_boost = 0;
		this.gradient_xgb = 0;
	}
}