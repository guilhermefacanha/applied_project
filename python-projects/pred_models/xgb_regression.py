#run with python -m pred_models.xgb_regression

import xgboost as xgb
import pandas as pd
import numpy as np
import statsmodels.api as sm
import matplotlib.pyplot as plt
import pickle
import statistics
import datetime

from sklearn.model_selection import train_test_split
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import mean_squared_error
from bson.objectid import ObjectId
from db.propertiesdao import PropertiesDao

def saveModelData(performance, mean_error, summary=''):
    data = {};
    data['_id'] = str(ObjectId())
    data['model'] = 'XGBRegressor'
    data['file'] = filename
    data['chart'] = chartfilename
    data['date'] = datetime.datetime.utcnow()
    data['performance'] = performance
    data['mean_error'] = mean_error
    data['summary'] = summary
    dao.saveModel(data)


# #############################################################################
dao = PropertiesDao()

# Get current date and time and create a time stamp
x = datetime.datetime.now()
dtstamp = x.strftime("%Y%m%d")

# Files variables
path = 'data/'
filename = 'grad_xgb_model'+dtstamp+'.sav'
chartfilename = 'grad_xgb_model'+dtstamp+'.png'
chartfilename2 = 'grad_xgb_model_importance'+dtstamp+'.png'

save = False

# Load data
dataset = dao.getDataSetModel()
dataset = pd.DataFrame.from_dict(dataset)
print('Dataset Acquired: (',len(dataset),')')


#Create training and test datasets
X = dataset[['bedrooms', 'bath', 'size_sqft', 'professionally_managed', 'no_pet_allowed', 'suit_laundry', 'park_stall', 'available_now', 'furnished', 'amenities', 'brand_new','loc_vancouver', 'loc_burnaby', 'loc_richmond', 'loc_surrey', 'loc_newwest', 'loc_abbotsford', 'no_basement']]
y = dataset['price'].values
y = y.reshape(-1, 1)


X_train, X_test, y_train, y_test = train_test_split(X, y,test_size = 0.2, random_state=42)

#Convert the training and testing sets into DMatrixes
DM_train = xgb.DMatrix(data = X, 
                       label = y)  
DM_test =  xgb.DMatrix(data = X,
                       label = y)


gbm_param_grid = {
     'colsample_bytree': np.linspace(0.5, 0.9, 5),
     'n_estimators':[100, 200],
     'max_depth': [10, 15, 20, 25]
}

#Instantiate the regressor
gbm = xgb.XGBRegressor()

#Perform grid search
try:
    grid_model = pickle.load(open(path+filename, 'rb'))
    print('Gradient Boosting model XGBRegressor loaded from saved data file')
except:
    print('Calculating Gradient Boosting XGBRegressor')
    
    grid_model = GridSearchCV(estimator = gbm, param_grid = gbm_param_grid, scoring = 'neg_mean_squared_error', cv = 5, n_jobs= 4, verbose = 1)
    grid_model.fit(X, y)
    print("Best parameters found: ",grid_model.best_params_)
    print("Lowest RMSE found: ", np.sqrt(np.abs(grid_model.best_score_)))

    pickle.dump(grid_model, open(path+filename, 'wb'))
    print('Gradient Boosting XGBRegressor Model exported to: ', path+filename)
    save = True
print('======================================================')


#Predict using the test data
pred = grid_model.predict(X)

print("Root mean square error for test dataset: {}".format(np.round(np.sqrt(mean_squared_error(y, pred)), 2)))
mean = statistics.mean(y.flatten())
print('Mean: ', mean)
rmse = np.round(np.sqrt(mean_squared_error(y, pred)),2)
print('Root Mean Squared Error:', rmse)
perf = 1 - (rmse/mean)
perf = perf * 100
print('Performance ', perf, '%')

#test
test = pd.DataFrame({"prediction": pred, "observed": y.flatten()})
lowess = sm.nonparametric.lowess
z = lowess(pred.flatten(), y.flatten())
test.plot(figsize = [14,8],
          x ="prediction", y = "observed", kind = "scatter", color = 'darkred')
plt.title("Extreme Gradient Boosting: Prediction Vs Test Data", fontsize = 18, color = "darkgreen")
plt.xlabel("Predicted Power Output", fontsize = 18) 
plt.ylabel("Observed Power Output", fontsize = 18)
plt.plot(z[:,0], z[:,1], color = "blue", lw= 3)

if(save == True):
    plt.savefig(path+chartfilename)
    saveModelData(perf,rmse,grid_model.best_params_)
#plt.show() uncomment to show plot in screen

print()
print('Model Calculation Finished')
print()
print('======================================================')