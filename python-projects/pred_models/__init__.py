from bson.objectid import ObjectId
import datetime
data = {};
data['_id'] = str(ObjectId())
data['model'] = 'GradientBoostingRegressor'
data['file'] = 'filename'
data['chart'] = 'chartfilename'
data['date'] = datetime.datetime.utcnow()

print(data)