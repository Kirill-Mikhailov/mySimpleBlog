DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS posts CASCADE;

CREATE TABLE IF NOT EXISTS posts (
    id uuid PRIMARY KEY,
    title varchar(255),
    image_url text,
    text text,
    likes_count bigint default 0,
    last_change_timestamp timestamp
);

CREATE TABLE IF NOT EXISTS tags (
    post_id uuid REFERENCES posts (id) ON DELETE CASCADE,
    tag varchar(50)
);

CREATE TABLE IF NOT EXISTS comments (
    id uuid PRIMARY KEY,
    post_id uuid REFERENCES posts (id) ON DELETE CASCADE,
    text text,
    last_change_timestamp timestamp
);
