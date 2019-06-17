import re
from text_mining_service.sklearn_service import SkLearnService


class PropertyService:
    
    def populateRoomSize(self, property):
        property['bedrooms']=0
        property['size_sqft']=0
        sp = property['characteristics'].split('-')
        for s in sp:
            if s is not None:
                s = str(s)
                if 'br' in s:
                    rooms = s.replace('br', '').strip()
                    property['bedrooms']=float(rooms)
                    property['update'] = True
                elif 'ft_sq' in s:
                    size = s.replace('ft_sq', '').strip()
                    property['size_sqft']=float(size)
                    property['update'] = True
        
        return property;
    
    def setToken(self,property,token):
        if token == 'professionally managed':
            property['professionally_managed'] = 1
        elif token == 'no pets':
            property['no_pet_allowed'] = 1
        elif token == 'suite laundry':
            property['suit_laundry'] = 1
        elif token == 'parking stall':
            property['park_stall'] = 1
        elif token == 'available now':
            property['available_now'] = 1
        elif token == 'building amenities':
            property['amenities'] = 1
        elif token == 'near school':
            property['near_school'] = 1
        elif token == 'brand new':
            property['brand_new'] = 1
        elif token == 'furnished':
            property['furnished'] = 1
    
    def populateTokens(self, property, sentences):
        property['professionally_managed'] = 0
        property['no_pet_allowed'] = 0
        property['suit_laundry'] = 0
        property['park_stall'] = 0
        property['available_now'] = 0
        property['amenities'] = 0
        property['near_school'] = 0
        property['brand_new'] = 0
        property['furnished'] = 0
        
        searchfor = ['professionally managed', 'no pets', 'parking stall', 'available now', 'building amenities', 'near school', 'brand new', 'suite laundry','furnished']
        if sentences is not None:
            for sentence in sentences:
                if sentence in searchfor:
                    self.setToken(property,sentence)
                    property['update']=True
                 
        return property

    def getLocationFromSentences(self, sentences, link):
        link = link.replace('https://vancouver.craigslist.org/','') 
        locations = ['burnaby', 'new westminster', 'richmond' ,'stanley park', 'surrey', 'vancouver', 'downtown', 'abbotsford', 'granville', 'brentwood','delta','langley','white-rock','coquitlam','new-westminster','fleetwood','port-moody']
        
        for location in locations:
            if location in link:
                return location
        
        for location in locations:
            for sentence in sentences:
                if location in sentence:
                    return location;
        return '';
    
    def setLocation(self,property,location):
        property['loc_vancouver'] = 0
        property['loc_newwest'] = 0
        property['loc_burnaby'] = 0
        property['loc_richmond'] = 0
        property['loc_surrey'] = 0
        property['loc_abbotsford'] = 0
        
        if('vancouver' in location or location == 'stanley park' or location == 'downtown' or location == 'granville'):
            property['loc_vancouver'] = 1
        elif location == 'new-westminster':
            property['loc_newwest'] = 1
        elif location == 'burnaby':
            property['loc_burnaby'] = 1
        elif location == 'surrey':
            property['loc_surrey'] = 1
        elif location == 'richmond':
            property['loc_richmond'] = 1
        elif location == 'abbotsford':
            property['loc_abbotsford'] = 1
            
        self.location = location
    
    def getSentences(self, description):
        sklearnService = SkLearnService()
        
        description = sklearnService.removeSpecialCharacters(description)
        description = description.lower().strip()
        
        sentences = sklearnService.getSentenceFromText(description, 2, 3);
        sentences = sklearnService.filterSameMeaning(sentences)
        
        return sentences;

    # method to extract number of bedrooms from text
    def tryGetBedroomFromDescription(self, property):
        numbers = ['one', 'two', 'three', 'four', 'five']  # writed numbers to be found
        exact = ['1bd', '2bd', '3bd', '4bd', '5bd']  # numbers with bd together pattern
        exact2 = ['1-br', '2-br', '3-br', '4-br', '5-br']  # numbers with bd together pattern
        exact3 = ['1bed', '2bed', '3bed', '4bed', '5bed']  # numbers with bd together pattern
        
        property['update'] = False
        desc = str(property['title']).lower() + ' ' + str(property['fullDescription']).lower() if 'fullDescription' in property else str(property['title']).lower()  # parse the texto to lowercase
        words = desc.split(' ');
        for i in range(0, len(words)):
            if words[i] in exact:  # check for a specific pattern in text and return the exact number of rooms from array position
                r = exact.index(words[i]) + 1
                property['bedrooms'] = r
                property['update'] = True
                return property;
           
            if words[i] in exact2:  # check for a specific pattern in text and return the exact number of rooms from array position
                r = exact2.index(words[i]) + 1
                property['bedrooms'] = r
                property['update'] = True
                return property;

            if words[i] in exact3:  # check for a specific pattern in text and return the exact number of rooms from array position
                r = exact3.index(words[i]) + 1
                property['bedrooms'] = r
                property['update'] = True
                return property;
            
            # check for a split pattern in text and apply algorithm to identify the number
            if words[i] in ['bedroom', 'bedrooms', 'bed', 'br', 'brm', 'bdrm', 'bdr'] or 'bed' in words[i] or 'bd' in words[i]:
                lw = str(words[i - 1]).strip()
                if(lw in numbers):
                    lw = numbers.index(lw) + 1
                r = -1
                try:
                    r = int(lw)
                except:
                    r = -1
                if r == -1:
                    try:
                        r = float(lw)
                    except:
                        r = -1
                if r >= 0 and r <= 7 :  # if the number is too high probably is not right
                    property['bedrooms'] = r
                    property['update'] = True
                    return property;
            
            if 'studio' in words or 'bachelor ' in desc or 'bachlor ' in desc:
                property['bedrooms'] = 1
                property['update'] = True
                return property;
            
        find = re.search("\d{1,5}[b][d]", desc)
        if find:
            size = str(find.group()).replace('bd', '').strip()
            r = -1
            try:
                r = float(size)
                if r >= 0 and r <= 7 :
                    property['bedrooms'] = r
                    property['update'] = True
                    return property;
                    
            except:
                r = -1
                
        return property;
    
    def tryGetSizeFromDescription(self, property):
        
        desc = str(property.characteristics).lower() + ' ' + str(property.title).lower() + ' ' + str(property.description).lower()  # parse the texto to lowercase
        words = desc.split(' ');
        for i in range(0, len(words)):
            
            # check for a split pattern in text and apply algorithm to identify the number
            if words[i] in ['square', 'sqft', 'sq.', 'sqt', 'sqf', 'sqft)', 'sq.ft.', 'sqft.', 'sq', 'sqft,', 'sf', 'sq.ft' , 'sq.ft.,' , 'sqft).', 'sq/ft', 'sq.ft'] :
                lw = str(words[i - 1]).strip().replace('+', '').replace(',', '')
                r = -1
                try:
                    r = int(lw)
                except:
                    r = -1
                if r == -1:
                    try:
                        r = float(lw)
                    except:
                        r = -1
                if r >= 100 :
                    property.setSize(r)
                    property.setUpdate()
                    return property;
                
            if words[i] in ['sq/ft:', 'footage:'] :
                lw = str(words[i + 1]).strip().replace('+', '').replace(',', '')
                r = -1
                try:
                    r = int(lw)
                except:
                    r = -1
                if r == -1:
                    try:
                        r = float(lw)
                    except:
                        r = -1
                if r >= 100 :
                    property.setSize(r)
                    property.setUpdate()
                    return property;
        
        find = re.search("\d{1,5}.sq", desc)
        if not find: 
            find = re.search("\d{1,5}.ft", desc)
        if find:
            size = str(find.group()).replace('sq', '').replace('ft', '').strip()
            r = -1
            try:
                r = float(size)
                if r >= 100 :
                    property.setSize(r)
                    property.setUpdate()
                    return property;
                    
            except:
                r = -1
                
        return property;
    
        numbers = ['one', 'two', 'three', 'four', 'five']  # writed numbers to be found
        desc = str(property.characteristics).lower() + ' ' + str(property.title).lower() + ' ' + str(property.description).lower()  # parse the texto to lowercase
        desc = desc.replace('\\xc2',' ').replace('\\xa0',' ') #clean dirty
        desc = desc.replace('&nbsp;',' ').replace('+', ' ').replace('/', ' ').replace('-', ' ') #clean dirty
        desc = desc.replace('full','').replace('private','').replace('  ',' ') #replace word full there is found between the bathroom word and number
        words = desc.split(' ');
        wordContains = ['bath','bths']
        
        if 'one and half ba' in desc:
            property.setBath(1.5)
            property.setUpdate()
            return property;
        
        find = re.search("\d{1,5}ba|\d[.]\d{1,5}ba", desc)
        if find:
            bath = str(find.group()).replace('ba', '').strip()
            r = -1
            try:
                r = float(bath)
                if r > 0 and  r <= 7 :
                    property.setBath(r)
                    property.setUpdate()
                    return property;
            except:
                pass
        
        # check for a split pattern in text and apply algorithm to identify the number
        for i in range(0, len(words)):
            #check if word has exact pattern to search for number in next word            
            if words[i] in ['bath:','bathroom:', 'bathrooms:', 'bathroom(s):'] :
                lw = str(words[i + 1]).strip().replace('+', '').replace(',', '')
                if(lw in numbers):
                    lw = numbers.index(lw) + 1
                r = -1
                try:
                    r = float(lw)
                    if r > 0 and  r <= 7 :
                        property.setBath(r)
                        property.setUpdate()
                        return property;
                except:
                    pass
            
            #check if word contains pattern to search for number in previous word            
            if any(w in words[i] for w in wordContains ) :
                lw = str(words[i - 1]).strip().replace('+', '').replace(',', '.').replace('/', '')
                if(lw in numbers):
                    lw = numbers.index(lw) + 1
                r = -1
                try:
                    r = float(lw)
                    if r > 0 and  r <= 7 :
                        property.setBath(r)
                        property.setUpdate()
                        return property;
                except:
                    pass
                
        if 'bath' in desc:
            property.setBath(1)
            property.setUpdate()
            return property;
                
        return property;
        
