import nltk  # Load NLTK
import time
from collections import defaultdict
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

print('=====Property Location Update Textmining=====')


rows = dao.getAllPropertiesWithQuery({ "loc_vancouver":0,
            "loc_burnaby":0,
            "loc_richmond":0,
            "loc_surrey":0,
            "loc_newwest":0,
            "loc_abbotsford":0,
            "loc_other":0 });

print("Records: ", len(rows)) 

size = len(rows)
count = 0
result = defaultdict(list)

for property in rows:
    sentences = [] if 'fullDescription' not in property else propertyService.getSentences(str(property['fullDescription']))
    propertyService.setLocation(property, propertyService.getLocationFromSentences(sentences, property['link']))
    
    location = 'loc_vancouver' if property['loc_vancouver'] == 1 else 'loc_burnaby' if property['loc_burnaby'] else 'loc_richmond' if property['loc_richmond'] else 'loc_surrey' if property['loc_surrey'] else 'loc_newwest' if property['loc_newwest'] else 'loc_abbotsford' if property['loc_abbotsford'] else 'loc_other'
    print(property['_id'], ' - ',  location)
    if 'loc_vancouver' in property:
        newValues = { "$set": {
            "loc_vancouver":property['loc_vancouver'],
            "loc_burnaby":property['loc_burnaby'],
            "loc_richmond":property['loc_richmond'],
            "loc_surrey":property['loc_surrey'],
            "loc_newwest":property['loc_newwest'],
            "loc_abbotsford":property['loc_abbotsford'],
            "loc_other":property['loc_other']
            }}
        dao.updateProperty(property['_id'], newValues)
    
    count += 1
    try:
        if count % 300 == 0:
            percent = round((count / size * 100), 2);
            print('==========================================')
            print('PROGESS: ', count, '/', size, '(', percent, '%)')
            print('==========================================')
            time.sleep(1)
    except:
           print("error showing the progress")

print(count,' records processed.')
print('Properties location updated!')
