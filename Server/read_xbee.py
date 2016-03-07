import time
import serial
import MySQLdb
from config import *
from gcm import GCM
from operator import eq
ser = serial.Serial(              
	port='/dev/ttyAMA0',
	baudrate = 9600,
	parity=serial.PARITY_NONE,
	stopbits=serial.STOPBITS_ONE,
	bytesize=serial.EIGHTBITS,
	timeout=1
)

db = MySQLdb.connect(configs_db["host"],configs_db["user"],configs_db["password"],configs_db["database"])
cursor = db.cursor()

while(1):
	flag_z=0
	flag_u1=0
	flag_u2=0
	flag_y=0
	humidity=""
	temperature=""
	co2emission=""
	
	data_xbee=ser.readline()
	print data_xbee
	
    	if(data_xbee.strip()=='Y'):
		flag_y=1
        	#print "Inside Y"
        	while(flag_u1==0):
            		#print "Reading humidity"
            		data_xbee=ser.readline()
            		#print data_xbee
            		if(data_xbee.strip()=="U"):
            		    flag_u1=1
            		else:
            		    humidity+=data_xbee
        	while(flag_u2==0):
            		#print "Reading temperature"
            		data_xbee=ser.readline()
            		#print data_xbee
            		if(data_xbee.strip()=="U"):
                		flag_u2=1
            		else:
                		temperature+=data_xbee
        	while(flag_z==0):
            		#print "Reading co2"
            		data_xbee=ser.readline()
            		#print data_xbee
            		if(data_xbee.strip()=="Z"):
                		flag_z=1
            		else:
                		co2emission+=data_xbee
            	#print "check"
		humidity=humidity.strip()
		temperature=temperature.strip()
		co2emission=co2emission.strip()
		
		humidity=float(humidity)
		temperature=float(temperature)
		co2emission=int(co2emission)
		if(co2emission>800):
			results=""
			reg=""
			gcm = GCM(gcm_api["GOOGLE_API_KEY"])
			data = {'warning': 'Danger high CO2 emission '}
			condition_gcm="SELECT gcm_regid FROM gcm_users"
			try:
				cursor.execute(condition_gcm)
				results = cursor.fetchall()
				print results
				#reg=results[0][0]
				for reg in results:
					reg=[reg[0]]
					response = gcm.json_request(registration_ids=reg, data=data)
					print response
					reg
					#print results
			except:
				print "error"

			reg=[reg]
			print reg
		
		if(temperature>60):
			results=""
			reg=""
			gcm = GCM(gcm_api["GOOGLE_API_KEY"])
			data = {'warning': 'Maybe your house is burning'}
			condition_gcm="SELECT gcm_regid FROM gcm_users"
			try:
				cursor.execute(condition_gcm)
				results = cursor.fetchall()
				print results
				#reg=results[0][0]
				for reg in results:
					reg=[reg[0]]
					response = gcm.json_request(registration_ids=reg, data=data)
					print response
					reg
					#print results
			except:
				print "error"

			reg=[reg]
			print reg
		elif(temperature>40):
			results=""
			reg=""
			gcm = GCM(gcm_api["GOOGLE_API_KEY"])
			data = {'warning': 'You might wanna turn on your AC'}
			condition_gcm="SELECT gcm_regid FROM gcm_users"
			try:
				cursor.execute(condition_gcm)
				results = cursor.fetchall()
				print results
				#reg=results[0][0]
				for reg in results:
					reg=[reg[0]]
					response = gcm.json_request(registration_ids=reg, data=data)
					print response
					reg
					#print results
			except:
				print "error"

			reg=[reg]
			print reg

		condition_sensor="INSERT INTO Sensor_Data(Humidity,Temperature,Co2ppm,Time) VALUES('%.2f','%.2f','%d',NOW())" % (humidity, temperature, co2emission)
        	print condition_sensor
        	try:
                	cursor.execute(condition_sensor)
                	db.commit()

        	except MySQLdb.Error, e:
                	print e.args[0],e.args[1]
                	db.rollback()
		print humidity,temperature,co2emission
	
	if(data_xbee.strip()=='*'):
		results=""
		reg=""
		gcm = GCM(gcm_api["GOOGLE_API_KEY"])
		data = {'warning': 'After the burning train youll see a burning house'}
		condition_gcm="SELECT gcm_regid FROM gcm_users"
		try:
			cursor.execute(condition_gcm)
			results = cursor.fetchall()
			print results
			#reg=results[0][0]
			for reg in results:
				reg=[reg[0]]
				response = gcm.json_request(registration_ids=reg, data=data)
				print response
				reg
				#print results
		except:
			print "error"

		reg=[reg]
		print reg

	if(data_xbee.strip()=='?'):
		results=""
		reg=""
		gcm = GCM(gcm_api["GOOGLE_API_KEY"])
		data = {'warning': 'Intruder Alert'}
		condition_gcm="SELECT gcm_regid FROM gcm_users"
		try:
			cursor.execute(condition_gcm)
			results = cursor.fetchall()
			print results
			#reg=results[0][0]
			for reg in results:
				reg=[reg[0]]
				response = gcm.json_request(registration_ids=reg, data=data)
				print response
				reg
				#print results
		except:
			print "error"

		reg=[reg]
		print reg
                
            
        
