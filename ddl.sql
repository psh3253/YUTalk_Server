CREATE DATABASE yutalk DEFAULT CHARACTER SET utf8;

CREATE USER 'yutalk'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'yutalk'@'%' IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON yutalk.* TO 'yutalk'@'localhost';
GRANT ALL PRIVILEGES ON yutalk.* TO 'yutalk'@'%';

CREATE TABLE yutalk.member
(
    user_id         VARCHAR(15)  NOT NULL PRIMARY KEY,
    password        VARCHAR(255) NOT NULL,
    name            VARCHAR(15)  NOT NULL,
    status_message  VARCHAR(15),
    register_time   TIMESTAMP    NOT NULL,
    last_login_time TIMESTAMP    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE yutalk.friend
(
    user_id   VARCHAR(15) NOT NULL,
    friend_id VARCHAR(15) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE yutalk.chat_room
(
    room_id      INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(15) NOT NULL,
    headcount    INTEGER     NOT NULL,
    last_message TEXT,
    make_time    TIMESTAMP   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE yutalk.chat_member
(
    user_id     VARCHAR(15) NOT NULL,
    room_id     INTEGER     NOT NULL,
    invite_time TIMESTAMP   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;



