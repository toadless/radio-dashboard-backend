CREATE TABLE IF NOT EXISTS discord_tokens
(
    user_id BIGINT NOT NULL PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    access_token VARCHAR NOT NULL,
    refresh_token VARCHAR NOT NULL,
    expiry TIMESTAMP NOT NULL
);