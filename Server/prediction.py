from sklearn.ensemble import RandomForestClassifier
import csv
from numpy import genfromtxt, savetxt

def predictSpeed():
	dataset = genfromtxt(open('dataset.csv','r'), delimiter=',', dtype='f8')[0:]    
	target = [x[0] for x in dataset]
	train = [x[1:] for x in dataset]
	#print target
	#print '\n'
	#print train
	#print dataset
	test = genfromtxt(open('test.csv','r'), delimiter=',', dtype='f8')[0:]
	rf = RandomForestClassifier(n_estimators=100)
    	rf.fit(train, target)
	savetxt('value.csv', rf.predict(test), delimiter=',', fmt='%f')
	#value = genfromtxt(open('value.csv','r'),delimiter=',', dtype='f8')[0:]
	#print value
	f = open('value.csv', 'r')
	value=f.read()
	print value," predicted Speed"
	return value.rstrip()

	
