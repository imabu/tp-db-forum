CREATE EXTENSION IF NOT EXISTS CITEXT;

CREATE TABLE IF NOT EXISTS users (
  about TEXT,
  email CITEXT UNIQUE,
  fullname TEXT,
  nickname CITEXT  PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS forums (
  posts INTEGER DEFAULT 0,
  slug CITEXT PRIMARY KEY NOT NULL,
  threads INTEGER DEFAULT 0,
  title TEXT NOT NULL,
  "user" CITEXT  NOT NULL REFERENCES users (nickname)
);

CREATE TABLE IF NOT EXISTS threads (
  author CITEXT  NOT NULL REFERENCES users (nickname),
  created TIMESTAMP,
  forum CITEXT NOT NULL REFERENCES forums (slug),
  id SERIAL PRIMARY KEY,
  message TEXT,
  slug CITEXT UNIQUE,
  title TEXT NOT NULL,
  votes INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS uservotes (
  nickname CITEXT NOT NULL REFERENCES users (nickname),
  thread int REFERENCES threads(id),
  voice INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS posts (
  author CITEXT NOT NULL REFERENCES users (nickname),
  created TIMESTAMP,
  forum CITEXT NOT NULL REFERENCES forums (slug),
  id SERIAL PRIMARY KEY,
  isEdited BOOLEAN DEFAULT FALSE,
  message TEXT,
  parent INTEGER DEFAULT 0,
  thread INTEGER REFERENCES threads (id),
  path int ARRAY
);