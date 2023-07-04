create table booking (
                         id bigint not null auto_increment,
                         end_date datetime(6) not null,
                         guests integer not null,
                         start_date datetime(6) not null,
                         housing_id bigint not null,
                         user_id bigint not null, primary key (id));

alter table booking add constraint FK52vua0nkv2hrl4e1kc5tstb3i foreign key (housing_id)
    references housing (id);

alter table booking add constraint FKkgseyy7t56x7lkjgu3wah5s3t foreign key (user_id)
    references user (id);
