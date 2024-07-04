drop table member if exists cascade;
create table member (
 id bigint generated by default as identity,
 login_id varchar(20),
 password varchar(20),
 name varchar(20),
 primary key (id)
);

drop table quiz if exists cascade;
create table quiz (
 id bigint generated by default as identity,
 member_id bigint not null,
 quiz_id bigint not null,
 question varchar(255) not null,
 answer varchar(255) not null,
 primary key (id),
 foreign key (member_id) references member(id)
);

drop table bookmark if exists cascade;
create table bookmark (
 id bigint generated by default as identity,
 member_id bigint not null,
 quiz_id bigint not null,
 primary key (id),
 foreign key (member_id) references member(id),
 foreign key (quiz_id) references quiz(id)
);
