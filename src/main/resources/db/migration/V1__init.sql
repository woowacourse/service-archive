create table conversation (
    id bigint AUTO_INCREMENT not null,
    created_date datetime not null,
    updated_date datetime not null,
    conversation_time datetime,
    message longtext,
    member_id bigint,
    primary key (id)
);


create table reply (
    id bigint AUTO_INCREMENT not null,
    created_date datetime not null,
    updated_date datetime not null,
    message longtext,
    reply_time datetime,
    member_id bigint,
    conversation_id bigint,
    primary key (id)
);

create table member (
    id bigint not null auto_increment,
    created_date datetime not null,
    updated_date datetime not null,
    avatar longtext,
    display_name varchar(255),
    member_id varchar(255) not null,
    primary key (id)
);

alter table conversation
    add constraint fk_conversation_member
    foreign key (member_id)
    references member (id);

alter table reply
    add constraint fk_reply_conversation
    foreign key (conversation_id)
    references conversation (id);

alter table reply
   add constraint fk_reply_member
   foreign key (member_id)
   references member (id);