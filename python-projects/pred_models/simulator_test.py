import pickle
import pandas as pd
from pred_models.simulator import Simulator

filename_reg = 'data/reg_model.sav'
filename_grad = 'data/grad_model.sav'
filename_grad_2 = 'data/grad_2_model.sav'

simulator = Simulator() 

# intialise data of lists.
data = {'price':[2900.0],
        'bedrooms':[2.0],
        'bath':[2.0],
        'size_sqft':[750.0],
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
        'loc_surrey':[0.0], 
        'loc_newwest':[0.0], 
        'loc_abbotsford':[0.0], 
        'loc_other':[0.0], 
        'no_basement':[1.0]
        }
 
# Create DataFrame
record = pd.DataFrame(data)
 
# Print the output.
print(record)

try:
    simulator.simulate(record)
    
    print("Result Record: ",record)
    
except:
    print('Error loading models found at ',filename_reg)
print('======================================================')