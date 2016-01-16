from bottle import route, run, template,response, get, post,request, static_file

@route('/Application/css/<staticFiles>')

def css1(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/Application/css")

@route('/Application/fonts/<staticFiles>')

def img(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/Application/fonts")


@route('/Application/images/portfolio/<staticFiles>')

def img(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/Application/images/portfolio")


@route('/Application/images/<staticFiles>')

def img(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/Application/images")

@route('/Application/js/<staticFiles>')

def img(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/Application/js")
