create table if not exists portal_document (
    id uuid not null,
    user_id uuid not null,
    document_type varchar(50) not null,
    original_filename text not null,
    filename text not null,
    size numeric not null,
    s3_file boolean not null default false,
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    constraint portal_document_pk primary key (id),
    constraint portal_document_user_fk foreign key (user_id) references portal_user(id) on delete cascade
);

create index idx_portal_document_user_id on portal_document(user_id);
create index idx_portal_document_document_type on portal_document(document_type);
