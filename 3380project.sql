use cs3380;

drop table if exists team;
drop table if exists employee;
drop table if exists city;
drop table if exists conference;
drop table if exists generate;
drop table if exists gameData;
drop table if exists standing;
drop table if exists leaderboard;
drop table if exists compete;
drop table if exists season;
drop table if exists signed;
drop table if exists players;
drop table if exists play;

create table city(
   cityID integer primary key IDENTITY (1,1),
   cityName text not null
);

create table conference(
   conferenceID integer primary key IDENTITY(1,1),
   conference text not null
);

create table team(
   cityID INTEGER,
   conferenceID INTEGER,
   teamID integer primary key,
   abbreviation text not null,
   nickname text not null,
   yearFounded integer not null,
   arenaName text not null,
   arenaCapacity integer,
   dleagueAffiliation text not null,
   FOREIGN key ("cityID") REFERENCES city ("cityID"),
   FOREIGN key ("conferenceID") REFERENCES "conference" ("conferenceID")
);

create table employee(
   teamID INTEGER,
   coach text not null,
   manager text not null,
   owner text not null
   FOREIGN key ("teamID") REFERENCES "team" ("teamID")
);

create table gameData(
   gameID integer primary key,
   gameDate text not null,
   
   homeTeamID integer,
   ptsHome integer not null,
   fgPtsHome float not null,
   fgPctHome float not null,
   ftPctHome float not null,
   fg3PctHome float not null,
   astHome integer not null,
   rebHome integer not null,
   homeTeamWins integer not null,

   awayTeamID integer,
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
   "gameID" INTEGER,
   "teamID" INTEGER,
   PRIMARY KEY ("gameID","teamID"),
   FOREIGN KEY ("gameID") REFERENCES "gameData"("gameID"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);


create table leaderboard(
   standingsDate integer primary key,
   gamesPlayed integer not null,
   gamesWon integer not null,
   gamesLost integer not null,
   winPercent integer not null,
   homeRecord integer not null,
   awayRecord integer not null
);

create table standings(
   "teamID" INTEGER,
   "standingsDate" INTEGER,
   PRIMARY key ("teamID", "standingsDate"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID"),
   FOREIGN KEY ("standingsDate") REFERENCES "leaderboard"("standingsDate")
);

create table season(
   seasonID integer primary key,
   season_year integer not null
);


create table compete(
   "seasonID" INTEGER,
   "teamID" INTEGER,
   PRIMARY KEY ("seasonID", "teamID"),
   FOREIGN KEY ("seasonID") REFERENCES "season"("seasonID"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);

create table player(
   playerID integer primary key,
   playerName text not null
);

create table signed(
   "playerID" INTEGER,
   "teamID" INTEGER,
   PRIMARY KEY ("playerID", "teamID"),
   FOREIGN KEY ("playerID") REFERENCES "player"("playerID"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);

create table play(
   "playerID" INTEGER,
   "seasonID" INTEGER,
   PRIMARY KEY ("playerID", "seasonID"),
   FOREIGN KEY ("playerID") REFERENCES "player"("playerID"),
   FOREIGN key ("seasonID") REFERENCES "season"("seasonID")
);







