select * from city;

SELECT * from conference;

SELECT * from team;

SELECT * from gameData;

SELECT * from player;

SELECT * from season;

select * from generate;

select * from compete;

SELECT * from signed;

SELECT * from leaderboard;

/*
SELECT * from player
join season on season.playerID = player.playerID
join compete on player.playerID = compete.playerID 
      and season.season_year = compete.season_year
join team on compete.teamID = team.teamID;

select team.nickname, gameData.* from team
join generate on generate.teamID = team.teamID
join gameData on gameData.gameID = generate.gameID
where team.teamID = 1610612742;*/
/*
SELECT * from player 
join play on player.playerID = play.playerID
join season on play.seasonID = season.seasonID
join compete on player.playerID 
;

SELECT * from player 
join signed on signed.playerID = player.playerID*/

/*SELECT * from play;*/


/*
  standings
  leaderboard
*/