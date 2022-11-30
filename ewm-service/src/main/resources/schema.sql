CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS categories
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                           NOT NULL,
    CONSTRAINT pk_categories primary key (id),
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS events
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(500)                       NOT NULL,
    category_id BIGINT,
    confirmed_Requests int,
    created_On TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    description VARCHAR(4000)                    NOT NULL,
    event_Date TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    initiator_id BIGINT,
    lat BIGINT,
    lon BIGINT,
    paid boolean                                 NOT NULL,
    participant_Limit BIGINT,
    published_On TIMESTAMP WITHOUT TIME ZONE,
    request_Moderation boolean,
    state_event VARCHAR(100),
    title VARCHAR(4000)                           NOT NULL,
    views INT,

    CONSTRAINT pk_events primary key (id),
    CONSTRAINT FK_EVENT_OWNER FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS requests
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    event_id BIGINT,
    requester_id BIGINT,
    state VARCHAR(100),
    CONSTRAINT pk_requests primary key (id),
    CONSTRAINT FK_requests FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT UQ_event_id_requester_id UNIQUE (event_id, requester_id)
    );

CREATE TABLE IF NOT EXISTS compilations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned boolean,
    title VARCHAR(4000)                         NOT NULL,
    CONSTRAINT pk_compilations primary key (id)
    );

CREATE TABLE IF NOT EXISTS compilations_events
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT,
    compilations_id BIGINT,
    CONSTRAINT pk_compilations_events primary key (id),
    CONSTRAINT FK_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT FK_compilations FOREIGN KEY (compilations_id) REFERENCES compilations (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment VARCHAR(280)                         NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    event_id BIGINT,
    user_id BIGINT,
    CONSTRAINT pk_comments primary key (id),
    CONSTRAINT FK_events_comments FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT FK_users_comments FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments_comments
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comments_id1 BIGINT,
    comments_id2 BIGINT,
    CONSTRAINT pk_comments_comments primary key (id),
    CONSTRAINT FK_comments_id1 FOREIGN KEY (comments_id1) REFERENCES comments (id) ON DELETE CASCADE,
    CONSTRAINT FK_comments_id2 FOREIGN KEY (comments_id2) REFERENCES comments (id) ON DELETE CASCADE
    );





