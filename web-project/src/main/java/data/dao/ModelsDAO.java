package data.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.PredictionModel;

import com.mongodb.client.model.Sorts;

public class ModelsDAO implements Serializable {
	
	private static final long serialVersionUID = 5614792379039248871L;

	public List<PredictionModel> getPredictionModels(){
		List<PredictionModel> list = new ArrayList<>();
		List<Document> documents = new ArrayList<>();
		ResourcesDAO.getCollectionPredictionModel().find().sort(Sorts.descending("date")).into(documents);
		documents.forEach(d -> list.add(new PredictionModel(d)));
		return list;
	}

}
