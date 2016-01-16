from bottle import route, run, template,response, get, post,request, static_file

#stylesheet
@route('/About/stylesheets/<staticFiles>')

def css1(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/stylesheets")



#prettyPhoto
@route('/About/prettyPhoto/dark_rounded/<staticFiles>')

def cssphotor(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/dark_rounded")

@route('/About/prettyPhoto/dark_square/<staticFiles>')

def cssphotos(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/dark_square")

@route('/About/prettyPhoto/default/<staticFiles>')

def cssphotod(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/default")

@route('/About/prettyPhoto/facebook/<staticFiles>')

def cssphotof(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/facebook")

@route('/About/prettyPhoto/light_rounded/<staticFiles>')

def cssphotolr(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/light_rounded")

@route('/About/prettyPhoto/light_square/<staticFiles>')

def cssphotols(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/prettyPhoto/light_square")



@route('/About/images/team/<staticFiles>')

def imgt(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/team")

@route('/About/images/flexslider/<staticFiles>')

def imgt(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/flexslider")


@route('/About/images/<staticFiles>')

def imgi(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images")

@route('/About/images/portfolio/<staticFiles>')

def imgp(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/portfolio")

@route('/About/images/icons/<staticFiles>')

def imgic(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/icons")

@route('/About/images/icons/social/<staticFiles>')

def imgs(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/icons/social")

@route('/About/images/icons/social/big/<staticFiles>')

def imgb(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/icons/social/big")

@route('/About/images/icons/social/small/<staticFiles>')

def imgsmal(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/images/icons/social/small")


@route('/About/javascripts/<staticFiles>')

def javas(staticFiles) :

    return static_file(staticFiles , root="/home/manchanda/Projects/SMARTHOME/html/ABOUT/javascripts")
