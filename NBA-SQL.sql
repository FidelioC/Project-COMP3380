
use cs3380;

drop table if exists city;

create table city(
  cityID integer primary key IDENTITY (1,1),
  cityName text not null
);