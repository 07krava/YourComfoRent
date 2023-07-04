create table image (
                       id bigint not null auto_increment,
                       data mediumblob not null,
                       file_name varchar(255),
                       housing_id bigint, primary key (id));


alter table image add constraint FKinf8aqvu8vwy2sx4cel8g7jhu foreign key (housing_id)
    references housing (id);
