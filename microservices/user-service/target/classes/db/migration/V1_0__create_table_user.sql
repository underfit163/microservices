CREATE table "user"
(
    id bigserial primary key,
    name varchar(500) not null,
    login varchar(256) not null,
    password varchar(256) not null,
    email varchar(256) not null,
    enabled bool default true,
    company_id bigint
);

insert into "user" (name, login, password, email, company_id)
values ('Test user 1', '11111', '123456', 'user1@gmal.com', 1);