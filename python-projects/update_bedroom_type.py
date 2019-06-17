import nltk  # Load NLTK
import time

from text_mining_service.sklearn_service import SkLearnService
from text_mining_service.property_service import PropertyService
from db.propertiesdao import PropertiesDao
# nltk.download()

nltk.download('punkt')
nltk.download('stopwords')
nltk.download('wordnet')

dao = PropertiesDao();
propertyService = PropertyService()
sklearnService = SkLearnService()

rows = dao.getAllPropertiesWithQuery({ "bedrooms": {"$ne":None} });

print("Loaded Records: ", len(rows)) 
size = len(rows)


count = 0
for property in rows:
    print(property['_id'], ' - ', property['bedrooms'], 'bdr ')
    newvalues = { "$set": { "bedrooms": float(property['bedrooms']),"size_sqft": float(property['size_sqft'])  } }
    dao.updateProperty(property['_id'], newvalues);
    count += 1
    try:
        if count % 500 == 0:
            percent = round((count / size * 100), 2);
            print('==========================================')
            print('PROGESS: ', count, '/', size, '(', percent, '%)')
            print('==========================================')
            time.sleep(1)
    except:
           print("error showing the progress")
           
print('Finished process: ',size,' records')

