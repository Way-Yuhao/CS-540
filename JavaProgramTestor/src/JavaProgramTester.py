'''
Created on Apr 21, 2019

@author: LiuYuhao
'''
import subprocess as sb

PATH = ''
TESTFILE = ''


testFile = open(TESTFILE, 'r')
os.chdir("/tmp/")

lines = testFile.readlines()
curOutput = ''
for line in lines:
    if line.contains('java'):
        curOutput = ''
        sb.check_output('')
    