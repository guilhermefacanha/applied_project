'''
Created on Jun 7, 2019
@author: limafacanhag
'''

# required modules
# pip install schedule
import schedule
import time
from _datetime import datetime, timedelta


def job_test():
    print("Scheduler Working")

    
def job_text_mining():
    print('=====Starting Text Mining=====')
    exec(open('../update_property_textmining.py').read())
    exec(open('../update_property_location_textmining.py').read())
    print('===== Text Mining Finished=====')


now = datetime.now() + timedelta(minutes=2);
min =  now.strftime(":%M")

print('==== ', now.strftime("%d/%m/%Y %H:%M:%S"), ' ====')
print('==== Scheduling jobs ====')

schedule.every(1).minutes.do(job_test)
print('>>Test every 1 min')
# schedule.every(10).minutes.do(job_test)
schedule.every().hour.at(min).do(job_text_mining)
print('>>Text Mining every 1 hour in minute ',min)
# schedule.every().day.at("10:30").do(job_test)
print('==== Jobs Scheduled ====')

while 1:
    schedule.run_pending()
    time.sleep(1)
    
