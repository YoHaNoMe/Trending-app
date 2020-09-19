from flask_sqlalchemy import SQLAlchemy
from flask_login import UserMixin
from sqlalchemy.dialects.postgresql import UUID
import uuid
import bcrypt
import hashlib

db = SQLAlchemy()


def setup_db(app):
    app.config['SQLALCHEMY_DATABASE_URI'] = 'postgres:///movieDiscussion'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    db.app = app
    db.init_app(app)
    db.create_all()


class User(UserMixin, db.Model):
    __tablename__ = "users"
    id = db.Column(UUID(as_uuid=True), default=uuid.uuid4, primary_key=True)
    name = db.Column(db.String, nullable=False)
    userName = db.Column(db.String, unique=True, nullable=False)
    email = db.Column(db.String, unique=True, nullable=False)
    password = db.Column(db.Binary, nullable=False)
    imageUrl = db.Column(db.String, nullable=False)
    favourites = db.relationship('Favourite', backref='user', lazy=True)

    def __init__(self, name, email, userName, password, imageUrl):
        # self.id = uuid.uuid4()
        self.name = name
        self.email = email
        self.userName = userName
        self.password = self.encrypt_password(password)
        self.imageUrl = imageUrl

    def insert(self):
        db.session.add(self)
        db.session.commit()

    def delete(self):
        db.session.delete(self)
        db.session.commit()

    def update(self):
        db.session.commit()

    def encrypt_password(self, password):
        return bcrypt.hashpw(
            password.encode('utf-8'), bcrypt.gensalt()
        )

    def check_user(self, given_password):
        if bcrypt.checkpw(given_password.encode('utf-8'), self.password):
            print('return True')
            return True
        print('return False')
        return False

    def format(self):
        return {"id": self.id}

    def long_format(self):
        return {
            "id": self.id,
            "name": self.name,
            "userName": self.userName,
            "email": self.email,
            "imageUrl": self.imageUrl
        }


class Favourite(db.Model):
    __tablename__ = 'favourites'
    id = db.Column(db.Integer, primary_key=True)
    movie_tv_id = db.Column(db.Integer, nullable=False)
    media_type = db.Column(db.String, nullable=False)
    user_id = db.Column(UUID(as_uuid=True), db.ForeignKey('users.id'), nullable=False)

    def __init__(self, movie_tv_id, media_type):
        self.movie_tv_id = movie_tv_id
        self.media_type = media_type

    def insert(self):
        db.session.add(self)
        db.session.commit()

    def delete(self):
        db.session.delete(self)
        db.session.commit()

    def format(self):
        return {
            'id': self.id,
            'movie_tv_id': self.movie_tv_id,
            'media_type': self.media_type,
        }
