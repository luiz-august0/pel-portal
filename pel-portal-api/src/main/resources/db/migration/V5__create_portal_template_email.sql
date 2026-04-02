create table if not exists portal_template_email (
    id uuid not null,
    template_email varchar(80) not null,
    html text not null
);