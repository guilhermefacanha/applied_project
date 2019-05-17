package org.lab.webcrawler.data;

import java.util.List;

import lombok.Data;

@Data
public class DataTable {
	
	private List<Row> rows;
	private List<String> columns;

}
