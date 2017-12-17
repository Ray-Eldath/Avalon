CREATE TABLE group_ (
  id             INTEGER PRIMARY KEY AUTOINCREMENT,
  time           DATETIME,
  senderUid      INTEGER,
  senderNickName TEXT,
  groupUid       INTEGER,
  groupName      TEXT,
  content        TEXT
);

CREATE TABLE friend_ (
  id             INTEGER PRIMARY KEY AUTOINCREMENT,
  time           DATETIME,
  senderUid      INTEGER,
  senderNickName TEXT,
  content        TEXT
);