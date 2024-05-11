DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         varchar(36) primary key  not null,
    username   varchar(255)             not null,
    password   varchar(255),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

DROP TABLE IF EXISTS user_profiles;

CREATE TABLE IF NOT EXISTS user_profiles
(
    user_id    varchar(36) primary key  not null,
    full_name  varchar(255)             not null,
    last_name  varchar(255),
    updated_at timestamp with time zone not null
);

INSERT INTO users (id, username, password, created_at, updated_at)
VALUES ('4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8', 'user1', '123456', current_timestamp, current_timestamp),
        ('2249fa0d-766d-41e5-9b7a-2ca0961d1ab6', 'user2', '234567', current_timestamp, current_timestamp),
        ('84409556-a609-4375-b5b6-44678009d193', 'user3', '345678', current_timestamp, current_timestamp),
        ('2a03be2d-386d-45d6-bba2-8ce8afd81c5f', 'user4', '456789', current_timestamp, current_timestamp),
        ('c0ec4c02-e22d-457a-982f-0f2b719d8142', 'user5', '567890', current_timestamp, current_timestamp),
        ('6e8393d1-1f0d-4d11-9708-2ef3ad1ae060', 'user6', '112345', current_timestamp, current_timestamp);

INSERT INTO user_profiles (user_id, full_name, last_name, updated_at)
VALUES
        ('4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8', 'full name 1', 'last name 1', current_timestamp),
        ('2249fa0d-766d-41e5-9b7a-2ca0961d1ab6',  'full name 2', 'last name 2', current_timestamp),
        ('84409556-a609-4375-b5b6-44678009d193',  'full name 3', 'last name 3', current_timestamp),
        ('2a03be2d-386d-45d6-bba2-8ce8afd81c5f',  'full name 4', 'last name 4', current_timestamp),
        ('c0ec4c02-e22d-457a-982f-0f2b719d8142',  'full name 5', 'last name 5', current_timestamp),
        ('6e8393d1-1f0d-4d11-9708-2ef3ad1ae060',  'full name 6', 'last name 6', current_timestamp);
