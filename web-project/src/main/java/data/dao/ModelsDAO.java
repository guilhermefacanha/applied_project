package data.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.lab.webcrawler.dao.resources.ResourcesDAO;
import org.lab.webcrawler.entity.PredictionModel;

public class ModelsDAO {
	
	public List<PredictionModel> getPredictionModels(){
		List<PredictionModel> list = new ArrayList<>();
		Document sort = Document.parse("{'date':-1}");
		ResourcesDAO.getCollectionPredictionModel().find().sort(sort).into(list);
		return list;
	}

}
