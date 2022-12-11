from flask import Flask, request, Response, jsonify
from pymongo import MongoClient, ASCENDING, errors
from datetime import datetime

app = Flask(__name__)
mongo_uri = 'mongodb://root:example@mongo_db:27017/'
mongo_client = MongoClient(mongo_uri)
id_countries_counter = 1
id_cities_counter = 1
id_temperatures_counter = 1
db = mongo_client['database']
countries_collection = db['Tari']
cities_collection = db['Orase']
temperatures_collection = db['Temperaturi']
countries_collection.create_index([('nume_tara', ASCENDING)], unique=True)
cities_collection.create_index([('id_tara', ASCENDING), ('nume_oras', ASCENDING)], unique=True)
temperatures_collection.create_index([('id_oras', ASCENDING), ('timestamp', ASCENDING)], unique=True)


## COUNTRIES
@app.route('/api/countries', methods=['POST', 'GET'])
def countries():
    global id_countries_counter
    if request.method == 'POST':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if 'nume' not in payload or 'lat' not in payload or 'lon' not in payload:
            return Response(status=400)
        if not isinstance(payload['nume'], str) or not isinstance(payload['lat'], float) or not isinstance(payload['lon'], float):
            return Response(status=400)
        try:
            countries_collection.insert_one({
                'id': id_countries_counter,
                'nume_tara': payload['nume'],
                'latitudine': payload['lat'],
                'longitudine': payload['lon'],
            })
        except errors.DuplicateKeyError:
            return Response(status=409)
        id_countries_counter += 1
        return jsonify({ 'id' : id_countries_counter - 1 }), 201
    elif request.method == 'GET':
        result: list = []
        for country in countries_collection.find():
            result.append({
                'id': country['id'],
                'nume': country['nume_tara'],
                'lat': country['latitudine'],
                'lon': country['longitudine'],
            })
        return jsonify(result), 200


@app.route('/api/countries/<int:id>', methods=['PUT', 'DELETE'])
def countries_by_id(id: int):
    if request.method == 'PUT':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if not countries_collection.find_one( { 'id': id } ):
            return Response(status=404)
        if 'id' not in payload or 'nume' not in payload or 'lat' not in payload or 'lon' not in payload:
            return Response(status=400)
        if not isinstance(payload['id'], int) or not isinstance(payload['nume'], str) or not isinstance(payload['lat'], float) or not isinstance(payload['lon'], float):
            return Response(status=400)
        if payload['id'] != id:
            return Response(status=400)
        try:
            countries_collection.update_one( { 'id': id }, { '$set': { 'nume_tara': payload['nume'], 'latitudine': payload['lat'], 'longitudine': payload['lon'] } } )
        except errors.DuplicateKeyError:
            return Response(status=409)
        return Response(status=200)
    elif request.method == 'DELETE':
        if not countries_collection.find_one( { 'id': id } ):
            return Response(status=404)
        cities_collection.delete_many( { 'id_tara': id } )
        countries_collection.delete_one( { 'id': id } )
        return Response(status=200)


## CITIES
@app.route('/api/cities', methods=['POST', 'GET'])
def cities():
    global id_cities_counter
    if request.method == 'POST':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if 'idTara' not in payload or 'nume' not in payload or 'lat' not in payload or 'lon' not in payload:
            return Response(status=400)
        if not isinstance(payload['idTara'], int) or not isinstance(payload['nume'], str) or not isinstance(payload['lat'], float) or not isinstance(payload['lon'], float):
            return Response(status=400)
        if not countries_collection.find_one({ 'id': payload['idTara'] }):
            return Response(status=400)
        try:
            cities_collection.insert_one({
                'id': id_cities_counter,
                'id_tara': payload['idTara'],
                'nume_oras': payload['nume'],
                'latitudine': payload['lat'],
                'longitudine': payload['lon'],
            })
        except errors.DuplicateKeyError:
            return Response(status=409)
        id_cities_counter += 1
        return jsonify({ 'id': id_cities_counter - 1 }), 201
    elif request.method == 'GET':
        result: list = []
        for city in cities_collection.find():
            result.append({
                'id': city['id'],
                'idTara': city['id_tara'],
                'nume': city['nume_oras'],
                'lat': city['latitudine'],
                'lon': city['longitudine'],
            })
        return jsonify(result), 200


@app.route('/api/cities/country/<int:id_tara>', methods=['GET'])
def cities_by_country(id_tara: int):
    result: list = []
    for city in cities_collection.find({ 'id_tara': id_tara }):
        result.append({
            'id': city['id'],
            'idTara': city['id_tara'],
            'nume': city['nume_oras'],
            'lat': city['latitudine'],
            'lon': city['longitudine'],
        })
    return jsonify(result), 200


@app.route('/api/cities/<int:id>', methods=['PUT', 'DELETE'])
def cities_by_id(id: int):
    if request.method == 'PUT':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if not cities_collection.find_one( { 'id': id } ):
            return Response(status=404)
        if 'id' not in payload or 'idTara' not in payload or 'nume' not in payload or 'lat' not in payload or 'lon' not in payload:
            return Response(status=400)
        if not isinstance(payload['id'], int) or not isinstance(payload['idTara'], int) or not isinstance(payload['nume'], str) or not isinstance(payload['lat'], float) or not isinstance(payload['lon'], float):
            return Response(status=400)
        if payload['id'] != id:
            return Response(status=400)
        if not countries_collection.find_one( { 'id': payload['idTara'] }):
            return Response(status=400)
        try:
            cities_collection.update_one({ 'id': id }, { '$set': { 'id_tara': payload['idTara'], 'nume_oras': payload['nume'], 'latitudine': payload['lat'], 'longitudine': payload['lon'] } } )
        except errors.DuplicateKeyError:
            return Response(status=409)
        return Response(status=200)
    elif request.method == 'DELETE':
        if not cities_collection.find_one( { 'id': id } ):
            return Response(status=404)
        temperatures_collection.delete_many( { 'idOras': id } )
        cities_collection.delete_one( { 'id': id } )
        return Response(status=200)


## TEMPERATURES
@app.route('/api/temperatures', methods=['POST', 'GET'])
def temperatures():
    if request.method == 'POST':
        global id_temperatures_counter
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if 'idOras' not in payload or 'valoare' not in payload:
            return Response(status=400)
        if not isinstance(payload['idOras'], int) or not isinstance(payload['valoare'], float):
            return Response(status=400)
        if not cities_collection.find_one({'id': payload['idOras']}):
            return Response(status=400)
        try:
            temperatures_collection.insert_one({
                'id': id_temperatures_counter,
                'valoare': payload['valoare'],
                'timestamp': datetime.now().timestamp(),
                'idOras': payload['idOras'],
            })
        except errors.DuplicateKeyError:
            return Response(status=409)
        id_temperatures_counter += 1
        return jsonify({ 'id' : id_temperatures_counter - 1}), 201
    elif request.method == 'GET':
        args = request.args
        query_cities = {}
        query_temperatures = {}
        query_dates = {}

        if 'lat' in args:
            query_cities['latitudine'] = float(args['lat'])
        if 'lon' in args:
            query_cities['longitudine'] = float(args['lon'])
        query_temperatures['idOras'] =  { '$in': cities_collection.find(query_cities, { 'id': 1 }).distinct('id') }

        if 'from' in args:
            query_dates['$gte'] = datetime.strptime(args['from'], '%Y-%m-%d').timestamp()
        if 'until' in args:
            query_dates['$lte'] = datetime.strptime(args['until'], '%Y-%m-%d').timestamp()
        if query_dates:
            query_temperatures['timestamp'] = query_dates

        result = []
        for temperature in temperatures_collection.find(query_temperatures):
            result.append({
                'id': temperature['id'],
                'valoare': temperature['valoare'],
                'timestamp': datetime.fromtimestamp(temperature['timestamp']).strftime('%Y-%m-%d'),
            })
        return jsonify(result), 200


@app.route('/api/temperatures/cities/<int:id_oras>', methods=['GET'])
def temperatures_cities(id_oras: int):
    args = request.args
    query_dates = {}
    query_temperatures = {
        'idOras': id_oras
    }

    if 'from' in args:
        query_dates['$gte'] = datetime.strptime(args['from'], '%Y-%m-%d').timestamp()
    if 'until' in args:
        query_dates['$lte'] = datetime.strptime(args['until'], '%Y-%m-%d').timestamp()
    if query_dates:
        query_temperatures['timestamp'] = query_dates

    result = []
    for temperature in temperatures_collection.find(query_temperatures):
        result.append({
            'id': temperature['id'],
            'valoare': temperature['valoare'],
            'timestamp': datetime.fromtimestamp(temperature['timestamp']).strftime('%Y-%m-%d'),
        })
    return jsonify(result), 200


@app.route('/api/temperatures/countries/<int:id_tara>', methods=['GET'])
def temperature_countries(id_tara: int):
    args = request.args
    query_dates = {}
    query_temperatures = {
        'idOras': { '$in': cities_collection.find( { 'id_tara': id_tara }, { 'id': 1 }).distinct('id') }
    }

    if 'from' in args:
        query_dates['$gte'] = datetime.strptime(args['from'], '%Y-%m-%d').timestamp()
    if 'until' in args:
        query_dates['$lte'] = datetime.strptime(args['until'], '%Y-%m-%d').timestamp()
    if query_dates:
        query_temperatures['timestamp'] = query_dates

    result = []
    for temperature in temperatures_collection.find(query_temperatures):
        result.append({
            'id': temperature['id'],
            'valoare': temperature['valoare'],
            'timestamp': datetime.fromtimestamp(temperature['timestamp']).strftime('%Y-%m-%d'),
        })
    return jsonify(result), 200


@app.route('/api/temperatures/<int:id>', methods=['PUT', 'DELETE'])
def temperature_id(id: int):
    if request.method == 'PUT':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if not temperatures_collection.find_one({ 'id': id }):
            return Response(status=404)
        if 'id' not in payload or 'idOras' not in payload or 'valoare' not in payload:
            return Response(status=400)
        if not isinstance(payload['id'], int) or not isinstance(payload['idOras'], int) or not isinstance(payload['valoare'], float):
            return Response(status=400)
        if payload['id'] != id:
            return Response(status=400)
        if not cities_collection.find_one( { 'id': payload['idOras'] } ):
            return Response(status=400)
        try:
            temperatures_collection.update_one( { 'id': id }, { '$set': { 'idOras': payload['idOras'], 'valoare': payload['valoare'] } } )
        except errors.DuplicateKeyError:
            return Response(status=409)
        return Response(status=200)
    elif request.method == 'DELETE':
        if not temperatures_collection.find_one({ 'id': id }):
            return Response(status=404)
        temperatures_collection.delete_one({ 'id': id})
        return Response(status=200)

if __name__ == '__main__':
    app.run('0.0.0.0', debug=True, port=80)