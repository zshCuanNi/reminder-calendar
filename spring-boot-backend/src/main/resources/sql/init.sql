drop database if exists memo;
create database memo;
use memo;

-- drop index deadlineIK on Memos;
drop table if exists Memos;
-- drop index usernameIK on User;
drop table if exists User;

create table User
(
    UID               int not null auto_increment,
    username          varchar(50),
    password               varchar(50),
    primary key (UID)
);

create index usernameIK on User
(
   username
);

create table Memos
(
    ID                   int not null auto_increment,
    UID                  int not null,
    deadline             datetime,
    headline             varchar(100),
    detail               varchar(100),
    primary key (ID)
);

create index deadlineIK on Memos
(
   deadline
);

alter table Memos add constraint FK_Memos foreign key (UID)
    references User (UID) on delete restrict on update restrict;