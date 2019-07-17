package data.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.lab.webcrawler.entity.PredictionModel;

import data.dao.ModelsDAO;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class DataModelsBean implements Serializable {
	
	private static final long serialVersionUID = 9165118446213349081L;

	@Inject
	ModelsDAO dao;
	
	@Getter
	private List<PredictionModel> predictionModels;

	@Getter @Setter
	private PredictionModel selectedModel;
	
	public void initDataModels() {
		predictionModels = dao.getPredictionModels();
	}

}
