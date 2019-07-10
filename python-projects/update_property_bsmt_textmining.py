import time
from db.propertiesdao import PropertiesDao
from text_mining_service.property_service import PropertyService
from text_mining_service.sklearn_service import SkLearnService


dao = PropertiesDao()
propertyService = PropertyService()
sklearnService = SkLearnService()

rows =  dao.getAllPropertiesWithQuery({"bedrooms":{"$lt":3},"$or":[{"bsmt_analyzed":None},{"bsmt_analyzed":0}]});

print("Loaded Records: ", len(rows)) 

size = len(rows)
count = 0
for property in rows:
    property['update'] = False;
    property = propertyService.verifyBasement(property) 
    if property['update'] == True:
        print(property['_id'], ' - bedrooms:',property['bedrooms'],' - update basement')
        if property['bedrooms'] == 0:
            property['bedrooms'] = 1;
        newvalues = { "$set": 
                     {
                         "bedrooms": property['bedrooms'],
                         "no_basement": property['no_basement'],
                         "bsmt_analyzed": 1 
                      } 
                     }
        #dao.updateProperty(property['_id'], newvalues);
    count += 1
    try:
        if count % 300 == 0:
            percent = round((count / size * 100), 2);
            print('==========================================')
            print('PROGESS: ', count, '/', size, '(', percent, '%)')
            print('==========================================')
            time.sleep(3)
    except:
           print("error showing the progress")
           
print('Finished process: ',size,' records')

