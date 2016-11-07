# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table announcement (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  creator_id                bigint,
  prompt_id                 bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_announcement primary key (id))
;

create table answer_decimal (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  answer                    double,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_decimal primary key (id))
;

create table answer_integer (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  answer                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_integer primary key (id))
;

create table answer_text (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  answer                    varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_text primary key (id))
;

create table answer_word (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  answer                    varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_word primary key (id))
;

create table blank (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  passage_id                bigint,
  start_index               integer,
  end_index                 integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_blank primary key (id))
;

create table choice (
  id                        bigint auto_increment not null,
  question_part_id          bigint not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  choice_type               integer,
  is_correct                tinyint(1) default 0,
  is_active                 tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_choice_choice_type check (choice_type in (0,1,2,3)),
  constraint pk_choice primary key (id))
;

create table content (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content primary key (id))
;

create table content_file (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  file_type                 varchar(255),
  uploader_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content_file primary key (id))
;

create table content_text (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      longtext,
  uploader_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content_text primary key (id))
;

create table course (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  description               varchar(255),
  start_date                datetime(6),
  end_date                  datetime(6),
  is_archived               tinyint(1) default 0,
  has_open_enrollment       tinyint(1) default 0,
  is_shared                 tinyint(1) default 0,
  institution_id            bigint,
  instructor_id             bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_course primary key (id))
;

create table exercise (
  id                        bigint auto_increment not null,
  course_id                 bigint not null,
  retired                   tinyint(1) default 0,
  creator_id                bigint,
  name                      varchar(255),
  description               varchar(255),
  order_index               integer,
  release_date              datetime(6),
  has_spaced_repetition     tinyint(1) default 0,
  is_hidden                 tinyint(1) default 0,
  exercise_type             integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_exercise_exercise_type check (exercise_type in (0,1,2)),
  constraint pk_exercise primary key (id))
;

create table exercise_record (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  time_to_complete          double,
  attempt_number            integer,
  is_cleared                tinyint(1) default 0,
  number_correct            integer,
  number_answered           integer,
  submitter_id              bigint,
  exercise_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_exercise_record primary key (id))
;

create table flagged_question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  exercise_id               bigint,
  question_id               bigint,
  status                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_flagged_question_status check (status in (0,1,2)),
  constraint pk_flagged_question primary key (id))
;

create table institution (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  location                  varchar(255),
  description               varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_institution primary key (id))
;

create table passage (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  title                     varchar(255),
  text                      TEXT,
  source                    varchar(255),
  length                    bigint,
  level                     bigint,
  text_type                 varchar(255),
  uploader_id               bigint,
  is_global                 tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_passage primary key (id))
;

create table project (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_project primary key (id))
;

create table prompt (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_prompt primary key (id))
;

create table question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  order_index               integer,
  question_type_id          bigint,
  prompt_id                 bigint,
  content_id                bigint,
  submitter_id              bigint,
  is_global                 tinyint(1) default 0,
  project_id                bigint,
  was_generated             tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_question primary key (id))
;

create table question_part (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  prompt_id                 bigint,
  content_id                bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_question_part primary key (id))
;

create table question_record (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  time_to_complete          double,
  attempt_number            integer,
  is_cleared                tinyint(1) default 0,
  exercise_record_id        bigint,
  submitter_id              bigint,
  question_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint uq_question_record_exercise_record_id unique (exercise_record_id),
  constraint pk_question_record primary key (id))
;

create table question_type (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_question_type primary key (id))
;

create table response (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  submitter_id              bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_response primary key (id))
;

create table sentence (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  passage_id                bigint,
  order_index               integer,
  text                      TEXT,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_sentence primary key (id))
;

create table student_in_institution (
  id                        bigint auto_increment not null,
  student_id                bigint,
  institution_id            bigint,
  official                  tinyint(1) default 0,
  created_time              datetime(6) not null,
  constraint pk_student_in_institution primary key (id))
;

create table student_question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  question_id               bigint,
  course_id                 bigint,
  status                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_student_question_status check (status in (0,1,2)),
  constraint pk_student_question primary key (id))
;

create table tag (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_tag primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  first_name                varchar(255),
  last_name                 varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  role                      integer,
  creator_id                bigint,
  institution_id            bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_user_role check (role in (0,1,2,3)),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;


create table announcement_content (
  announcement_id                bigint not null,
  content_id                     bigint not null,
  constraint pk_announcement_content primary key (announcement_id, content_id))
;

create table course_coinstructor (
  course_id                      bigint not null,
  user_id                        bigint not null,
  constraint pk_course_coinstructor primary key (course_id, user_id))
;

create table course_announcement (
  course_id                      bigint not null,
  announcement_id                bigint not null,
  constraint pk_course_announcement primary key (course_id, announcement_id))
;

create table course_student (
  course_id                      bigint not null,
  user_id                        bigint not null,
  constraint pk_course_student primary key (course_id, user_id))
;

create table exercise_question (
  exercise_id                    bigint not null,
  question_id                    bigint not null,
  constraint pk_exercise_question primary key (exercise_id, question_id))
;

create table passage_tag (
  passage_id                     bigint not null,
  tag_id                         bigint not null,
  constraint pk_passage_tag primary key (passage_id, tag_id))
;

create table question_question_part (
  question_id                    bigint not null,
  question_part_id               bigint not null,
  constraint pk_question_question_part primary key (question_id, question_part_id))
;
alter table announcement add constraint fk_announcement_creator_1 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_announcement_creator_1 on announcement (creator_id);
alter table announcement add constraint fk_announcement_prompt_2 foreign key (prompt_id) references prompt (id) on delete restrict on update restrict;
create index ix_announcement_prompt_2 on announcement (prompt_id);
alter table blank add constraint fk_blank_passage_3 foreign key (passage_id) references passage (id) on delete restrict on update restrict;
create index ix_blank_passage_3 on blank (passage_id);
alter table choice add constraint fk_choice_question_part_4 foreign key (question_part_id) references question_part (id) on delete restrict on update restrict;
create index ix_choice_question_part_4 on choice (question_part_id);
alter table course add constraint fk_course_institution_5 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_course_institution_5 on course (institution_id);
alter table course add constraint fk_course_instructor_6 foreign key (instructor_id) references user (id) on delete restrict on update restrict;
create index ix_course_instructor_6 on course (instructor_id);
alter table exercise add constraint fk_exercise_course_7 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_exercise_course_7 on exercise (course_id);
alter table exercise add constraint fk_exercise_creator_8 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_exercise_creator_8 on exercise (creator_id);
alter table passage add constraint fk_passage_uploader_9 foreign key (uploader_id) references user (id) on delete restrict on update restrict;
create index ix_passage_uploader_9 on passage (uploader_id);
alter table question add constraint fk_question_questionType_10 foreign key (question_type_id) references question_type (id) on delete restrict on update restrict;
create index ix_question_questionType_10 on question (question_type_id);
alter table question add constraint fk_question_prompt_11 foreign key (prompt_id) references prompt (id) on delete restrict on update restrict;
create index ix_question_prompt_11 on question (prompt_id);
alter table question add constraint fk_question_content_12 foreign key (content_id) references content (id) on delete restrict on update restrict;
create index ix_question_content_12 on question (content_id);
alter table question add constraint fk_question_submitter_13 foreign key (submitter_id) references user (id) on delete restrict on update restrict;
create index ix_question_submitter_13 on question (submitter_id);
alter table question add constraint fk_question_project_14 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_question_project_14 on question (project_id);
alter table question_part add constraint fk_question_part_prompt_15 foreign key (prompt_id) references prompt (id) on delete restrict on update restrict;
create index ix_question_part_prompt_15 on question_part (prompt_id);
alter table question_part add constraint fk_question_part_content_16 foreign key (content_id) references content (id) on delete restrict on update restrict;
create index ix_question_part_content_16 on question_part (content_id);
alter table question_record add constraint fk_question_record_exerciseRecord_17 foreign key (exercise_record_id) references exercise_record (id) on delete restrict on update restrict;
create index ix_question_record_exerciseRecord_17 on question_record (exercise_record_id);
alter table sentence add constraint fk_sentence_passage_18 foreign key (passage_id) references passage (id) on delete restrict on update restrict;
create index ix_sentence_passage_18 on sentence (passage_id);
alter table user add constraint fk_user_institution_19 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_user_institution_19 on user (institution_id);



alter table announcement_content add constraint fk_announcement_content_announcement_01 foreign key (announcement_id) references announcement (id) on delete restrict on update restrict;

alter table announcement_content add constraint fk_announcement_content_content_02 foreign key (content_id) references content (id) on delete restrict on update restrict;

alter table course_coinstructor add constraint fk_course_coinstructor_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_coinstructor add constraint fk_course_coinstructor_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table course_announcement add constraint fk_course_announcement_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_announcement add constraint fk_course_announcement_announcement_02 foreign key (announcement_id) references announcement (id) on delete restrict on update restrict;

alter table course_student add constraint fk_course_student_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_student add constraint fk_course_student_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table exercise_question add constraint fk_exercise_question_exercise_01 foreign key (exercise_id) references exercise (id) on delete restrict on update restrict;

alter table exercise_question add constraint fk_exercise_question_question_02 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table passage_tag add constraint fk_passage_tag_passage_01 foreign key (passage_id) references passage (id) on delete restrict on update restrict;

alter table passage_tag add constraint fk_passage_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

alter table question_question_part add constraint fk_question_question_part_question_01 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_question_part add constraint fk_question_question_part_question_part_02 foreign key (question_part_id) references question_part (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table announcement;

drop table announcement_content;

drop table answer_decimal;

drop table answer_integer;

drop table answer_text;

drop table answer_word;

drop table blank;

drop table choice;

drop table content;

drop table content_file;

drop table content_text;

drop table course;

drop table course_coinstructor;

drop table course_announcement;

drop table course_student;

drop table exercise;

drop table exercise_question;

drop table exercise_record;

drop table flagged_question;

drop table institution;

drop table passage;

drop table passage_tag;

drop table project;

drop table prompt;

drop table question;

drop table question_question_part;

drop table question_part;

drop table question_record;

drop table question_type;

drop table response;

drop table sentence;

drop table student_in_institution;

drop table student_question;

drop table tag;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

