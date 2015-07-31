#!/usr/bin/python
# Author: Scott Hazelhurst
# (C) University of the Witwatersrand, SBIMB
# Released under GPL 2 licence

import argparse
import glob
import re
import os
import sys



def parseArguments():
    parser = argparse.ArgumentParser(description='give a fam file, update population')
    parser.add_argument('fam', metavar='famfile', type=str, 
                   help='fam file name')
    parser.add_argument('--popfname', dest='popfname', action='store',
                   default="", 
                   help='name of pop file in phe format ')
    parser.add_argument('--popcol', dest='popcol', action='store',
                   default = 3, type=int, 
                   help='column in popfile (number from 1) default is 3 ')
    parser.add_argument('--output', dest="output", action='store',
                    type=str,default="")

    args = parser.parse_args()
    return args


def getLabels(pfname,col):
    f = open(pfname)
    phe = {}
    for line in f:
        data = re.findall("\w+",line)
        indiv="%s:%s"%(data[0],data[1])
        phe[indiv]=data[col-1]
    f.close()
    return phe


args=parseArguments()

phe = getLabels(args.popfname,args.popcol)

os.system("cp %s %s.orig"%(args.fam,args.fam))
if not args.output:
    fout=sys.stdout
else:
    fout=open(args.output,"w")

f = open(args.fam)
for line in f:
    if "#" in line:
        fout.write(line)
        continue
    mm = re.search("(\w+)(\s+)(\w+)(.*\s+)\S",line)
    if not mm:
        sys.exit("Error with line "+line)
    pid="%s:%s"%(mm.group(1),mm.group(3))
    blank=mm.group(2)
    rest=mm.group(4)
    pop = phe[pid]
    fout.write("%s%s%s%s%s\n"%(mm.group(1),blank,mm.group(3),rest,pop))
    
f.close()
fout.close()
