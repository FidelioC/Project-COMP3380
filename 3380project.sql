drop table if exists season;
drop table if exists play;
drop table if exists players;
drop table if exists sign;
drop table if exists team;
drop table if exists conference;
drop table if exists standing;
drop table if exists leaderboard;
drop table if exists generate;
drop table if exists gameData;
drop table if exists homedata;
drop table if exists awaydata;
drop table if exists manager;
drop table if exists owner;
drop table if exists coach;
drop table if exists employee;
drop table if exists city;
drop table if exists complete;


create table season(
   seasonID integer primary key,
   season_year integer not null
);
create table player(
   playerID integer primary key,
   playerName text not null
);
create table play(
   playerID integer REFERENCES player(playerID)
);
create table sign(
   playerID integer REFERENCES player(playerID),
   teamID integer REFERENCES team(teamID)
);
create table team(
   teamID integer primary key,
   abbreviation text not null,
   nickname text not null,
   yearFounded integer not null,
   arena text not null,
   arenaCapacity integer,
   dleagueAffiliation text not null
);
create table conference(
   conferenceID integer primary key,
   conference text not null
);

create table manager(
   managerID integer primary key,
   generalManager text not null
);

create table owner(
   ownerID integer primary key,
   owner text not null
);

create table coach(
coachID integer primary key,
headCoach text not null
);

create table employee(
   /*union:employee -> owner, manager, coach*/
   employeeID integer primary key,
   name text not null
);

create table city(
   cityID integer primary key,
   cityName text not null
);

create table complete(
   seasonID integer REFERENCES season(seasonID),
   teamID integer REFERENCES team(teamID)
);

create table gameData(
   gameID integer primary key,
   gamedate text not null
);

create table homeData(
   gameDate text REFERENCES gameData(gameDate),
   homeTeamID integer primary key,
   ptsHome integer not null,
   fgPtsHome float not null,
   fgPctHome float not null,
   ftPctHome float not null,
   fg3PctHome float not null,
   astHome integer not null,
   rebHome integer not null,
   homeTeamWins integer not null
);

create table awayData(
   gameDate text REFERENCES gameData(gameDate),
   awayTeamID integer primary key,
   ptsAway integer not null,
   fgPtsAway float not null,
   fgPctAway float not null,
   ftPctAway float not null,
   fg3PctAway float not null,
   astAway integer not null,
   rebAway integer not null,
   awayTeamWins integer not null
);

create table generate(
   gameID integer REFERENCES gameData(gameID),
   teamID integer REFERENCES team(teamID)
);

create table leaderboard(
   standingDate integer primary key,
   gamePlayed integer not null,
   gameWon integer not null,
   gameLost integer not null,
   winPercent integer not null,
   homeRecord integer not null,
   awayRecord integer not null
);

create table standing(
   teamID integer REFERENCES team(teamID),
   standingDate integer REFERENCES leaderboard(standingDate)
);

