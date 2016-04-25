create table rules.t_rule_package(
  id bigint not null,
  package_name character varying(200) not null,
  description text,
  constraint pk_rule_package primary key(id)
);

create table rules.t_dslr_rule(
  id bigint not null,
  rule_name character varying(100) not null,
  rule_package_id bigint not null,
  definition text not null,
  constraint pk_dslr_rule primary key(id)
);

alter table rules.t_dslr_rule
  add constraint fk_dslr_rule_rule_package
  foreign key (rule_package_id)
  references rules.t_rule_package;

create table rules.t_dsl_expander (
  id bigint not null,
  expander_name character varying(100) not null,
  rule_package_id bigint not null,
  definition text not null,
  constraint pk_dsl_expaner primary key(id)
);

alter table rules.t_dsl_expander
  add constraint fk_dsl_expander_rule_package
  foreign key (rule_package_id)
  references rules.t_rule_package;

grant usage on schema rules to kv_user;
grant all privileges on all tables in schema rules to kv_user;
grant all privileges on all sequences in schema rules to kv_user;