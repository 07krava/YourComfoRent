create table housing (
                         id bigint not null auto_increment,
                         quantity_bathrooms integer,
                         quantity_bedrooms integer,
                         quantity_beds integer,
                         description varchar(255),
                         housing_type varchar(255),
                         is_active BOOLEAN DEFAULT true,
                         max_amount_people integer,
                         price decimal(38,2),
                         title varchar(255), primary key (id));
