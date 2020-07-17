create table conversation (
    id bigint AUTO_INCREMENT not null,
    created_date datetime not null,
    updated_date datetime not null,
    conversation_time datetime,
    message longtext,
    user_id varchar(255),
    primary key (id)
);


create table reply (
    id bigint AUTO_INCREMENT not null,
    created_date datetime not null,
    updated_date datetime not null,
    message longtext,
    reply_time datetime,
    user_id varchar(255),
    conversation_id bigint,
    primary key (id)
);


alter table reply
    add constraint fk_reply_conversation
    foreign key (conversation_id)
    references conversation (id);