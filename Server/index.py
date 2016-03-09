from bottle import route, run, template,response, get, post,request, static_file
import os
from config import *
import staticFilesForAbout
import staticFilesForLogin
import staticFilesForApplication
import MySQLdb
import serial
import time
import requests
import json
from gcm import GCM
import collections
from saveDB import saveFromFanDB
from prediction import predictSpeed

db = MySQLdb.connect(configs_db["host"],configs_db["user"],configs_db["password"],configs_db["database"])
cursor = db.cursor()

ser = serial.Serial(
        port='/dev/ttyAMA0',
        baudrate = 9600,
        parity=serial.PARITY_NONE,
        stopbits=serial.STOPBITS_ONE,
        bytesize=serial.EIGHTBITS,
        timeout=1
)



@route('/')
def greet(name='Stranger'):
        print 'hello'
        print os.path.dirname(os.path.abspath(__file__))
        return static_file('index.html',root="/home/pi/Desktop/SMARTHOME/html/ABOUT")


@get('/login')
def login_page(name='Stranger'):
        print 'hello'
        print os.path.dirname(os.path.abspath(__file__))
        return static_file('index.html',root="/home/pi/Desktop/SMARTHOME/html/Login")

@get('/automatedFan')
def login_page():
        print "Welcome to Automated Fan"
	saveFromFanDB()
	
	speed = predictSpeed()
	#speed=float(speed)
	status_float=0.0
	status_float=float(speed)
	print type(status_float)
	status=0
	print type(status)
	#status=int(status_float)
	q=insertIntoFan(int(status_float),"Android App")
        print q


	return int(status_float)
        

@post('/login') # or @route('/login', method='POST')
def login():
        username = request.forms.get('username')
        password = request.forms.get('password')
	
        if(username == configs_user['username'] and password == configs_user['password']):

		lights=""
		automatic=""
		curtains=""
		security=""
		fans=""
		
		results=selectFromLights1()
		light_status1=results[0][0]
		print light_status1
		
		results=selectFromLights2()
		light_status2=results[0][0]
		print light_status2
		
		results=selectFromFan()
		fan_status=results[0][0]
		print fan_status
		
		results=selectFromCurtain()
		curtain_status=results[0][0]
		print curtain_status

		results=selectFromSecurity()
		security_status=results[0][0]
		print security_status

		results=selectFromAutomaticMode()
		automatic_status=results[0][0]
		print automatic_status

		results=selectFromSensorData()
		print results[0][0],results[0][1],results[0][2]
               	humidity=results[0][0]
                temperature=results[0][1]
               	co2emission=results[0][2]
		
		results=selectFromUsers()
		connected_users=results
		print connected_users	
			
                if(light_status1 == 0 and light_status2 == 0):
                	lights="bulb_off.png"
                else:
                        lights="bulb_on.png"


                if(automatic_status == 0):
                        automatic="unchecked"
                else:
                        automatic="checked"

                if(curtain_status == 0):
                        curtains="CURTAINS_CLOSED.png"
                else:
                        curtains="CURTAINS_OPEN.png"

                if(security_status == 0):
                        security="doorSecurityOff.png"
                else:
                        security="doorSecurityOn.png"



                if(fan_status == 0):
                        fans="FAN_speed0.png"
                elif(fan_status == 1):
                        fans="FAN_speed1.png"
                elif(fan_status == 2):
                        fans="FAN_speed2.png"
                elif(fan_status == 3):
                        fans="FAN_speed3.png"
                elif(fan_status == 4):
                        fans="FAN_speed4.png"
                else:
                        fans="FAN_speed5.png"

		print lights
                return template('/home/pi/Desktop/SMARTHOME/html/Application/index.html',light=lights, fan=fans, curtain=curtains, security=security, automatic=automatic, connected_devices=connected_users,temperature=temperature,humidity=humidity,co2emission=co2emission)
        else:
                return template("<p> Login unsuccessful {{name}} {{pass1}} </p>",name=username, pass1=password)


def date_handler(obj):
    return obj.isoformat() if hasattr(obj, 'isoformat') else obj

@route('/test')
def testing():
        print "hee"
        username=configs_user['username']+configs_user['password']
        return 'Hey'+username


@post('/lights_control')
def toggle():
        print "Hello Lights are toggle"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
        
	q=insertIntoLights1(status,Interface)
	print q
	
	q=insertIntoLights2(status,Interface)
	print q
		
        return request.forms.get('status')

@post('/lights_control1')
def toggle():
        print "Hello Lights1 are toggle"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
        q=insertIntoLights1(status,Interface)
	print q
        return request.forms.get('status')

@post('/lights_control2')
def toggle():
        print "Hello Lights2 are toggle"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
	q=insertIntoLights2(status,Interface)
	print q
        return request.forms.get('status')


@post('/security_control')
def toggle():
        print "Hello Security are toggle"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
        q=insertIntoSecurity(status,Interface)
	print q
	return request.forms.get('status')


@post('/fan_control')
def toggle():
        print "Hello Fan speed changed"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
        q=insertIntoFan(status,Interface)
	print q

        return request.forms.get('status')



@post('/curtain_control')
def toggle():
        print "Hello Curtains changed"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
        q=insertIntoCurtain(status,Interface)
	print q
	
	return request.forms.get('status')

@post('/away_mode_control')
def toggle():
        print "Hello Away Mode"
        status = request.forms.get('status')
        status = int(status)
        Interface = request.forms.get('Interface')
        print Interface


	curtain_status=0
        q=insertIntoCurtain(curtain_status,Interface)
	print q
		
	fan_status=0
	q=insertIntoFan(fan_status,Interface)
	print q


	security_status=1
	q=insertIntoSecurity(security_status,Interface)
	print q

	light_status1=0
	q=insertIntoLights1(light_status1,Interface)
	print q


	light_status2=0
	q=insertIntoLights2(light_status2,Interface)
	print q

        return request.forms.get('status')


@post('/morning_mode_control')
def toggle():
        print "Hello Morning Mode"
        status = request.forms.get('status')
        status = int(status)
        Interface = request.forms.get('Interface')
        print Interface


	curtain_status=1
        q=insertIntoCurtain(curtain_status,Interface)
	print q
		

	security_status=0
	q=insertIntoSecurity(security_status,Interface)
	print q

	light_status1=1
	q=insertIntoLights1(light_status1,Interface)
	print q


	light_status2=1
	q=insertIntoLights2(light_status2,Interface)
	print q

        return request.forms.get('status')


@post('/sleep_mode_control')
def toggle():
        print "Hello Sleep Mode"
        status = request.forms.get('status')
        status = int(status)
        Interface = request.forms.get('Interface')
        print Interface


	curtain_status=0
        q=insertIntoCurtain(curtain_status,Interface)
	print q


	security_status=1
	q=insertIntoSecurity(security_status,Interface)
	print q

	light_status1=0
	q=insertIntoLights1(light_status1,Interface)
	print q
	

	light_status2=0
	q=insertIntoLights2(light_status2,Interface)
	print q


        return request.forms.get('status')

@post('/theater_mode_control')
def toggle():
        print "Hello theater Mode"
        status = request.forms.get('status')
        status = int(status)
        Interface = request.forms.get('Interface')
        print Interface


	curtain_status=0
        q=insertIntoCurtain(curtain_status,Interface)
	print q


	light_status1=0
	q=insertIntoLights1(light_status1,Interface)
	print q


	light_status2=0
	q=insertIntoLights2(light_status2,Interface)
	print q



        return request.forms.get('status')


@post('/automatic_control')
def toggle():
        print "Hello automatic_control changed"
        status = request.forms.get('status')
        Interface = request.forms.get('Interface')
	
	q=insertIntoAutomaticMode(status,Interface)
	print q	

        return request.forms.get('status')



#Mobile ENDPOINTS

@get('/logintest') # or @route('/login')
def login():
        return '''
        <form action="/login" method="post">
                    Username: <input name="username" type="text" />
                    Password: <input name="password" type="password" />
                    <input value="Login" type="submit" />
                </form>
            '''


@post('/data') # or @route('/login', method='POST')
def data():
        print "Mobile end point for data"
	
	humidity=0.00
	temperature=0.00
	co2emission=0            
	data=""

				
	results=selectFromLights1()
        light_status1=results[0][0]
        print light_status1

        results=selectFromLights2()
        light_status2=results[0][0]
        print light_status2

        results=selectFromFan()
        fan_status=results[0][0]
        print fan_status

        results=selectFromCurtain()
        curtain_status=results[0][0]
        print curtain_status

        results=selectFromSecurity()
        security_status=results[0][0]
        print security_status

        results=selectFromAutomaticMode()
        automatic_status=results[0][0]
        print automatic_status

        results=selectFromSensorData()
        print results[0][0],results[0][1],results[0][2]
        humidity=results[0][0]
        temperature=results[0][1]
        co2emission=results[0][2]

        results=selectFromUsers()
        connected_users=results
        print connected_users

        data={'lights1':light_status1,'lights2':light_status2, 'fan':fan_status,'curtains':curtain_status, 'security':security_status,'automatic':automatic_status,'temperature':temperature, 'humidity':humidity,'co2emission':co2emission,'connected_user':connected_users}
        
        return data

@post('/logincheck') # or @route('/login', method='POST')
def checklogin():
        username = request.forms.get('username')
        password = request.forms.get('password')
        if(username == configs_user['username'] and password == configs_user['password']):
                return "1"

        else:
                return "0"



def getdata():
        return "Test data"


@post('/send_serialterminer')
def senddata():
        return null

@post('/sos')
def testingfunction():
	latitude = request.forms.get('latitude')
	longitude = request.forms.get('longitude')
	message="Amit needs help "+latitude+" ,"+longitude
	sendMessageGcm(message)
	print message

@post('/test_gcm')
def testingfunction():
        message=request.forms.get('message')
        sendMessageGcm('asdasdqwe123123',message)

@post('/storeGcmUser')
def storeGcmUser():
        gcm_regId = request.forms.get('gcm_regId')
        insert_data="INSERT INTO gcm_users(gcm_regid, created_at) VALUES('%s', NOW())" % (gcm_regId)
        print insert_data
        try:
                cursor.execute(insert_data)
                db.commit()
        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
        return "0"

def sendMessageGcm(message):
	results=""
	reg=""
	gcm = GCM(gcm_api["GOOGLE_API_KEY"])
	print message
	data = {'warning': message}
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
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback()
		print "returning 0"
		return "0"
	print "returning 1"
	return "1"


@get('/test_gcm')
def testingfunction():
	
	sendMessageGcm('asdasdqwe123123','hello')

@post('/storeGcmUser')
def storeGcmUser():
	gcm_regId = request.forms.get('gcm_regId')
	insert_data="INSERT INTO gcm_users(gcm_regid, created_at) VALUES('%s', NOW())" % (gcm_regId)
	print insert_data
	try:
		cursor.execute(insert_data)
		db.commit()
		
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback()
	return "0"

def insertIntoLights1(status,Interface):
	status = int(status)
	print Interface
        condition_light1="INSERT INTO Lights1(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
        print condition_light1
        if(status==1):
                ser.write('c')
        elif(status==0):
                ser.write('d')
        try:
                cursor.execute(condition_light1)
                db.commit()
		return '1'

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'

def insertIntoLights2(status,Interface):
	status = int(status)
        print Interface
        condition_light2="INSERT INTO Lights2(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
        print condition_light2
        if(status==1):
                ser.write('1')
        elif(status==0):
                ser.write('2')
        try:
                cursor.execute(condition_light2)
                db.commit()
		return '1'

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'

def insertIntoFan(status,Interface):
	
	status = int(status)
	print Interface
	
	results=selectFromSensorData()
	humidity=results[0][0]
	temperature=results[0][1]
	
	humidity=float(humidity)
	temperature=float(temperature)
	
        condition_fan="INSERT INTO Fan(Status,Interface,Humidity,Temperature,Time) VALUES('%d','%s','%.2f',%.2f,NOW())" % (status, Interface,humidity,temperature)
        print condition_fan
	if(status==0):
                        print "sending e"
                        ser.write('e')
                        #senddata('e')
        elif(status==1):
                        print "sneding f"
                        #senddata('f')
                        ser.write('f')
        elif(status==2):
                        print "sneding g"
                        #senddata('g')
                        ser.write('g')
        elif(status==3):
                        print "sneding h"
                        #senddata('h')
                        ser.write('h')
        elif(status==4):
                        print "sneding i"
                        #senddata('i')
                        ser.write('i')
        elif(status==5):
                        print "sneding j"
                        #senddata('j')
                        ser.write('j')

	try:
                cursor.execute(condition_fan)
                db.commit()
		return '1'
        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'

def insertIntoCurtain(status,Interface):
	status = int(status)
	print Interface
        condition_curtains="INSERT INTO Curtains(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
        print condition_curtains

	if(status==1):
                ser.write('o')
        elif(status==0):
                ser.write('p')

        try:
                cursor.execute(condition_curtains)
                db.commit()
		return '1'
        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'

def insertIntoSecurity(status,Interface):
	status = int(status)
	print Interface
        condition_security="INSERT INTO Security(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
        print condition_security
        
        if(status==1):
                        print "sending t"
                        ser.write('t')
                        #senddata('t')
        elif(status==0):
                        print "sneding s"
                        #senddata('s')
                        ser.write('s')
                
        try:
                cursor.execute(condition_security)
                db.commit()
		return '1'

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'
                
def insertIntoAutomaticMode(status,Interface):
	status = int(status)
        print Interface
        condition_automaticmode="INSERT INTO AutomaticMode(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
        print condition_automaticmode
	
	if(status==1):
                ser.write('b')

        elif(status==0):
                ser.write('a')

        try:
                cursor.execute(condition_automaticmode)
                db.commit()
		return '1'
        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
		return '0'

def selectFromLights1():
	condition_light1="SELECT Status FROM Lights1 ORDER BY Time DESC LIMIT 1"
	try:
		cursor.execute(condition_light1)
		results = cursor.fetchall()
		return results
	
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback()
		return 0

def selectFromLights2():
        condition_light2="SELECT Status FROM Lights2 ORDER BY Time DESC LIMIT 1"
        try:
                cursor.execute(condition_light2)
                results = cursor.fetchall()
                return results

	except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0

def selectFromFan():
        condition_fan="SELECT Status FROM Fan ORDER BY Time DESC LIMIT 1"

        try:
		cursor.execute(condition_fan)
		results = cursor.fetchall()
		return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0

def selectFromCurtain():
       
	condition_curtain="SELECT Status FROM Curtains ORDER BY Time DESC LIMIT 1"
        try:
                cursor.execute(condition_curtain)
                results = cursor.fetchall()
                return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0

def selectFromSecurity():

        condition_security="SELECT Status FROM Security ORDER BY Time DESC LIMIT 1"

        try:
                cursor.execute(condition_security)
                results = cursor.fetchall()
                return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0
                     
def selectFromAutomaticMode():

        condition_automaticmode="SELECT Status FROM AutomaticMode ORDER BY Time DESC LIMIT 1"

        try:
                cursor.execute(condition_automaticmode)
                results = cursor.fetchall()
                print results
		return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0

def selectFromSensorData():

        condition_sensor="SELECT Humidity, Temperature, Co2ppm FROM Sensor_Data ORDER BY Time DESC LIMIT 1"


        try:
                cursor.execute(condition_sensor)
                results = cursor.fetchall()
                return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0

def selectFromUsers():

        condition_user="SELECT Name FROM Users WHERE ONLINE = 1 AND NAME IS NOT NULL ORDER BY Time DESC"


        try:
                cursor.execute(condition_user)
                results = cursor.fetchall()
                return results

        except MySQLdb.Error, e:
                print e.args[0],e.args[1]
                db.rollback()
                return 0


def sendMessageGcm(registration_ids,message):
	'''url="https://android.googleapis.com/gcm/send"
	headers = {'Authorization': 'key='+gcm["GOOGLE_API_KEY"],
		'Content-Type':'application/json'}

	payload = {'registration_ids': registration_ids, 'data': message}
	r = requests.post(url, data, headers=headers)
	print  json.dumps(payload)

	print r'''
	results=""
	reg=""
	gcm = GCM(gcm_api["GOOGLE_API_KEY"])
	data = {'warning': message}
	condition_gcm="SELECT gcm_regid FROM gcm_users"
	try:
		cursor.execute(condition_gcm)
		results = cursor.fetchall()
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

	#regis=["APA91bHJsIRkKDGJQeJgOJAQ_YYHrS0GuB5fTHtuD-Ir0M5TjZbPIVUfKD7IRPB1VkyUjjGYLRxVcmO3tuPYZNsPfYOaGIg5zzWuCIvpgr7xGLVlzo4s2bSGPJ8y_wzSYVjjY6WjGfFqpN83Y2ftoOMEoW-SEdwbEw"]
	#print regis
	#response = gcm.json_request(registration_ids=reg, data=data)
	#print response




run(host='192.168.0.100', port=9000, debug=True)
