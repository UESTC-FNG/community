alter table QUESTION alter column ID bigint auto_increment;
alter table COMMENT alter column PARENT_ID bigint not null;

