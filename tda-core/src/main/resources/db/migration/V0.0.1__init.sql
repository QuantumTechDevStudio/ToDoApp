CREATE TABLE tda_user
(
    uuid    TEXT PRIMARY KEY,
    name    TEXT NOT NULL,
    surname TEXT NOT NULL
);

COMMENT ON COLUMN tda_user.uuid IS 'UUID пользователя';
COMMENT ON COLUMN tda_user.name IS 'Имя пользователя';
COMMENT ON COLUMN tda_user.surname IS 'Фамилия пользователя';

CREATE TABLE tda_task
(
    id          BIGSERIAL PRIMARY KEY,
    description TEXT                            NOT NULL,
    datetime    TIMESTAMP                       NOT NULL,
    user_uuid   TEXT REFERENCES tda_user (uuid) NOT NULL
);

COMMENT ON COLUMN tda_task.id IS 'ID задачи';
COMMENT ON COLUMN tda_task.description IS 'Описание задачи';
COMMENT ON COLUMN tda_task.datetime IS 'Дата и время задачи';
COMMENT ON COLUMN tda_task.user_uuid IS 'UUID пользователя, который создал задачу';

CREATE TABLE tda_request
(
    request_uuid TEXT PRIMARY KEY,
    type         TEXT,
    datetime     TIMESTAMP NOT NULL
);

COMMENT ON COLUMN tda_request.request_uuid IS 'UUID запроса';
COMMENT ON COLUMN tda_request.datetime IS 'Временная метка запроса';
COMMENT ON COLUMN tda_request.type IS 'Тип запроса';