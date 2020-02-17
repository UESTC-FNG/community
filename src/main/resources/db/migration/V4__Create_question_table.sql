create table question
(
	id int auto_increment,
	title varchar(50),
	description text,
	gmtcreate bigint,
	gmtmodified bigint,
	creator int,
	commitcount int,
	viewcount int,
	likecount int,
	tag varchar(256),
	constraint question_pk
		primary key (id)
);