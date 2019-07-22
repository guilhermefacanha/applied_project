#run with python -m pred_models.ols_regression

import pickle
import statistics
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import statsmodels.api as sm
import datetime
import xgboost as xg

from sklearn import metrics
from bson.objectid import ObjectId
from db.propertiesdao import PropertiesDao

def saveModelData(performance, mean_error, summary=''):
    data = {};
    data['_id'] = str(ObjectId())
    data['model'] = 'DMatrix Regression'
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
filename = 'reg_dmatrix_model_'+dtstamp+'.sav'
chartfilename = 'reg_dmatrix_model_pred_chart_'+dtstamp+'.png'

save = False


# Load data
dataset = dao.getDataSetModelPd()
print('Dataset Acquired: (',len(dataset),')')

# define the data/predictors as the pre-set feature names
df = dataset[dao.getDataSetModelNames()]

# Put the target (housing value -- MEDV) in another DataFrame
target = dataset[['price']]

X = df
y = target["price"]

# Note the difference in argument order
print('======================================================')
try:
    model = pickle.load(open(path+filename, 'rb'))
    print('Regression DMatrix model loaded from saved data file')
except:
    print('Calculating DMatrix Regression Model')
    params = {'max_depth':3,
    'min_child_weight':10,
    'learning_rate':0.3,
    'subsample':0.5,
    'colsample_bytree':0.6,
    'obj':'reg:squarederror',
    'n_estimators':1000,
    'eta':0.3}
    dmatrix = xg.DMatrix(X, target.values, feature_names=dao.getDataSetModelNames())
    model = xg.train(params, dmatrix)
    pickle.dump(model, open(path+filename, 'wb'))
    print('Regression Model DMatrix exported to: ', path+filename)
    save = True
print('======================================================')

dtest = xg.DMatrix(X, label='')
predictions = model.predict(dtest)  # make the predictions by the model

# Print out the statistics
print('Mean: ', statistics.mean(y))
print('Mean Absolute Error:', metrics.mean_absolute_error(y, predictions))  
print('Mean Squared Error:', metrics.mean_squared_error(y, predictions))
rmse = np.sqrt(metrics.mean_squared_error(y, predictions))
print('Root Mean Squared Error:', rmse)
perf = 1 - (np.sqrt(metrics.mean_squared_error(y, predictions)) / statistics.mean(y))
perf = perf * 100
print('Performance ', perf, '%')

# #############################################################################
# Plot
test = pd.DataFrame({"prediction": predictions, "observed": y})
lowess = sm.nonparametric.lowess
z = lowess(predictions.flatten(), y)
test.plot(figsize = [14,8],
          x ="prediction", y = "observed", kind = "scatter", color = 'darkred')
plt.title("Extreme Gradient Boosting: Prediction Vs Test Data", fontsize = 18, color = "darkgreen")
plt.xlabel("Predicted Power Output", fontsize = 18) 
plt.ylabel("Observed Power Output", fontsize = 18)
plt.plot(z[:,0], z[:,1], color = "blue", lw= 3)

if(save == True):
    plt.savefig(path+chartfilename)
    saveModelData(perf,rmse)

print()
print('Model DMatrix Calculation Finished')
print()
print('======================================================')


