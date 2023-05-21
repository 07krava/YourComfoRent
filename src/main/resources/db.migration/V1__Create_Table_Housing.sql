create table housing (
                         id bigint not null auto_increment,
                         max_amount_people integer,
                         description varchar(255),
                         is_active BOOLEAN DEFAULT true,
                         price decimal(38,2),
                         title varchar(255), primary key (id));
