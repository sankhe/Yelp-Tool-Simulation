create table yelp_user (
year_since VARCHAR(100),
funny_votes integer,
useful_votes integer,
cool_votes integer,
review_count integer,
name VARCHAR(100),
user_id VARCHAR(100),
fans integer,
avg_stars float,
constraint pk_user PRIMARY KEY(user_id));

create table yelp_business (
business_id VARCHAR(100),
open integer,
city VARCHAR(50),
state VARCHAR(50),
review_count integer,
name VARCHAR(100),
stars float,
zip VARCHAR(100),
constraint pk_business PRIMARY KEY(business_id));

create table category (
business_id VARCHAR(100),
cat_name VARCHAR(100),
constraint pk_category PRIMARY KEY(business_id, cat_name),
constraint fk_bid FOREIGN KEY(business_id) references yelp_business(business_id)
on delete cascade);

create table sub_category (
business_id VARCHAR(100),
main_cat_name VARCHAR(100),
sub_cat_name VARCHAR(100),
constraint pk_sub_category PRIMARY KEY(business_id, main_cat_name, sub_cat_name),
constraint fk_business_id FOREIGN KEY(business_id) references yelp_business(business_id)
on delete cascade);


create table hours (
business_id VARCHAR(100),
day VARCHAR(50),
open_time float,
close_time float,
constraint pk_hours PRIMARY KEY(business_id, day),
constraint fk_bus_id FOREIGN KEY(business_id) references yelp_business(business_id)
on delete cascade);

create table attributes (
business_id VARCHAR(100),
attr_name VARCHAR(100),
attr_value VARCHAR(100),
attr_name_val VARCHAR(100),
constraint pk_attr PRIMARY KEY(business_id, attr_name_val),
constraint fk_buss_id FOREIGN KEY(business_id) references yelp_business(business_id)
on delete cascade);

create table yelp_review (
funny integer,
useful integer,
cool integer,
review_id VARCHAR(100),
user_id VARCHAR(100),
stars integer,
review_date date,
review_text clob,
business_id VARCHAR(100),
constraint pk_review PRIMARY KEY(review_id)
);


