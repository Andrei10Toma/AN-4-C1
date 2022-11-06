from flask import Flask, jsonify, request, Response

app = Flask(__name__)
movies = []
id_counter = 1

@app.route('/movies', methods=['GET', 'POST'])
def get_movies():
    global id_counter
    global movies
    if request.method == 'GET':
        return jsonify(movies), 200
    elif request.method == 'POST':
        payload = request.get_json(silent=True)
        if not payload:
            return Response(status=400)
        if not payload['nume']:
            return Response(status=400)
        movies.append({
            'id': id_counter,
            'nume': payload['nume'],
        })
        id_counter += 1
        return Response(status=201)


def search_rename_movie(id: int, nume: str):
    for movie in movies:
        if movie['id'] == id:
            movie['nume'] = nume
            return movie
    return None


def search_movie(id: int):
    for movie in movies:
        if movie['id'] == id:
            return movie
    return None


def delete_movie(id: int):
    for i in range(0, len(movies)):
        if movies[i]['id'] == id:
            del movies[i]
            return 1
    return 0

@app.route('/movie/<int:id>', methods=['GET', 'PUT', 'DELETE'])
def get_movie(id: int):
    if request.method == 'PUT':
        payload = request.get_json(silent=True)
        if not payload or not payload['nume']:
            return Response(status=400)
        movie = search_rename_movie(id, payload['nume'])
        if not movie:
            return Response(status=404)
        return Response(status=200)
    elif request.method == 'GET':
        movie = search_movie(id)
        if not movie:
            return Response(status=404)
        return jsonify(movie), 200
    elif request.method == 'DELETE':
        r = delete_movie(id)
        if r == 0:
            return Response(status=404)
        return Response(status=200)


@app.route('/reset')
def reset():
    global movies
    global id_counter
    movies = []
    id_counter = 0
    return Response(status=200)


if __name__ == '__main__':
    movies = []
    id_counter = 1
    app.run('127.0.0.1', debug=True)
