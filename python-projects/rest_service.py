import json

from flask import Flask
from flask.globals import request
from flask_cors.extension import CORS

from db.propertiesdao import PropertiesDao
import pandas as pd
from simulator import Simulator
from bson import json_util
from flask.sessions import session_json_serializer
from db.DateTimeEncoder import DateTimeEncoder


#pip install flask_cors
#start the rest api application
app = Flask(__name__)
CORS(app) #apply cors for remote connection accept

@app.route('/')
def index():
    data = {}
    data['test'] = {"self": {"href": "/test"}}
    data['get_property'] = {"self": {"href": "/prop/{propid}"}}
    data['testpost'] = {"self": {"href": "/testpost"}}
    data['simulate'] = {"self": {"href": "/simulate"}}
    
    return json.dumps(data);

@app.route('/simulate', methods=['POST'])
def simulate():
    simulator = Simulator()
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