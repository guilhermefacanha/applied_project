package org.lab.webcrawler.entity;

import java.io.Serializable;
import java.util.Date;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PredictionModel implements Serializable{
	private static final long serialVersionUID = -3419268092170701057L;
	
	public PredictionModel(Document document){
		if(document.containsKey("summary"))
			this.summary = document.get("summary").getClass().toString().contains("Document") ? ((Document) document.get("summary")).toJson() : document.getString("summary");
		if(document.containsKey("chart2"))
			this.chart2 = document.getString("chart2");
		if(document.containsKey("chart3"))
			this.chart3 = document.getString("chart3");
		
		this.id = document.getString("_id");
		this.model = document.getString("model");
		this.file = document.getString("file");
		this.chart = document.getString("chart");
		this.date = document.getDate("date");
		this.performance = document.getDouble("performance");
		this.mean_error = document.getDouble("mean_error");
	}

	@BsonProperty("id")
	String id;
	String model;
	String file;
	String chart;
	String chart2;
	String chart3;
	Date date;
	Double performance;
	Double mean_error;
	String summary;
}
