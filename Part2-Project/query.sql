/* City that has more than one team */
SELECT cityName
from team
join city on city.cityID = team.cityID
group by cityName
having count(cityName) > 1


/*Show the game ID, and home team where home team get 135+ points*/
SELECT gameID, nickname
from gameData
left join team on team.teamID = gameData.homeTeamID
where ptsHome > 135; 

/*the highest score each team has made at home*/
with allTeam as(
  SELECT team.teamID, teamName, gameData.ptsHome, gameData.gameID
  from team
  join generate on team.teamID = generate.teamID
  join gameData on generate.gameID = gameData.gameID
  where team.teamID = gameData.homeTeamID
)
select teamID, teamName, max(ptsHome) as HighestPoint
from allTeam
group by teamID, teamName
order by teamID desc;

/*players stay in one team*/
select player.playerID, playerName, count(team.teamID)
from player
join signed on player.playerID = signed.playerID
join team on team.teamID = signed.teamID
group by player.playerID, playerName
having count(team.teamID) = 1

/* A player play on which seasons and play on what team for each of that season. 
Search by the player name*/
SELECT player.playerName, teamName, compete.season_year from player
join season on season.playerID = player.playerID
join compete on player.playerID = compete.playerID 
      and season.season_year = compete.season_year
join team on team.teamID = compete.teamID
where playerName = 'Kobe Bryant';


/*teams on each conference*/
SELECT teamName, conference
from team
join conference on conference.conferenceID = team.conferenceID
ORDER by conference

/*The roster of a team on a specific season*/
SELECT teamName, player.playerName, season_year
from compete
join team on team.teamID = compete.teamID
join player on compete.playerID = player.playerID
where season_year = 2019 and teamName = 'Portland'
order by teamName


/*all team home won from 2004-2020*/
select teamName,sum(homeTeamWins) as totalWins
from gameData
join team on team.teamID = gameData.homeTeamID
group by teamName
order by totalWins DESC





