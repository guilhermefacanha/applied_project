'''
Created on Jun 6, 2019

@author: limafacanhag
'''

#required module
#python -m pip install pymongo 

import pymongo
import json

import pandas as pd
import numpy as np

from bson.objectid import ObjectId
from db.DateTimeEncoder import DateTimeEncoder

class PropertiesDao(object):
    '''
    classdocs
    '''
    
    
    def __init__(self):
        '''
        Constructor
        '''
        print('Creating instance of PropertiesDao')
        self.client = pymongo.MongoClient("mongodb://adminuser:admin123@ds149365.mlab.com:49365/rent-analysis")
        self.db = self.client["rent-analysis"]
        self.propertyCollection = self.db["property"]
        self.modelCollection = self.db["pred_model"]
        print('Connection to database completed!')
        pass
    
    def testClass(self):
        print('Init class test')
        prop = self.getOneProperty()
        propAll = self.getAllProperties()
        propsQuery = self.getAllPropertiesWithQuery({"price":{"$lte":2000}});
        
        print(prop)
        print('Number of properties: ', len(propAll))
        print('Number of properties with price less than 2000: ', len(propsQuery))
        print('Class test complete')

    def getOneProperty(self):
        return self.propertyCollection.find_one()
    
    def getOnePropertyJson(self):
        return json.dumps(self.getOneProperty(), cls=DateTimeEncoder)
    
    def getAllProperties(self, limit=0):
        allprop = []
        if limit > 0:
            for x in self.propertyCollection.find().limit(limit):
                allprop.append(x)
        else:
            for x in self.propertyCollection.find():
                allprop.append(x)
            
        return allprop
    
    def getAllPropertiesWithQuery(self, query):
        props = []
        
        for x in self.propertyCollection.find(query):
            props.append(x)
        
        return props

    def getModelNames(self):
        return self.modelCollection.distinct('model');
    
    def getDataSetModelNames(self):
        return ['bedrooms', 'bath', 'size_sqft_lg', 'professionally_managed', 'no_pet_allowed', 'suit_laundry', 'park_stall', 'available_now', 'furnished', 'amenities', 'brand_new','loc_vancouver', 'loc_burnaby', 'loc_richmond', 'loc_surrey', 'loc_newwest', 'loc_abbotsford', 'loc_other','no_basement']
    
    def getDataSetModel(self, limit = 0, bdrm = 0):
        # bedrooms > 0 and bedrooms < 5 and size_sqft < 5000 and price < 6000 and bath < 5
        if bdrm > 0:
            query = {"bedrooms":bdrm,"price":{"$gt":900,"$lt":6000},"bath":{"$gt":0,"$lt":6},"size_sqft":{"$gt":0}}
        else:
            query = {"bedrooms":{"$gt":0,"$lt":6},"price":{"$gt":900,"$lt":6000},"bath":{"$gt":0,"$lt":6},"size_sqft":{"$gt":0}}
            
        if limit > 0:
            return self.propertyCollection.find(query).limit(limit)
        else:
            return self.propertyCollection.find(query);
    
    def getDataSetModelPd(self, limit = 0, bdrm = 0):
        dataset = self.getDataSetModel(limit=limit, bdrm=bdrm)
        dataset = pd.DataFrame.from_dict(dataset)
        dataset['size_sqft_lg'] = np.log10(dataset['size_sqft'])
        return dataset
    
    def updateProperty(self, _id, newValues):
        myquery = { "_id": _id }
        self.propertyCollection.update_one(myquery, newValues)
    
    def saveModel(self, data):
        self.modelCollection.insert(data)
    

    
    def getLastModel(self, model):
        query = {'model':model};
        cursor = self.modelCollection.find(query).sort('date',-1).limit(1)
        return cursor[0] if cursor.count() > 0 else None
    
    

#self test code
#dao = PropertiesDao()
#dao.testClass()
