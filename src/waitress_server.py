from waitress import serve
import flask_app
serve(flask_app.app, host='0.0.0.0', port=8080)