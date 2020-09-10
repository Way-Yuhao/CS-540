'''
Created on Mar 31, 2019

@author: LiuYuhao
'''
import pandas as pd 
import numpy as np 
from numpy import linalg as LA
import matplotlib.pyplot as plt

def main():
    cd = pd.read_csv('cardata.csv')
    
    print('----Basics----')
    d = 13 - 3 + 1
    print('Dimension: {}'.format(d))
    mean_retail = cd['Retail($)'].mean()
    print('Retail Mean: ${}'.format(mean_retail))
    mean_hp = cd['Horsepower'].mean()
    print('Horsepower Mean: ${}'.format(mean_hp))
    
    print('----Normalizing Data----')
    normalizedMX = normalize(cd)
    covarMX = normalizedMX.cov()
    
    print('----Matrix Decomposition----')
    eigVal, eigVec = LA.eig(covarMX)
    eigTup = list(zip(eigVal, eigVec))
    sortedEig = sorted(eigTup, key = lambda x: x[0], reverse = True)
    print("First eigenvector: {}".format(sortedEig[0][1]))
    print("Third eigenvector: {}".format(sortedEig[2][1]))
    
    print('----Eigenvector Analysis----')
    posEigFeatures = []
    featureList = list(cd)
    index = 0
    for x in sortedEig[0][1]:
        if x>0:
            posEigFeatures.append(featureList[index + 2])
        index += 1
    print('Features corresponding to positive eigenvector entries: \n{}'.format(posEigFeatures))
    
    print('----Principle Component Analysis----')
    subData = normalize(cd, drop = False)
    setMinivan = dropFrames(subData[subData['Category'].isin(['minivan'])])
    setSedan = dropFrames(subData[subData['Category'].isin(['sedan'])])
    setSUV = dropFrames(subData[subData['Category'].isin(['suv'])])
    global e1 
    e1 = sortedEig[0][1]
    global e2 
    e2 = sortedEig[1][1]
    
    plotPCA(setMinivan) #blue
    plotPCA(setSedan) #yellow
    plotPCA(setSUV) #green
    plt.show()
    
def plotPCA(dataset):
    vec = getVectors(dataset)
    plt.scatter(np.dot(vec, e1), np.dot(vec, e2))
    
def getVectors(subSet):
    vectors = []
    for i in range(len(subSet)) : 
        vector = []
        for j in range(0, 11):
            vector.append(subSet.iloc[i, j])
        vectors.append(vector)
    return vectors
    
def dropFrames(sets):
    del sets['Model']
    del sets['Category']
    return sets
     
def normalize(cd, drop = True):
    result = cd.copy()
    if drop:
        del result['Model']
        del result['Category']
    for featureName in result:
        if featureName == 'Model' or featureName == 'Category':
            continue
        mean = result[featureName].mean()
        std = result[featureName].std()
        result[featureName] = (result[featureName] - mean) / std
    return result
if __name__ == '__main__':
    main()