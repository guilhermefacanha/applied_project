import pickle
import xgboost as xg

from db.propertiesdao import PropertiesDao
from _datetime import datetime

class Simulator:

    def __init__(self):
        self.dao = PropertiesDao();
        self.loadModels();
    
    def loadModels(self):
        self.path = 'data/'
        self.dateLoaded = datetime.now();
        self.models = []
        
        modelNames = ['XGBRegressor','GradientBoostingRegressor','OLS Regression','DMatrix Regression','XGB Linear Regression']
        
        for name in modelNames:
            model = self.dao.getLastModel(name)
            if model is not None:
                self.models.append(model)
        
        self.modelsApp = [];
        
        for model in self.models:
            name = model['model']
            print('Loading Model ',name)
            model_reg = pickle.load(open(self.path+model['file'], 'rb'))
            model_json = {'key':name,'model':model_reg}
            self.modelsApp.append(model_json)
        
        print('----- Models Loaded Successfully -----')
    
    
        
    def simulate(self, record):
        try:
            df = record[self.dao.getDataSetModelNames()]
            
            for model in self.modelsApp:
                key = model['key']
                reg = model['model']
                if key == 'DMatrix Regression':
                    dtest = xg.DMatrix(df, label='')
                    pred = reg.predict(dtest)
                else:
                    pred = reg.predict(df)
            
                record[key] = pred.tolist();
            return record;
        except Exception as ex:
            print('Error simulating models ',ex)