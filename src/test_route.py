import os
import re
import numpy as np
import pandas as pd
from scipy import fftpack

import scipy.io.wavfile as sw
import matplotlib.pyplot as plt
import pickle

expected_length = 8 * 48000 #48 khz arbitrary sampling rate

rootDir = os.path.abspath(os.path.join(os.getcwd() , os.pardir)) + "\\data\\"
goodDir = rootDir + "\\good\\"
badDir = rootDir + "\\bad\\"
file_names = [goodDir + x for x in os.listdir(goodDir)] + [badDir + x for x in os.listdir(badDir)]

test_file = file_names[0]
rate,signal = sw.read(test_file)
if (len(signal) < expected_length):
    signal = np.resize(signal, expected_length)
signal = signal[:expected_length]
signal = fftpack.rfft(signal)
signal = pd.DataFrame(signal).T

print(signal)


pkl_filename = "pickle_model.pkl"

print(test_file)
with open(pkl_filename, 'rb') as file:
    pickle_model = pickle.load(file)
    
Ypredict = pickle_model.predict(signal)
print(Ypredict)
