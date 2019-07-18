package data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.lab.webcrawler.entity.PredictionModel;

import core.util.UtilFunctions;
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

	@Getter
	@Setter
	private PredictionModel selectedModel;

	@Getter
	@Setter
	private int index;

	public void initDataModels() {
		predictionModels = dao.getPredictionModels();
	}

	public void processChange() {
		if (selectedModel != null)
			UtilFunctions.adicionarMsg("Selected Model: " + selectedModel.getModel(), false);
		else
			UtilFunctions.adicionarMsg("NO Selected Model", false);

	}

	public boolean isShowDetails() {
		return selectedModel != null;
	}

	public List<String> getCharts() {
		List<String> list = new ArrayList<>();
		if (selectedModel != null) {
			if (!UtilFunctions.isNullEmpty(selectedModel.getChart()))
				list.add(selectedModel.getChart());
			if (!UtilFunctions.isNullEmpty(selectedModel.getChart2()))
				list.add(selectedModel.getChart2());
			if (!UtilFunctions.isNullEmpty(selectedModel.getChart3()))
				list.add(selectedModel.getChart3());
		}
		return list;
	}

	public String isClear(int index) {
		if (index % 2 == 0)
			return "clearfix";

		return "";
	}

}
