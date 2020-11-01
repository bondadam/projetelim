import os
import re
import numpy as np
import pandas as pd
import scipy.io.wavfile as sw
import matplotlib.pyplot as plt
from scipy import fftpack
from sklearn.metrics import confusion_matrix
from sklearn import svm
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.preprocessing import StandardScaler
import pickle


rootDir = os.path.abspath(os.path.join(os.getcwd() , os.pardir)) + "\\data\\"
goodDir = rootDir + "\\good\\"
badDir = rootDir + "\\bad\\"
file_names = [goodDir + x for x in os.listdir(goodDir)] + [badDir + x for x in os.listdir(badDir)]
expected_length = 8 * 48000 #48 khz arbitrary sampling rate
final_dataset = pd.DataFrame(columns = range(expected_length + 1), dtype = np.float64)


### Standardisation de la longueur à 8 s
### Si plus court on pad de 0

for i in file_names:
    rate,signal = sw.read(i)
    if (len(signal) < expected_length):
        signal = np.resize(signal, expected_length)
    signal = signal[:expected_length]
    signal = fftpack.rfft(signal) #fourier transform
    signal = np.append(signal, 1 if "good" in i else 0)
    final_dataset = final_dataset.append(pd.DataFrame(signal).T)
    
print(final_dataset)
print(final_dataset.dtypes)



#Finalize dataset with the attributes and target
fd=final_dataset
y=fd.iloc[:,-1]
X=fd.iloc[:,0:expected_length]

#Spliting into test and train
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.33, random_state = 1)
#del y_train

#Feature Scaling
sc = StandardScaler()
X_train = sc.fit_transform(X_train)
X_test = sc.transform(X_test)

X_train=pd.DataFrame(X_train)
y_train=pd.DataFrame(y_train)
X_test=pd.DataFrame(X_test)


#Model Building

#SVM
model = svm.SVC(kernel = 'rbf', C = 1)
model1 = model.fit(X_train,y_train.values.ravel())
model1.score(X_train,y_train)
predicted=model.predict(X_test)
print(accuracy_score(y_test,predicted))

pkl_filename = "pickle_model.pkl"
with open(pkl_filename, 'wb') as file:
    pickle.dump(model, file)

