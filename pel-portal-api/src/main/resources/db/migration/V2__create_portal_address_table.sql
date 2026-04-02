create table portal_address (
    id uuid not null,
    cep varchar(8) not null,
    street varchar(255) not null,
    number varchar(20) not null,
    neighborhood varchar(100) not null,
    city varchar(100) not null,
    state varchar(2) not null,
    complement varchar(100),
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    constraint portal_address_pk primary key (id)
);

create index idx_portal_address_cep on portal_address(cep);
create index idx_portal_address_city_state on portal_address(city, state);