#! /usr/bin/python

# To change this template, choose Tools | Templates
# and open the template in the editor.

__author__="Misgana Bayetta <misgana.bayetta@gmail.com>"
__date__ ="$May 23, 2013 11:38:34 AM$"

if __name__ == "__main__":
    print "Hello World";
TEST = open('preparedCrx.moses').readlines()

def getOut(Data):
 return [int(x.split('\t')[0]) for x in Data[1:]]

OBS = getOut(TEST)
print OBS