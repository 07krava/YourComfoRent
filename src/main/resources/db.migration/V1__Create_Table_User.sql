create table user (
                      id bigint not null auto_increment,
                      email varchar(255),
                      password varchar(255),
                      phone varchar(255),
                      username varchar(255), primary key (id));
