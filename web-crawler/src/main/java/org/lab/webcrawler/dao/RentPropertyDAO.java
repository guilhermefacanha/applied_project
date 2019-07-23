package org.lab.webcrawler.dao;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.RentProperty;

import com.mongodb.BasicDBObject;

public class RentPropertyDAO implements Serializable {
	private static final long serialVersionUID = 8200291980072454185L;

	public void saveAll(List<RentProperty> list) {
		StringBuffer str = new StringBuffer();
		List<Long> ids = getRegisteredIds();
		for (RentProperty rentProperty : list) {
			try {
				if (!ids.contains(rentProperty.getId())) {
					save(rentProperty);
					System.out.println("New Prop.: " + rentProperty.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Warnings/Errors");
		System.out.println(str.toString());
	}
	
	public void save(RentProperty property) {
		ResourcesDAO.getCollectionProperty().insertOne(property);
	}
	
	public void updateFullDescription(RentProperty p) {
		ResourcesDAO.getCollectionProperty().updateOne(eq("_id", p.getId()), new Document("$set", new Document("fullDescription", p.getFullDescription())));
	}

	public void updateCreationDate(RentProperty p) {
		ResourcesDAO.getCollectionProperty().updateOne(eq("_id", p.getId()), new Document("$set", new Document("creationDate", p.getCreationDate())));
	}

	public void updateRentDate(RentProperty p) {
		Document document = new Document();
		document.append("numberDays", p.getNumberDays());
		document.append("soldDate", p.getSoldDate());
		ResourcesDAO.getCollectionProperty().updateOne(eq("_id", p.getId()), new Document("$set", document));
	}
	
	public void removeNumberDaysSoldDate() {
		Document document = new Document();
		document.append("numberDays", null);
		document.append("soldDate", null);
		ResourcesDAO.getCollectionProperty().updateMany(exists("numberDays"), new Document("$set", document));
	}
	
	public List<RentProperty> getPropertiesWithLinkCreationNull(){
		List<RentProperty> list = new ArrayList<>();
		BasicDBObject query = new BasicDBObject("creationDate", null);
		ResourcesDAO.getCollectionProperty().find(query).into(list);
		return list;
	}

	public List<RentProperty> getPropertiesWithLinkNotRented(){
		List<RentProperty> list = new ArrayList<>();
		BasicDBObject query = new BasicDBObject("soldDate", null);
		ResourcesDAO.getCollectionProperty().find(query).into(list);
		return list;
	}

	public List<RentProperty> getPropertiesNullDescription(){
		List<RentProperty> list = new ArrayList<>();
		BasicDBObject query = new BasicDBObject("fullDescription", null);
		ResourcesDAO.getCollectionProperty().find(query).into(list);
		return list;
	}
	
	private List<Long> getRegisteredIds(){
		List<Long> list = new ArrayList<>();
		Consumer<RentProperty> consumer = (RentProperty r) -> {
			list.add(r.getId());
		};
		ResourcesDAO.getCollectionProperty().find().projection(include("id")).forEach(consumer);
		
		return list;
	}
	
	public RentProperty getPropertyById(long id) {
		BasicDBObject query = new BasicDBObject("_id", id);
		return ResourcesDAO.getCollectionProperty().find(query).limit(1).first();
	}
	public RentProperty getPropertyByLink(String link) {
		BasicDBObject query = new BasicDBObject("link", link);
		return ResourcesDAO.getCollectionProperty().find(query).limit(1).first();
	}

	private boolean existsProperty(long id) {
//		RentProperty find = entityManager.find(RentProperty.class, id);
//		return find != null;
		return false;
	}

	public void remove(RentProperty p) {
		ResourcesDAO.getCollectionProperty().findOneAndDelete(eq("_id", p.getId()));
	}

	public long getPropertiesSize() {
		return ResourcesDAO.getCollectionProperty().countDocuments();
	}

}
