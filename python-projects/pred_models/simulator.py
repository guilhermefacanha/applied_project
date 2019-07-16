import pickle
from db.propertiesdao import PropertiesDao
from _datetime import datetime

class Simulator:

    def __init__(self):
        self.dao = PropertiesDao();
        self.loadModels();
    
    def loadModels(self):
        self.dateLoaded = datetime.now();
        
        self.grad_xgb = self.dao.getLastModel('XGBRegressor');
        self.grad_boost = self.dao.getLastModel('GradientBoostingRegressor');
        self.reg_model = self.dao.getLastModel('OLS Regression');
        self.dnatrix_model = self.dao.getLastModel('DMatrix Regression');
        self.xgb_linear_model = self.dao.getLastModel('XGB Linear Regression');
        
        self.filename_reg = 'data/'+self.reg_model
        self.filename_grad = 'data/'+self.grad_boost
        self.filename_grad_2 = 'data/'+self.grad_xgb
        
        self.model_reg = pickle.load(open(self.filename_reg, 'rb'))
        print('Regression model_reg loaded from saved data file')
    
        self.model_grad = pickle.load(open(self.filename_grad, 'rb'))
        print('Regression model_grad loaded from saved data file')
    
        self.model_grad_2 = pickle.load(open(self.filename_grad_2, 'rb'))
        print('Regression model_grad_2 loaded from saved data file')
    
        
    def simulate(self, record):
        # Print the output.
        print(record)

        try:
            df = record[['bedrooms', 'bath', 'size_sqft', 'professionally_managed', 'no_pet_allowed', 'suit_laundry', 'park_stall', 'available_now', 'amenities', 'brand_new','loc_vancouver', 'loc_burnaby', 'loc_richmond', 'loc_surrey', 'loc_newwest', 'loc_abbotsford', 'no_basement']]
            predictions_reg = self.model_reg.predict(df)  # make the predictions_reg by the model_reg
            predictions_grad = self.model_grad.predict(df)  # make the predictions_grad by the model_grad
            predictions_grad_2 = self.model_grad_2.predict(df)  # make the predictions_grad_2 by the model_grad
            
            record['regression'] = predictions_reg.tolist()
            record['gradient_xgb'] = predictions_grad.tolist()
            record['gradient_boost'] = predictions_grad_2.tolist()
            
            print("Property: ",record.price.tolist())
            print("Prediction Reg: ",predictions_reg.tolist())
            print("Prediction Grad Boosting: ",predictions_grad.tolist())
            print("Prediction Grad Boosting 2: ",predictions_grad_2.tolist())
            
            return record;
        except:
            print('Error loading models found at ',self.filename_reg)