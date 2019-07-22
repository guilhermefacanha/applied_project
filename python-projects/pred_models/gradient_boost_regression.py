#run with python -m pred_models.gradient_boost_regression.py

import numpy as np
import statistics
import matplotlib.pyplot as plt
import pandas as pd
import statsmodels.api as sm
import pickle
import datetime

from sklearn import ensemble
from sklearn.metrics import mean_squared_error
from db.propertiesdao import PropertiesDao
from bson.objectid import ObjectId

def saveModelData(performance, mean_error, summary = ''):
    data = {};
    data['_id'] = str(ObjectId())
    data['model'] = 'GradientBoostingRegressor'
    data['file'] = filename
    data['chart'] = chartfilename
    data['chart2'] = chartfilename2
    data['chart3'] = chartfilename3
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
filename = 'grad_boost_model_'+dtstamp+'.sav'
chartfilename = 'grad_boost_model_pred_chart_'+dtstamp+'.png'
chartfilename2 = 'grad_boost_model_dev_chart_'+dtstamp+'.png'
chartfilename3 = 'grad_boost_model_importance_chart_'+dtstamp+'.png'

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
y_train = y.values
X_train = X
y_test = y.values
X_test = X

params = {'n_estimators': 700, 'max_depth': 20, 'min_samples_split': 5,
          'learning_rate': 0.01, 'loss': 'ls'}

# #############################################################################
# Fit regression model
print('======================================================')
try:
    model = pickle.load(open(path+filename, 'rb'))
    print('Gradient Boosting model loaded from saved data file')
except:
    print('Calculating Gradient Boosting')
    model = ensemble.GradientBoostingRegressor(**params)
    
    model.fit(X_train, y_train)
    pickle.dump(model, open(path+filename, 'wb'))
    print('Gradient Boosting Model exported to: ', path+filename)
    save = True
    print('Gradient Boosting Model data saved in database')
    

print('======================================================')

pred = model.predict(X_test)

mean = statistics.mean(y_test.flatten())
print('Mean: ', mean)
mse = mean_squared_error(y_test, pred)
print("MSE: %.4f" % mse)
rmse = np.round(np.sqrt(mse),2)
print('Root Mean Squared Error:', rmse)
perf = 1 - (rmse/mean)
perf = perf * 100
print('Performance ', perf, '%')

# #############################################################################
# Plot training deviance

# compute test set deviance
test_score = np.zeros((params['n_estimators'],), dtype=np.float64)

for i, y_pred in enumerate(model.staged_predict(X_test)):
    test_score[i] = model.loss_(y_test, y_pred)

plt.figure(figsize=(12, 6))
plt.subplot(1, 2, 1)
plt.title('Deviance')
plt.plot(np.arange(params['n_estimators']) + 1, model.train_score_, 'b-', label='Training Set Deviance')
plt.plot(np.arange(params['n_estimators']) + 1, test_score, 'r-', label='Test Set Deviance')
plt.legend(loc='upper right')
plt.xlabel('Boosting Iterations')
plt.ylabel('Deviance')
plt.savefig(path+chartfilename2)
plt.cla()
plt.clf()

# #############################################################################
# Plot feature importance
feature_importance = model.feature_importances_
Z = [x for _,x in sorted(zip(feature_importance,dao.getDataSetModelNames()))]
# make importances relative to max importance
feature_importance = 100.0 * (feature_importance / feature_importance.max())
sorted_idx = np.argsort(feature_importance)
pos = np.arange(sorted_idx.shape[0]) + .5
plt.subplot(1, 2, 2)
plt.barh(pos, feature_importance[sorted_idx], align='center')
plt.yticks(pos, Z)
plt.xlabel('Relative Importance')
plt.title('Variable Importance')
plt.savefig(path+chartfilename3)

# #############################################################################
# Plot
test = pd.DataFrame({"prediction": pred, "observed": y})
lowess = sm.nonparametric.lowess
z = lowess(pred.flatten(), y)
test.plot(figsize = [14,8],
          x ="prediction", y = "observed", kind = "scatter", color = 'darkred')
plt.title("Extreme Gradient Boosting: Prediction Vs Test Data", fontsize = 18, color = "darkgreen")
plt.xlabel("Predicted Power Output", fontsize = 18) 
plt.ylabel("Observed Power Output", fontsize = 18)
plt.plot(z[:,0], z[:,1], color = "blue", lw= 3)

if(save == True):
    plt.savefig(path+chartfilename)
    saveModelData(perf,rmse)
#plt.show() uncomment to show plot in screen

print()
print('Model Calculation Finished')
print()
print('======================================================')
