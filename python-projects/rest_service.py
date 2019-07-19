import json

from flask import Flask
from flask.globals import request
from flask.sessions import session_json_serializer
from flask_cors.extension import CORS

from db.DateTimeEncoder import DateTimeEncoder
from db.propertiesdao import PropertiesDao
import pandas as pd
from pred_models.simulator import Simulator


#pip install flask
#pip install flask_cors
#start the rest api application
app = Flask(__name__)
CORS(app) #apply cors for remote connection accept
simulator = Simulator()

@app.route('/')
def index():
    data = {}
    data['test'] = {"self": {"href": "/test"}}
    data['get_property'] = {"self": {"href": "/prop/{propid}"}}
    data['testpost'] = {"self": {"href": "/testpost"}}
    data['simulate'] = {"self": {"href": "/simulate"}}
    data['simulate_test'] = {"self": {"href": "/simulate_test"}}
    
    return json.dumps(data);

@app.route('/simulate_test', methods=['GET'])
def simulatetest():
    # intialise data of lists.
    data = {'price':[2400.0],
            'bedrooms':[3.0],
            'bath':[3.0],
            'size_sqft':[1479.0],
            'professionally_managed':[0.0],
            'no_pet_allowed':[1.0],
            'suit_laundry':[1.0],
            'park_stall':[1.0], 
            'available_now':[0.0], 
            'furnished':[1.0], 
            'amenities':[0.0], 
            'brand_new':[0.0],
            'loc_vancouver':[1.0], 
            'loc_burnaby':[0.0], 
            'loc_richmond':[0.0], 
            'loc_surrey':[1.0], 
            'loc_newwest':[0.0], 
            'loc_abbotsford':[0.0], 
            'loc_other':[0.0], 
            'no_basement':[1.0]
            }
    
    record = pd.DataFrame(data)
    record = simulator.simulate(record)
    return record.to_json(orient='index')
    
@app.route('/simulate', methods=['POST'])
def simulate():
    req_data = request.get_json()
    data = {'price':[req_data['price']],
        'bedrooms':[req_data['bedrooms']],
        'bath':[req_data['bath']],
        'size_sqft':[req_data['size_sqft']],
        'professionally_managed':[req_data['professionally_managed']],
        'no_pet_allowed':[req_data['no_pet_allowed']],
        'suit_laundry':[req_data['suit_laundry']],
        'park_stall':[req_data['park_stall']], 
        'available_now':[req_data['available_now']], 
        'amenities':[req_data['amenities']], 
        'brand_new':[req_data['brand_new']],
        'loc_vancouver':[req_data['loc_vancouver']], 
        'loc_burnaby':[req_data['loc_burnaby']], 
        'loc_richmond':[req_data['loc_richmond']], 
        'loc_surrey':[req_data['loc_surrey']], 
        'loc_newwest':[req_data['loc_newwest']], 
        'loc_abbotsford':[req_data['loc_abbotsford']], 
        'loc_other':[req_data['loc_other']], 
        'furnished':[req_data['furnished']], 
        'no_basement':[req_data['no_basement']]
        }
    
    # Create DataFrame
    record = pd.DataFrame(data)
    
    record = simulator.simulate(record)
    
    return record.to_json(orient='index')

@app.route('/test')
def test():
    return PropertiesDao().getOnePropertyJson()

@app.route('/prop/<string:prop_id>')
def getProperty(prop_id):
    try:
        return PropertiesDao().getOnePropertyJson()
    except:
        return 'No record found!'

if __name__ == '__main__':
    app.run(debug=False,host='0.0.0.0')