package org.lab.webcrawler.data;

import java.util.ArrayList;
import java.util.List;

import org.lab.webcrawler.util.UtilFunction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrawlerSearch {
	
	/**
	 * searchForElement you can use a tag with attributes
	 * <br/>Example: li[class=myClass] will search for elements of tag li with attribute class with value myClass
	 * <br/>Example: span[class=infoSpan] will search for elements of tag span with attribute class with value infoSpan
	 */
	private String searchForElement;
	
	/**
	 * typeValueSearch where the value is going to be found
	 * <br/>Example: html, data, attribute
	 */
	private String typeValueSearch;
	
	private CrawlerSearch crawlerSearchNextLevel;
	private List<CrawlerSearch> searchValues;
	
	public void addSearchValue(CrawlerSearch search) {
		if(UtilFunction.isListaValida(searchValues))
			searchValues = new ArrayList<>();
		
		searchValues.add(search);
	}

}
