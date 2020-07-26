ALTER TABLE conversation CHANGE user_id member_id bigint;

ALTER TABLE reply CHANGE user_id member_id bigint;

create table member (
    id bigint not null auto_increment,
    created_date datetime not null,
    updated_date datetime not null,
    avatar longtext,
    display_name varchar(255),
    member_id varchar(255) not null,
    primary key (id)
);

create table conversation_file (
    conversation_id bigint not null,
    url varchar(255)
);

create table reply_file (
    reply_id bigint not null,
    url varchar(255)
);

alter table conversation
    add constraint fk_conversation_member
    foreign key (member_id)
    references member (id);

alter table reply
   add constraint fk_reply_member
   foreign key (member_id)
   references member (id);

alter table conversation_file
    add constraint fk_file_conversation
    foreign key (conversation_id)
    references conversation (id);

alter table reply_file
    add constraint fk_file_reply
    foreign key (reply_id)
    references reply (id);