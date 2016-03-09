import MySQLdb
import csv
from config import *

def saveFromFanDB():
	db = MySQLdb.connect(configs_db["host"],configs_db["user"],configs_db["password"],configs_db["database"])
	cursor = db.cursor()

	condition_fan="SELECT Status,Humidity,Temperature FROM Fan ORDER BY Time DESC"
	cursor.execute(condition_fan)
	rows = cursor.fetchall()
	fp = open('dataset.csv', 'w')
	myFile = csv.writer(fp)
	myFile.writerows(rows)
	fp.close()
	print "Inside saveFromFanDB"
	return '1'
