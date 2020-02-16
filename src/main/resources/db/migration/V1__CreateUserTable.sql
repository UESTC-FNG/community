create table user
(
	id int auto_increment,
	account_id varchar(100),
	name varchar(20),
	token char(36),
	GMTCREATE BIGINT,
	GMTMODIFIED BIGINT,
	constraint user_pk
		primary key (id)
);
