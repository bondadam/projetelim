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
from flask import Flask, flash, request, redirect, url_for, render_template, jsonify
from werkzeug.utils import secure_filename


sampling_rate = 22050 # 22Hz arbitrary sampling rate
expected_length = 7 * sampling_rate

ALLOWED_EXTENSIONS = {'wav'}
UPLOAD_FOLDER = './'

app = Flask(__name__)
app.secret_key = "test"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024

@app.route('/')
def index():
    return render_template('index.html')

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/upload', methods=['POST'])
def upload():
    if request.method == 'POST':
        # check if the post request has the file part
        if 'file' not in request.files:
            flash('No file part')
            return redirect(request.url)
        file = request.files['file']
        # if user does not select file, browser also
        # submit an empty part without filename
        if file.filename == '':
            flash('No selected file')
            return redirect(request.url)
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            signal, _ = librosa.load(filename, sampling_rate)
            if (len(signal) < expected_length):
                signal = np.resize(signal, expected_length)
            signal = signal[:expected_length]
            signal = fftpack.rfft(signal) #fourier transform
            signal = pd.DataFrame(signal).T

            pkl_filename = "pickle_model.pkl"
            with open(pkl_filename, 'rb') as file:
                pickle_model = pickle.load(file)
                
            Ypredict = pickle_model.predict(signal)
            result = {'correct': Ypredict[0]}
            return jsonify(result)

    abort(400)
