create table location (
                          housing_id bigint not null,
                          apartment_number integer,
                          city varchar(255),
                          country varchar(255),
                          house_number integer,
                          region varchar(255),
                          street varchar(255),
                          zip_code varchar(255), primary key (housing_id));

alter table location add constraint FKerymo068uh6mbfcig9btpvdnt foreign key (housing_id)
    references housing (id);

