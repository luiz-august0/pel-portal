create table if not exists portal_user_details (
    id uuid not null,
    birth_date timestamp not null,
    phone varchar(11),
    special_needs boolean not null,
    program_knowledge_source varchar(50) not null,
    program_knowledge_source_other varchar(255),
    internal_relationship_type varchar(50),
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    constraint portal_user_details_pk primary key (id)
);