#run with python -m pred_models.ols_regression

import pickle
import statistics
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import statsmodels.api as sm
import datetime

from sklearn import metrics
from bson.objectid import ObjectId
from db.propertiesdao import PropertiesDao

def saveModelData(performance, mean_error, summary):
    data = {};
    data['_id'] = str(ObjectId())
    data['model'] = 'OLS Regression'
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
filename = 'reg_ols_model_'+dtstamp+'.sav'
chartfilename = 'reg_ols_model_pred_chart_'+dtstamp+'.png'

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
    print('Regression model loaded from saved data file')
except:
    print('Calculating regression model')
    model = sm.OLS(y, X).fit()
    pickle.dump(model, open(path+filename, 'wb'))
    print('Regression Model exported to: ', path+filename)
    save = True
print('======================================================')

predictions = model.predict(X)  # make the predictions by the model

#save predictions to csv uncomment if needed
#dataset['prediction'] = predictions;
#dataset.to_csv('../data/result.csv')

# Print out the statistics
summary = model.summary()
print(summary)
print(model.conf_int())
print('Mean: ', statistics.mean(y))
print('Mean Absolute Error:', metrics.mean_absolute_error(y, predictions))  
print('Mean Squared Error:', metrics.mean_squared_error(y, predictions))
mean_error = np.sqrt(metrics.mean_squared_error(y, predictions))
print('Root Mean Squared Error:', mean_error)
perf = 1 - (np.sqrt(metrics.mean_squared_error(y, predictions)) / statistics.mean(y))
perf = perf * 100
print('Performance ', perf, '%')

# #############################################################################
# Plot
test = pd.DataFrame({"prediction": predictions, "observed": y})
lowess = sm.nonparametric.lowess
z = lowess(predictions.values.flatten(), y)
test.plot(figsize = [14,8],
          x ="prediction", y = "observed", kind = "scatter", color = 'darkred')
plt.title("Regression OLS Model: Prediction Vs Test Data", fontsize = 18, color = "darkgreen")
plt.xlabel("Predicted Power Output", fontsize = 18) 
plt.ylabel("Observed Power Output", fontsize = 18)
plt.plot(z[:,0], z[:,1], color = "blue", lw= 3)

if(save == True):
    plt.savefig(path+chartfilename)
    saveModelData(perf,mean_error,str(summary))

print()
print('Model Calculation Finished')
print()
print('======================================================')


