import pandas as pd
import numpy as np

from db.propertiesdao import PropertiesDao
from pred_models.simulator import Simulator
import json

#simulator = Simulator()
dao = PropertiesDao()

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
 
# Create DataFrame
record = pd.DataFrame(data)
record['size_sqft_lg'] = np.log10(record['size_sqft'])
 
# Print the output.
print(record.to_json(orient='index'))

"""
try:
    simulator.simulate(record)
    result = json.loads(record.to_json(orient='index'))
    
    print('Price: ',result['0']['price'])
    models = dao.getModelNames();
    for model in models:
        if model in record:
            print(model,': ',result['0'][model])
    
except:
    print('Error simulating models')
"""
    
print('======================================================')