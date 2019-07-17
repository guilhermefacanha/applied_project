package org.lab.webcrawler.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PredictionModel implements Serializable{
	private static final long serialVersionUID = -3419268092170701057L;

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
