import os
import getpass
import subprocess
nameOfFile='';
#This file makes the text file using the master list and the date- it is called by the startCrabJobs() function in the masterPythonScriptAnotherCopy file
def createTextFile(masterList,date, fullDate):
    #Move to the correct place and create the file in the user's test directory
    user=getpass.getuser()
    nameOfFile=subprocess.check_output("echo $PWD", shell=True)
    nameOfFile=nameOfFile.rstrip()
    print(nameOfFile)
    nameOfFile+="/textFiles/Jobs" + str(fullDate) + ".txt"
    #The name of the file is jobs along with the date and the time
    command=str('touch '+ nameOfFile)
    print(command)
    os.system(command)
    newFile=open(nameOfFile, 'w')
    for i in masterList:
        newFile.write(i[0] + ' ' + i[1] + ' '+ i[2] + ' ' + date)
        #who the user is
        newFile.write('\ngenSimCrabConfig\n')
    newFile.close()
#Change the step of a particular job
def changeStep(line, newStep, newfile):
    newFile=open(newfile, 'r')
    data = newFile.readlines()
    lineNum=line+1
    newFile[lineNum+1] = newStep;
    with open(newfile) as thefile:
        thefile.writelines(data)
#get the line number of a particular job
def getLineNumber(minMass, maxMass, lambdaValue, date):
    newFile=open('Jobs' + dateFile, 'r')
    lineNum=0
    for line in newFile.readLines():
        lineNum+1
        if minMass + ' ' + maxMass + ' ' + lambdaValue in line:
            correctLineNum=lineNum
    return correctLineNum
#Get the date of the job

def getLine(lineNum, newFile):
    f=open(newFile, 'r')
    lines=f.readlines()
    return lines[int(lineNum)]

#Get the current step of the job, this is done by opening and reading through the file to search for the correct job and read the line below it 
def getStep(line2, newfile):
    newFile=open(newfile, 'r')
    lineNum=-1
    data=newFile.readlines();
    data=[x.rstrip('\n') for x in data]
    for line in data:
        lineNum+=1
        words=[]
        words2=[]
        for word in line.split():
            words.append(word)
        for word in line2.split():
            words2.append(word)
        if (words2[0]==words[0] and words2[1]==words[1] and words2[2]==words[2]):
            print(data[lineNum+1])
            return data[lineNum+1]
