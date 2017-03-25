CREATE TABLE GroupMessage(
  id INTEGER PRIMARY KEY,
  time DATETIME,
  senderUid INTEGER,
  senderNickName TEXT,
  groupUid INTEGER,
  groupName TEXT,
  content TEXT
);

CREATE TABLE FriendMessage(
  id INTEGER PRIMARY KEY,
  time DATETIME,
  senderUid INTEGER,
  senderNickName TEXT,
  content TEXT
);