import nltk  # Load NLTK
import time

from db.propertiesdao import PropertiesDao
from text_mining_service.property_service import PropertyService
from text_mining_service.sklearn_service import SkLearnService

# nltk.download()
nltk.download('punkt')
nltk.download('stopwords')
nltk.download('wordnet')


dao = PropertiesDao()
propertyService = PropertyService()
sklearnService = SkLearnService()

print('=====Property Fields Update Textmining=====')

#rows = dao.getAllProperties(1000)
rows = dao.getAllPropertiesWithQuery({"$or":[{"bedrooms":None},{"bedrooms":0}]});
print("Loaded Records: ", len(rows)) 

size = len(rows)
count = 0
for property in rows:
    
    #get number of bedrooms and size from characteristics field
    property = propertyService.populateRoomSize(property)
    
    #get number of bedrooms from description text
    if property['bedrooms'] == 0:
        property = propertyService.tryGetBedroomFromDescription(property)
    
    #get number of bedrooms from description text
    if property['size_sqft'] == 0:
        property = propertyService.tryGetSizeFromDescription(property)
    
    #get number of bathrooms out of advertisement
    property = propertyService.tryGetBathFromDescription(property) 
        
    sentences = [] if 'fullDescription' not in property else propertyService.getSentences(str(property['fullDescription']))
    property = propertyService.populateTokens(property, sentences)
    property = propertyService.verifyBasement(property)
    
    if property['bedrooms'] == 0 and property['no_basement'] == 0:
            property['bedrooms'] = 1;
    
    if('update' in property and property['update']==True):
        print(property['_id'], ' - ', property['bedrooms'], 'bdr ', property['size_sqft'], 'sqft ')
        newValues = { "$set": {
            "bedrooms":property['bedrooms'],
            "size_sqft":property['size_sqft'],
            "bath":property['bath'],
            "professionally_managed":property['professionally_managed'],
            "no_pet_allowed":property['no_pet_allowed'],
            "suit_laundry":property['suit_laundry'],
            "park_stall":property['park_stall'],
            "available_now":property['available_now'],
            "amenities":property['amenities'],
            "near_school":property['near_school'],
            "brand_new":property['brand_new'],
            "no_basement":property['no_basement'],
            "furnished":property['furnished'],
            "no_basement": property['no_basement'],
            "bsmt_analyzed": 1 
            }}
        dao.updateProperty(property['_id'], newValues)
    count += 1
    try:
        if count % 100 == 0:
            percent = round((count / size * 100), 2);
            print('==========================================')
            print('PROGESS: ', count, '/', size, '(', percent, '%)')
            print('==========================================')
            time.sleep(3)
    except:
           print("error showing the progress")

print('Finished process: ',size,' records')
