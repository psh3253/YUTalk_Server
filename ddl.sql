CREATE DATABASE yutalk DEFAULT CHARACTER SET utf8;

CREATE USER 'yutalk'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'yutalk'@'%' IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON yutalk.* TO 'yutalk'@'localhost';
GRANT ALL PRIVILEGES ON yutalk.* TO 'yutalk'@'%';

CREATE TABLE yutalk.member
(
    user_id VARCHAR(15) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(20) NOT NULL,
    status_message VARCHAR(60),
    register_time TIMESTAMP NOT NULL,
    last_login_time TIMESTAMP NOT NULL
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;