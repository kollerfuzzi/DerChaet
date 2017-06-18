DROP TABLE Message;
DROP TABLE ChatUser;

CREATE TABLE ChatUser
(
	username VARCHAR(16) PRIMARY KEY,
	pwd VARCHAR(32) NOT NULL,
	email VARCHAR(50) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE Message
(
	id SERIAL PRIMARY KEY,
	sendtime TIMESTAMP NOT NULL,
	source_user VARCHAR(16) NOT NULL,
	message VARCHAR(500) NOT NULL
);

ALTER TABLE Message
ADD FOREIGN KEY (source_user)
REFERENCES ChatUser(username);

INSERT INTO ChatUser (username, pwd, email, enabled) VALUES('gg', 'grassmugg', 'gg@htlkaindorf.at', TRUE);
INSERT INTO ChatUser (username, pwd, email, enabled) VALUES('Elias', 'kollerfuzzi', 'kolelb14@htlkaindorf.at', TRUE);
INSERT INTO ChatUser (username, pwd, email, enabled) VALUES('Alexander', 'mayer', 'mayalb14@htlkaindorf.at', TRUE);
INSERT INTO Message (sendtime, source_user, message) VALUES(TO_TIMESTAMP('03.08.2007 08:33', 'DD.MM.YYYY HH:MI'), 'Elias', 'Hello');
INSERT INTO Message (sendtime, source_user, message) VALUES(TO_TIMESTAMP('03.08.2007 08:34', 'DD.MM.YYYY HH:MI'), 'Alexander', 'Hi');
INSERT INTO Message (sendtime, source_user, message) VALUES(TO_TIMESTAMP('03.08.2007 08:35', 'DD.MM.YYYY HH:MI'), 'gg', 'Smoke W33D');

