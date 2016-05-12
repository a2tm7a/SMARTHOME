from bottle import route, run, template,response, get, post,request, static_file

@route('/Login/css/<staticFiles>')

def css1(staticFiles) :

    return static_file(staticFiles , root="/home/pi/Desktop/SMARTHOME/html/Login/css")

@route('/Login/javascripts/<staticFiles>')

def img(staticFiles) :

    return static_file(staticFiles , root="/home/pi/Desktop/SMARTHOME/html/Login/js")

