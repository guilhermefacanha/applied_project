'''
Created on Jun 6, 2019

@author: limafacanhag
'''

#required module
#python -m pip install pymongo 

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
#dao = PropertiesDao()
#dao.testClass()
