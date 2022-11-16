select * from city;

SELECT * from conference;

SELECT * from team;

SELECT * from gameData;

SELECT * from player;

SELECT * from season;

select * from generate;

select * from compete;

SELECT * from play;

SELECT * from signed;

SELECT * from player
join season on season.playerID = player.playerID
join compete on player.playerID = compete.playerID 
      and season.season_year = compete.season_year;

/*
SELECT * from player 
join play on player.playerID = play.playerID
join season on play.seasonID = season.seasonID
join compete on player.playerID 
;

SELECT * from player 
join signed on signed.playerID = player.playerID*/




/*
  standings
  leaderboard
*/