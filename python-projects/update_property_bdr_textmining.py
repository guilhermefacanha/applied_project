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

rows = dao.getAllPropertiesWithQuery({ "bedrooms": None });

print("Loaded Records: ", len(rows)) 
size = len(rows)


count = 0
for property in rows:
    property = propertyService.tryGetBedroomFromDescription(property)
    if property['update'] == True:
        print(property['_id'], ' - ', property['bedrooms'], 'bdr ')
        newvalues = { "$set": { "bedrooms": property['bedrooms'] } }
        dao.updateProperty(property['_id'], newvalues);
    count += 1
    try:
        if count % 50 == 0:
            percent = round((count / size * 100), 2);
            print('==========================================')
            print('PROGESS: ', count, '/', size, '(', percent, '%)')
            print('==========================================')
            time.sleep(3)
    except:
           print("error showing the progress")
           
print('Finished process: ',size,' records')

