CREATE TABLE friend_ (
  id            INT(11)             NOT NULL AUTO_INCREMENT,
  time          DATETIME            NOT NULL DEFAULT '0000-00-00 00:00:00',
  senderUid     BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  senderNicName TINYTEXT            NOT NULL,
  content       TEXT                NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = MyISAM, DEFAULT CHARSET = utf8;
CREATE TABLE group_ (
  id             INT(11)             NOT NULL AUTO_INCREMENT,
  time           DATETIME            NOT NULL DEFAULT '0000-00-00 00:00:00',
  senderUid      BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  senderNickName TINYTEXT            NOT NULL,
  groupUid       BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  groupName      TINYTEXT            NOT NULL,
  content        TEXT                NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = MyISAM, DEFAULT CHARSET = utf8;