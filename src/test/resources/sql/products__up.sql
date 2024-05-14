DROP TABLE IF EXISTS products;
CREATE TABLE products
(
    id          varchar(36) primary key  not null,
    brand_id    varchar(36),
    article     varchar(255)             not null,
    name        varchar(1024)            not null,
    description varchar(4096)            not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null
);

DROP TABLE IF EXISTS brands;
CREATE TABLE brands
(
    id          varchar(36) primary key  not null,
    name        varchar(1024)            not null,
    description varchar(4096)            not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null
);

DROP TABLE IF EXISTS media;
CREATE TABLE media
(
    id             varchar(36) primary key  not null,
    reference_type varchar(255)             not null,
    reference_id   varchar(36)              not null,
    collection     varchar(255),
    name           varchar(1024),
    description    varchar(4096),
    created_at     timestamp with time zone not null,
    updated_at     timestamp with time zone not null
);

CREATE INDEX IF NOT EXISTS media_reference_index ON media (reference_type, reference_id);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories
(
    id         varchar(36) primary key  not null,
    name       varchar(1024)            not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category
(
    product_id  varchar(36) not null,
    category_id varchar(36) not null
);

CREATE INDEX IF NOT EXISTS product_category_product_id_category_id_index ON product_category (product_id, category_id);


INSERT INTO brands (id, name, description, created_at, updated_at)
VALUES ('00c146fd-12f2-413b-b7b5-cbd5857586d6', 'brand a',
        'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo',
        current_timestamp, current_timestamp),
       ('55f88058-7c11-4b34-af7c-eee23ee97e22', 'best brand 1',
        'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt',
        current_timestamp,
        current_timestamp),
       ('add83dbb-ec7f-4853-b770-d59d394625ac', 'best brand 2',
        'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem',
        current_timestamp,
        current_timestamp),
       ('7d53d7e4-3abf-43bf-83d4-78c3f2f92bde', 'trash brand',
        'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?',
        current_timestamp,
        current_timestamp),
       ('9b810916-b7c0-48b6-a9c9-e1905d82848f', 'super brand',
        'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',
        current_timestamp,
        current_timestamp)
;

INSERT INTO products (id, brand_id, article, name, description, created_at, updated_at)
VALUES ('116d9889-cc74-47da-b37b-3570ca4acb2b', '00c146fd-12f2-413b-b7b5-cbd5857586d6', 'spn111111',
        'some product name 1', 'some very long description 1', current_timestamp,
        current_timestamp),
       ('a8eb7881-0ff8-4ce8-8565-7583d17081f9', '00c146fd-12f2-413b-b7b5-cbd5857586d6', 'spn111112',
        'some product name 2', 'some very long description 2', current_timestamp,
        current_timestamp),
       ('43c92224-2a55-4ee8-bd83-31ac69c07c04', '55f88058-7c11-4b34-af7c-eee23ee97e22', 'spn111113',
        'some product name 3', 'some very long description 3', current_timestamp,
        current_timestamp),
       ('3e5a1062-9653-48ce-bac1-8fbfdad08175', '55f88058-7c11-4b34-af7c-eee23ee97e22', 'spn111114',
        'some product name 4', 'some very long description 4', current_timestamp,
        current_timestamp),
       ('744be470-0de0-484d-85e4-5939653c1561', 'add83dbb-ec7f-4853-b770-d59d394625ac', 'spn111115',
        'some product name 5', 'some very long description 5', current_timestamp,
        current_timestamp),
       ('556f75bc-c855-4339-ad75-5c9c8b3bd35b', '7d53d7e4-3abf-43bf-83d4-78c3f2f92bde', 'spn111116',
        'some product name 6', 'some very long description 6', current_timestamp,
        current_timestamp),
       ('34a6453a-df08-4f52-90ca-a3e3e7d38583', '9b810916-b7c0-48b6-a9c9-e1905d82848f', 'spn111117',
        'some product name 7', 'some very long description 7', current_timestamp,
        current_timestamp)
;

INSERT INTO media (id, reference_type, reference_id, collection, name, description, created_at,
                   updated_at)
VALUES ('d12bc519-e4bb-4f3f-95c4-a54f76a47dd6', 'org.do6po.cicero.test.integration.model.ProductM',
        '116d9889-cc74-47da-b37b-3570ca4acb2b',
        'collection1', '', '', current_timestamp, current_timestamp),
       ('7991bcfa-0350-4ee2-b560-f23016baafd0', 'org.do6po.cicero.test.integration.model.ProductM',
        '116d9889-cc74-47da-b37b-3570ca4acb2b',
        'collection1', '', '', current_timestamp, current_timestamp),
       ('37a830f1-b127-4ff9-b009-09d321b5e031', 'org.do6po.cicero.test.integration.model.ProductM',
        '116d9889-cc74-47da-b37b-3570ca4acb2b',
        'collection2', '', '', current_timestamp, current_timestamp),
       ('9a223c97-6c4c-4cd5-a24e-c76461fa0970', 'org.do6po.cicero.test.integration.model.ProductM',
        'a8eb7881-0ff8-4ce8-8565-7583d17081f9',
        'collection3', '', '', current_timestamp, current_timestamp),
       ('2c60f753-91a4-420d-83b3-f24b1b12ca2a', 'org.do6po.cicero.test.integration.model.BrandM',
        '00c146fd-12f2-413b-b7b5-cbd5857586d6',
        '', '', '', current_timestamp, current_timestamp)
;

INSERT INTO categories (id, name, created_at, updated_at)
VALUES ('47d347e4-0c7b-47ce-975f-4f4f92b9ca0a', 'category first', current_timestamp,
        current_timestamp),
       ('61252e97-80f4-4212-9ecb-76f3f7487718', 'category second', current_timestamp,
        current_timestamp),
       ('4a52e36c-3d78-4c4b-a9e5-569251ea74ab', 'category third', current_timestamp,
        current_timestamp),
       ('b1771a71-e956-4b13-a89b-eaa0be296866', 'category forth', current_timestamp,
        current_timestamp)
;

INSERT INTO product_category (product_id, category_id)
VALUES ('116d9889-cc74-47da-b37b-3570ca4acb2b', '47d347e4-0c7b-47ce-975f-4f4f92b9ca0a'),
       ('116d9889-cc74-47da-b37b-3570ca4acb2b', '61252e97-80f4-4212-9ecb-76f3f7487718'),
       ('a8eb7881-0ff8-4ce8-8565-7583d17081f9', '47d347e4-0c7b-47ce-975f-4f4f92b9ca0a'),
       ('a8eb7881-0ff8-4ce8-8565-7583d17081f9', '61252e97-80f4-4212-9ecb-76f3f7487718'),
       ('a8eb7881-0ff8-4ce8-8565-7583d17081f9', '4a52e36c-3d78-4c4b-a9e5-569251ea74ab'),
       ('43c92224-2a55-4ee8-bd83-31ac69c07c04', '4a52e36c-3d78-4c4b-a9e5-569251ea74ab'),
       ('34a6453a-df08-4f52-90ca-a3e3e7d38583', '4a52e36c-3d78-4c4b-a9e5-569251ea74ab')
;