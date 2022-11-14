CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS stats
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255)                           NOT NULL,
    uri VARCHAR(512)                           NOT NULL,
    hits bigint,
    CONSTRAINT pk_stats primary key (id),
    CONSTRAINT UQ_APP_URI UNIQUE (app, uri)
);

CREATE TABLE IF NOT EXISTS hits
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255)                           NOT NULL,
    uri VARCHAR(512)                           NOT NULL,
    ip VARCHAR(512)                            NOT NULL,
    created timestamp without time zone        NOT NULL,
    stats_id bigint,
    CONSTRAINT pk_hits PRIMARY KEY (id),
    CONSTRAINT fk_hits_stats FOREIGN KEY (stats_id) references stats (id) ON DELETE CASCADE
);








