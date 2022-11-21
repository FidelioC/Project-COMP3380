*
use cs3380;
#
*
drop table if exists generate;
#
*
drop table if exists gameData;
#
*
drop table if exists leaderboard;
#
*
drop table if exists compete;
#
*
drop table if exists season;
#
*
drop table if exists signed;
#
*
drop table if exists team;
#
*
drop table if exists city;
#
*
drop table if exists conference;
#
*
drop table if exists player;
#
*
create table city(
   cityID integer primary key IDENTITY (1,1),
   cityName varchar(15) not null
);
#
*
create table conference(
   conferenceID integer primary key IDENTITY(1,1),
   conference varchar(5) not null
);
#
*
create table team(
   teamID integer primary key,
   abbreviation varchar(3) not null,
   teamName varchar(20) not null,
   nickname varchar(15) not null,
   conferenceID INTEGER,
   arenaName varchar(30) not null,
   arenaCapacity integer,
   dleagueAffiliation varchar(35) not null,
   tCoach varchar(17) not null,
   tManager varchar(17) not null,
   tOwner varchar(35) not null,
   cityID INTEGER,
   yearFounded integer not null,
   FOREIGN key ("cityID") REFERENCES city ("cityID"),
   FOREIGN key ("conferenceID") REFERENCES "conference" ("conferenceID")
);
#
*
create table gameData(
   gameDate varchar(10) not null,
   gameID integer primary key,
   
   homeTeamID integer,
   awayTeamID integer,
   
   ptsHome integer not null,
   fgPctHome float not null,
   ftPctHome float not null,
   fg3PctHome float not null,
   astHome integer not null,
   rebHome integer not null,
   
   ptsAway integer not null,
   fgPctAway float not null,
   ftPctAway float not null,
   fg3PctAway float not null,
   astAway integer not null,
   rebAway integer not null,

   homeTeamWins integer not null,
);
#
*
create table generate(
   "gameID" INTEGER,
   "teamID" INTEGER,
   PRIMARY KEY ("gameID","teamID"),
   FOREIGN KEY ("gameID") REFERENCES "gameData"("gameID"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);
#
*
create table leaderboard(
   "teamID" INTEGER,
   seasonID integer not null,
   standingsDate varchar(10) not null,
   gamesPlayed integer not null,
   gamesWon integer not null,
   gamesLost integer not null,
   winPercent float not null,
   PRIMARY key ("teamID", "standingsDate"),
   FOREIGN key ("teamID") REFERENCES "team"("teamID")
);
#
*
create table player(
   playerID integer primary key,
   playerName varchar(50) not null
);
#
*
create table season(
   "playerID" INTEGER,
   "season_year" integer not null,
   PRIMARY KEY ("playerID", "season_year"),
   FOREIGN key ("playerID") REFERENCES "player"("playerID"),
);
#
*
create table compete(
   "teamID" INTEGER,
   "playerID" INTEGER,
   "season_year" INTEGER,
   FOREIGN KEY ("playerID","season_year") REFERENCES "season"("playerID","season_year"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);
#
*
create table signed(
   "playerID" INTEGER,
   "teamID" INTEGER,
   PRIMARY KEY ("playerID", "teamID"),
   FOREIGN KEY ("playerID") REFERENCES "player"("playerID"),
   FOREIGN KEY ("teamID") REFERENCES "team"("teamID")
);
#








