#!/usr/bin/env python
# coding: utf-8


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
import librosa


sampling_rate = 100 # 22Hz arbitrary sampling rate
expected_length = 6 * sampling_rate

### Standardisation de la longueur à 8 s
### Si plus court on pad de 0

for i in os.listdir():
    if i[-1] == "v":
        signal, _ = librosa.load(i, sampling_rate)
        if (len(signal) < expected_length):
            signal = np.resize(signal, expected_length)
            #signal = np.pad(signal, (0, expected_length - len(signal)), 'constant')
        signal = signal[:expected_length]
        signal = fftpack.rfft(signal) #fourier transform
        signal = pd.DataFrame(signal).T
        pkl_filename = "pickle_model.pkl"
        with open(pkl_filename, 'rb') as file:
            pickle_model = pickle.load(file)
            Ypredict = pickle_model.predict(signal)
            print(Ypredict)
