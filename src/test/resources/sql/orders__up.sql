DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    id          varchar(36) primary key  not null,
    user_id     varchar(46)              not null,
    description varchar(2024)            not null,
    created_at  timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

CREATE INDEX orders_user_id_index ON orders (user_id);

INSERT INTO orders (id, user_id, description, created_at, updated_at)
VALUES ('028cd14d-a85f-40d8-b406-2887586db371', '4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8',
        'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s',
        current_timestamp, current_timestamp),
       ('558bcdac-fbfd-4819-ad0e-f55310ea9e31', '4fbcfc50-7dda-4c1c-b358-30e70cb8b6d8',
        'Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of',
        current_timestamp, current_timestamp),
       ('01d45da1-4b83-43b3-a40e-94a383fb9892', '2249fa0d-766d-41e5-9b7a-2ca0961d1ab6',
        'All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet.',
        current_timestamp, current_timestamp),
       ('532cd78f-3289-4f64-8307-5664a2645e07', '2249fa0d-766d-41e5-9b7a-2ca0961d1ab6',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit,', current_timestamp,
        current_timestamp),
       ('a0e21a4a-5d2b-49dd-a4c0-c6b3e56d047b', '2249fa0d-766d-41e5-9b7a-2ca0961d1ab6',
        'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo',
        current_timestamp, current_timestamp),
       ('44cf0f62-b76f-4372-83f7-99ce1b5b96f9', '6e8393d1-1f0d-4d11-9708-2ef3ad1ae060',
        'Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum',
        current_timestamp, current_timestamp);
