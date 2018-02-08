CREATE TABLE group_ (
  id             INTEGER PRIMARY KEY AUTO_INCREMENT,
  time           DATETIME,
  senderUid      INTEGER,
  senderNickName TEXT,
  groupUid       INTEGER,
  groupName      TEXT,
  content        TEXT
);

CREATE TABLE friend_ (
  id             INTEGER PRIMARY KEY AUTO_INCREMENT,
  time           DATETIME,
  senderUid      INTEGER,
  senderNickName TEXT,
  content        TEXT
);

CREATE TABLE quote_ (
  id             INTEGER PRIMARY KEY AUTO_INCREMENT,
  uid            INTEGER,
  speaker        TEXT,
  content        TEXT
);