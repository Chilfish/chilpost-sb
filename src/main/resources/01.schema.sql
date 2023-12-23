Create Table If Not Exists users (
  id         Int Auto_Increment
    Primary Key,
  uuid       Binary(16)                                        Not Null,
  name       Varchar(255)                                      Not Null,
  nickname   Varchar(255)                                      Not Null,
  password   Varchar(255)                                      Not Null,
  email      Varchar(255)                                      Not Null,
  bio        Varchar(255) Default 'hello'                      Not Null,
  avatar     Varchar(255) Default '/placeholder.avatar.png'    Not Null,
  level      Varchar(255) Default 'user'                       Not Null,
  deleted    Tinyint(1)   Default 0                            Not Null,
  deleted_at Datetime(6)                                       Null,
  created_at Datetime(6)  Default '2023-12-14 10:49:14.144267' Not Null,
  updated_at Datetime(6)  Default '2023-12-14 10:49:14.144267' Not Null,
  Constraint users_email_unique
    Unique (email),
  Constraint users_name_unique
    Unique (name)
);

Create Table If Not Exists user_status (
  id              Int Auto_Increment
    Primary Key,
  user_id         Int                         Not Null,
  post_count      Int  Default 0              Not Null,
  follower_count  Int  Default 0              Not Null,
  following_count Int  Default 0              Not Null,
  followers       Json Default (_utf8mb4'[]') Not Null,
  followings      Json Default (_utf8mb4'[]') Not Null,
  Constraint fk_user_status_user_id__id
    Foreign Key (user_id) References users (id)
);

Create Table If Not Exists posts (
  id         Int Auto_Increment
    Primary Key,
  uuid       Binary(16)                                       Not Null,
  owner_id   Binary(16)                                       Not Null,
  content    Text                                             Not Null,
  deleted    Tinyint(1)  Default 0                            Not Null,
  deleted_at Datetime(6)                                      Null,
  created_at Datetime(6) Default '2023-12-14 10:49:14.059992' Not Null,
  is_body    Tinyint(1)  Default 1                            Not Null,
  parent_id  Binary(16)                                       Null,
  child_id   Binary(16)                                       Null,
  media      Json        Default (_utf8mb4'[]')               Not Null
);

Create Table If Not Exists post_status (
  id            Int Auto_Increment
    Primary Key,
  post_id       Int                         Not Null,
  like_count    Int  Default 0              Not Null,
  comment_count Int  Default 0              Not Null,
  repost_count  Int  Default 0              Not Null,
  likes         Json Default (_utf8mb4'[]') Not Null,
  Constraint fk_post_status_post_id__id
    Foreign Key (post_id) References posts (id)
);
