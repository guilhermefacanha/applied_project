'''
Created on Jun 6, 2019

@author: limafacanhag
'''

#required module
#python -m pip install pymongo 

import pymongo
import json
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
    
    def getDataSetModel(self, limit = 0):
        # bedrooms > 0 and bedrooms < 5 and size_sqft < 5000 and price < 6000 and bath < 5
        query = {"bedrooms":{"$gt":0},"price":{"$gt":0},"price":{"$lt":8000},"bath":{"$ne":None},"size_sqft":{"$ne":None}}
        if limit > 0:
            return self.propertyCollection.find(query).limit(limit)
        else:
            return self.propertyCollection.find(query); 
    
    def updateProperty(self, _id, newValues):
        myquery = { "_id": _id }
        self.propertyCollection.update_one(myquery, newValues)
    
    def saveModel(self, data):
        self.modelCollection.insert(data)

    
    def getLastModel(self, model):
        return self.modelCollection.find({'model':model}).limit(1).sort({'date':-1})
    
    

#self test code
#dao = PropertiesDao()
#dao.testClass()
