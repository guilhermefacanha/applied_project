package core.dao.resources;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe responsavel por injetar {@link EntityManager} Resource
 * @author gfsolucoesti
 */
public class Resources {

	private static EntityManagerFactory factory = null;

	static {
		try {
			factory = Persistence.createEntityManagerFactory("pu");
		} catch (Exception e) {
			System.out.println("ERRO ao inicializar FACTORY JPA");
			e.printStackTrace();
			
		}
	}

	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	public void dispose(@Disposes EntityManager em) {
		em.close();
	}

	public static EntityManagerFactory getFactory() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("pu");
		}
		return factory;
	}
}