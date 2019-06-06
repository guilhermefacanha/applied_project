'''
Created on Jun 6, 2019

@author: limafacanhag
'''

#python -m pip install pymongo - required module

import pymongo

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
        print('Connection to database completed!')
        pass
        
    def getOneProperty(self):
        return self.propertyCollection.find_one()
    
    def getAllProperties(self):
        allprop = []
        
        for x in self.propertyCollection.find():
            allprop.append(x)
        
        return allprop
    
    def getAllPropertiesWithQuery(self, query):
        props = []
        
        for x in self.propertyCollection.find(query):
            props.append(x)
        
        return props
    

#self test code
dao = PropertiesDao()
prop = dao.getOneProperty()
propAll = dao.getAllProperties()
propsQuery = dao.getAllPropertiesWithQuery({"price":{"$lte":2000}});

print(prop)
print('Number of properties: ', len(propAll))
print('Number of properties with price less than 2000: ', len(propsQuery))
