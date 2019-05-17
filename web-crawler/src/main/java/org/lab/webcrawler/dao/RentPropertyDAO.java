package org.lab.webcrawler.dao;

import java.util.List;

import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.RentProperty;

public class RentPropertyDAO {
	public void saveAll(List<RentProperty> list) {
		StringBuffer str = new StringBuffer();
		List<Long> ids = getRegisteredIds();
		for (RentProperty rentProperty : list) {
			try {
				if (!ids.contains(rentProperty.getId())) {
//					entityManager.persist(rentProperty);
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
//		entityManager.createQuery("UPDATE RentProperty x SET x.fullDescription = :desc WHERE x.id = :id").setParameter("id", p.getId()).setParameter("desc", p.getFullDescription()).executeUpdate();
	}

	public void updateCreationDate(RentProperty p) {
//		entityManager.createQuery("UPDATE RentProperty x SET x.creationDate = :date WHERE x.id = :id").setParameter("id", p.getId()).setParameter("date", p.getCreationDate()).executeUpdate();
	}

	public void updateRentDate(RentProperty p) {
//		entityManager.createQuery("UPDATE RentProperty x SET x.soldDate = :date, x.numberDays = :days WHERE x.id = :id")
//								.setParameter("id", p.getId())
//								.setParameter("days", p.getNumberDays())
//								.setParameter("date", p.getCreationDate()).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<RentProperty> getPropertiesWithLinkCreationNull(){
//		return entityManager.createQuery("SELECT NEW RentProperty(x.id,x.link,x.fullDescription,x.creationDate) FROM RentProperty x WHERE x.creationDate is null").getResultList();
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<RentProperty> getPropertiesWithLinkNotRented(){
//		return entityManager.createQuery("SELECT NEW RentProperty(x.id,x.link,x.fullDescription,x.creationDate) FROM RentProperty x WHERE x.soldDate is null").getResultList();
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<RentProperty> getPropertiesNullDescription(){
//		return entityManager.createQuery("SELECT x FROM RentProperty x WHERE x.soldDate is null AND ( x.fullDescription is null OR x.fullDescription = '' )").getResultList();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getRegisteredIds(){
//		return entityManager.createQuery("SELECT x.id FROM RentProperty x").getResultList();
		return null;
	}

	private boolean exists(long id) {
//		RentProperty find = entityManager.find(RentProperty.class, id);
//		return find != null;
		return false;
	}

	public void remove(RentProperty p) {
//		entityManager.createQuery("DELETE FROM RentProperty x WHERE x.id = :id")
//								.setParameter("id", p.getId())
//								.executeUpdate();
	}

	public long getPropertiesSize() {
//		return (Long) entityManager.createQuery("SELECT COUNT(x.id) FROM RentProperty x").getSingleResult();
		return 0;
	}

}
