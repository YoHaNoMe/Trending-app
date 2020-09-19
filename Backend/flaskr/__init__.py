from flask import Flask, request, abort, jsonify
# from flask_uuid import FlaskUUID
from models import setup_db, User, Favourite
from flask_login import LoginManager
from flask_login import current_user
from flask_login import login_user, login_required, logout_user

# Define loginManager
login_manager = LoginManager()


def create_app():
    # Define application
    app = Flask(__name__)
    # Define Secret Key
    app.config['SECRET_KEY'] = b'WS)\xe1~E\x12\xfc\xfe\xd8\xce\xdb4\x89\xd74'
    # Setup database
    setup_db(app)

    # Setup UUID for urls
    # FlaskUUID(app)

    # Define loginManager
    # login_manager.init_app(app)

    '''
    LoginManager
    '''
    # @login_manager.user_loader
    # def load_user(user_id):
    #     return User.query.get(user_id)

    '''
    Main Route
    '''
    @app.route('/')
    def index():
        return 'Hello world'

    '''
    Get User Route
    '''
    @app.route('/users/<uuid:user_id>')
    # @login_required
    def get_user(user_id):
        user = User.query.get(user_id)
        return jsonify({
            "success": True,
            "status_code": 200,
            "user": user.long_format()
        })

    '''
    Register Route
    '''
    @app.route('/register', methods=['POST'])
    def register():
        # Get user data
        user_data = request.get_json()

        # Check if there is not request
        if not user_data:
            abort(400)

        # Check that the request has the correct arguments
        if not (
            'name' in user_data and
            'userName' in user_data and
            'email' in user_data and
            'password' in user_data and
            'imageUrl' in user_data
        ):
            abort(400)

        name = user_data['name']
        userName = user_data['userName']
        email = user_data['email']
        password = user_data['password']
        imageUrl = user_data['imageUrl']

        # Check that non of the fields are empty
        if not (
            name and
            userName and
            email and
            password and
            imageUrl
        ):
            abort(400)

        user = User(name, email, userName, password, imageUrl)
        try:
            user.insert()
            # login_user(user)
            return jsonify({
                'status_code': 201,
                "success": True,
                "user": user.long_format()
            })
        except Exception as e:
            print(e)
            abort(400)

    '''
    Login Route
    '''
    @app.route('/login', methods=['POST'])
    def login():
        # Get user data
        user_data = request.get_json()

        # Check that there is request
        if not user_data:
            abort(400)

        # Check that the request has the correct arguments
        if not (
            'userName' in user_data and
            'password' in user_data
        ):
            abort(400)

        # Check that all the fields are not empty
        if not (
            user_data['userName'] and
            user_data['password']
        ):
            abort(400)

        # Get the user
        user = User.query.filter_by(userName=user_data['userName']).first()

        # Check that user exists
        if not user:
            abort(404)

        # Check that the password is correct
        if not user.check_user(user_data['password']):
            print('sadlasdlasl')
            abort(403)

        # Login user
        # login_user(user)

        return jsonify({
            "success": True,
            "status_code": 200,
            "user": user.long_format()
        })

    '''
    Update User Route
    '''
    @app.route('/users/<uuid:user_id>', methods=['PATCH'])
    def update_user(user_id):
        # Get user
        user = User.query.get(user_id)

        # Get user data from json
        user_data = request.get_json()

        # Check if there is request
        if not user_data:
            abort(400)

        # Check that the request has at least one correct field |
        # Check that at least one field is not empty
        if not (
            ('name' in user_data and user_data['name']) or
            ('email' in user_data and user_data['email']) or
            ('password' in user_data and user_data['password']) or
            ('imageUrl' in user_data and user_data['imageUrl'])
        ):
            abort(400)

        # Extract data
        name = user_data['name'] if 'name' in user_data else user.name

        email = user_data['email'] if 'email' in user_data else user.email

        imageUrl = user_data['imageUrl'] if 'imageUrl' in user_data else user.imageUrl

        if 'password' in user_data:
            password = user.encrypt_password(user_data['password'])
        else:
            password = user.password

        # update user
        user.name = name
        user.email = email
        user.imageUrl = imageUrl
        user.password = password

        try:
            user.update()
            return jsonify({
                'success': True,
                'status_code': 200,
                'user': user.long_format()
            })
        except Exception as e:
            print(e)
            abort(422)

    '''
    Logout Route
    '''
    # @app.route('/logout')
    # @login_required
    # def logout():
    #     logout_user()
    #     return jsonify({
    #         "success": True,
    #         "status_code": 200,
    #     })

    '''
    Get Favourites route
    '''
    @app.route('/users/<uuid:user_id>/favourites')
    def get_favourites(user_id):
        try:
            user = User.query.get_or_404(user_id)
            favourites = [fav.format() for fav in user.favourites]
            return jsonify({
                'success': True,
                'status_code': 200,
                'favourites': favourites,
            })
        except Exception as e:
            print(e)
            abort(422)

    '''
    Post Favourites route
    '''
    @app.route('/users/<uuid:user_id>/favourites', methods=['POST'])
    def post_favourites(user_id):
        # Get the user or return 404 not found
        user = User.query.get_or_404(user_id)
        # Get the data
        data = request.get_json()

        # Check that there is data
        if not data:
            abort(400)

        # Check that the data has the correct fields
        if not(
            ('movie_tv_id' in data and isinstance(data['movie_tv_id'], int) and data['movie_tv_id']) or
            ('media_type' in data and isinstance(data['media_type'], str) and data['media_type'])
          ):
            abort(400)

        print(data)
        try:
            favourite = Favourite(data['movie_tv_id'], data['media_type'])
            user.favourites.append(favourite)
            favourite.insert()
            return jsonify({
                "success": True,
                "status_code": 200,
                "favourite": favourite.format()
            })
        except Exception as e:
            print('Error ', e)
            abort(422)

    '''
    Delete Favourite route
    '''
    @app.route('/users/<uuid:user_id>/favourites/<int:fav_id>', methods=['DELETE'])
    def delete_favourites(user_id, fav_id):
        # Get user or return 404 not found
        print(type(user_id), fav_id)
        user = User.query.get_or_404(user_id)
        print('HEREEEEEEEEEEEEE')
        # Get favourite or return 404 not found
        favourite = Favourite.query.filter_by(movie_tv_id=fav_id).first_or_404()
        print('HEREEEEEEEEEEEEE1234')
        try:
            user.favourites.remove(favourite)
            favourite.delete()
            return jsonify({
                "success": True,
                "status_code": 200,
                "favourite_deleted_id": fav_id
            })
        except Exception as e:
            print(e)
            abort(422)

    '''
    Error Handler
    '''
    def get_error_msg(status_code, msg):
        return jsonify({
            "success": False,
            "status_code": status_code,
            "message": msg
        })

    @app.errorhandler(400)
    def bad_request(e):
        return (get_error_msg(400, 'Bad Request'), 400)

    @app.errorhandler(422)
    def processable(e):
        return (get_error_msg(422, 'Unprocessable'), 422)

    @app.errorhandler(404)
    def not_found(e):
        return (get_error_msg(404, 'Not found'), 404)

    @app.errorhandler(401)
    def unauthorized(e):
        return (get_error_msg(
            401, 'You don\'t have Authorization to access this endpoint'), 401)

    return app


if __name__ == '__main__':
    create_app().run()
