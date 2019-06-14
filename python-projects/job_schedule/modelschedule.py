'''
Created on Jun 7, 2019

@author: limafacanhag
'''

#required modules
#pip install schedule
import schedule
import time

from db.propertiesdao import PropertiesDao
dao = PropertiesDao()

def job():
    print("I'm working...")
    print(dao.getOneProperty())


schedule.every(1).minutes.do(job)
schedule.every(10).minutes.do(job)
schedule.every().hour.do(job)
schedule.every().day.at("10:30").do(job)

while 1:
    schedule.run_pending()
    time.sleep(1)