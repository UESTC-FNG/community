create table comment
(
	id BIGINT auto_increment,
	parent_id int not null,
	type int auto_increment,
	commentator int not null,
	gmt_create BIGINT not null,
	gmt_modified bigint not null,
	link_count bigint default 0,
	constraint comment_pk
		primary key (id)
);

comment on column comment.parent_id is '父问题 id';

comment on column comment.commentator is '评论人id';

comment on column comment.gmt_create is '创建时间';

comment on column comment.gmt_modified is '修改时间';