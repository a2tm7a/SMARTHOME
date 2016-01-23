from bottle import route, run, template,response, get, post,request, static_file
import os
from config import *
import staticFilesForAbout
import staticFilesForLogin
import staticFilesForApplication
import MySQLdb
#import RPi.GPIO as GPIO

#GPIO.setmode(GPIO.BCM)

#GPIO.setup(14,GPIO.OUT)
#GPIO.setup(15,GPIO.IN)
#GPIO.setup(18,GPIO.OUT)
#GPIO.setup(7,GPIO.OUT)
#GPIO.setup(25,GPIO.OUT)

#ser=serial.Serial('/dev/ttyAMA0' , 9600)
#ser.open()

db = MySQLdb.connect(configs_db["host"],configs_db["user"],configs_db["password"],configs_db["database"])
cursor = db.cursor()



@route('/')
def greet(name='Stranger'):
	print 'hello'
	print os.path.dirname(os.path.abspath(__file__))
	return static_file('index.html', root="/home/manchanda/Projects/SMARTHOME/html/ABOUT")


@get('/login')
def login_page(name='Stranger'):
	print 'hello'
	print os.path.dirname(os.path.abspath(__file__))
	return static_file('index.html', root="/home/manchanda/Projects/SMARTHOME/html/Login")


@post('/login') # or @route('/login', method='POST')
def login():
	username = request.forms.get('username')
	password = request.forms.get('password')
	if(username == configs_user['username'] and password == configs_user['password']):
		condition_light="SELECT Status FROM Lights ORDER BY Time DESC LIMIT 1"
		condition_fan="SELECT Status FROM Fan ORDER BY Time DESC LIMIT 1"
		condition_curtain="SELECT Status FROM Curtains ORDER BY Time DESC LIMIT 1"
		condition_security="SELECT Status FROM Security ORDER BY Time DESC LIMIT 1"
		condition_automaticmode="SELECT Status FROM AutomaticMode ORDER BY Time DESC LIMIT 1"
		try:
			cursor.execute(condition_light)
			results = cursor.fetchall()
			print results[0][0]
			light_status=results[0][0]

			cursor.execute(condition_fan)
			results = cursor.fetchall()
			print results[0][0]
			fan_status=results[0][0]

			cursor.execute(condition_curtain)
			results = cursor.fetchall()
			print results[0][0]
			curtain_status=results[0][0]

			cursor.execute(condition_security)
			results = cursor.fetchall()
			print results[0][0]
			security_status=results[0][0]


			cursor.execute(condition_automaticmode)
			results = cursor.fetchall()
			print results[0][0]
			automatic_status=results[0][0]


			if(light_status == 0):
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


		except:
			print "DB Error" 
		
		return template('/home/manchanda/Projects/SMARTHOME/html/Application/index.html',light=lights, fan=fans, curtain=curtains, security=security, automatic=automatic)
	else:
		return template("<p> Login unsuccessful {{name}} {{pass1}} </p>",name=username, pass1=password)


@route('/test')
def testing():
	print "hee"
	username=configs['username']+configs['password']
	return 'Hey'+username


@post('/lights_control')
def toggle():
	print "Hello Lights are toggle"
	status = request.forms.get('status')
	status = int(status)
	Interface = request.forms.get('Interface')
	print Interface
	condition_light="INSERT INTO Lights(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
	print condition_light
	try:
		cursor.execute(condition_light)
		db.commit()
		

	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback() 
	return "toggle"


@post('/security_control')
def toggle():
	print "Hello Security are toggle"
	status = request.forms.get('status')
	status = int(status)
	Interface = request.forms.get('Interface')
	print Interface
	condition_security="INSERT INTO Security(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
	print condition_security
	try:
		cursor.execute(condition_security)
		db.commit()
		

	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback() 
	return "toggle"


@post('/fan_control')
def toggle():
	print "Hello Fan speed changed"
	status = request.forms.get('status')
	status = int(status)
	Interface = request.forms.get('Interface')
	print Interface
	condition_fan="INSERT INTO Fan(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
	print condition_fan
	try:
		cursor.execute(condition_fan)
		db.commit()
		
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback() 
	return "Changed"



@post('/curtain_control')
def toggle():
	print "Hello Curtains changed"
	status = request.forms.get('status')
	status = int(status)
	Interface = request.forms.get('Interface')
	print Interface
	condition_curtains="INSERT INTO Curtains(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
	print condition_curtains
	try:
		cursor.execute(condition_curtains)
		db.commit()
		
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback() 
	return "Changed"



@post('/automatic_control')
def toggle():
	print "Hello automatic_control changed"
	status = request.forms.get('status')
	status = int(status)
	Interface = request.forms.get('Interface')
	print Interface
	condition_automaticmode="INSERT INTO AutomaticMode(Status,Interface,Time) VALUES('%d','%s',NOW())" % (status, Interface)
	print condition_automaticmode
	try:
		cursor.execute(condition_automaticmode)
		db.commit()
		
	except MySQLdb.Error, e:
		print e.args[0],e.args[1]
		db.rollback() 
	return "Changed"




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
	data=getdata()
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



run(host='localhost', port=8080, debug=True)
