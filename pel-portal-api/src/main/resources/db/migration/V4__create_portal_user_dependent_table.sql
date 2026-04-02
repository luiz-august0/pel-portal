create table if not exists portal_user_dependent (
    id uuid not null,
    user_id uuid not null,
    dependent_id uuid not null,
    dependent_relationship varchar(50),
    from_link boolean default false,
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    constraint portal_user_dependent_pk primary key (id),
    constraint portal_user_dependent_fk_user foreign key (user_id) references portal_user(id) on delete cascade,
    constraint portal_user_dependent_fk_dependent foreign key (dependent_id) references portal_user(id) on delete cascade,
    constraint portal_user_dependent_un_user_dependent unique (user_id, dependent_id)
);

create index idx_portal_user_dependent_user_id on portal_user_dependent(user_id);
create index idx_portal_user_dependent_dependent_id on portal_user_dependent(dependent_id);
