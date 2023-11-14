CREATE table "company"
(
    id bigserial primary key,
    name varchar(500) not null,
    msrn bigint not null unique,
    description text,
    user_id bigint
);

insert into company (name, msrn, description, user_id)
values ('Company 1', '11111', 'Company 1 description', 1);