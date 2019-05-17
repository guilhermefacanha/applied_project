package org.lab.webcrawler.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public RentProperty(long id, String link,String description, Date creation) {
		super();
		this.id = id;
		this.link = link;
		this.fullDescription = description;
		this.creationDate = creation;
	}
	
	public String getCreationDateStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return dateFormat.format(creationDate);
	}
}
