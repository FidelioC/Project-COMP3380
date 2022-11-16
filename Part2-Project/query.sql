/*search the team name from the city*/
Select nickname from team
join city on city.cityID = team.cityID
where cityName like 'Los Angeles' /*city name is the input*/

/*Show the game ID, and home team where home team get 135+ points*/
SELECT gameID, nickname
from gameData
left join team on team.teamID = gameData.homeTeamID
where ptsHome > 135; 

/*the highest score each team has made at home*/
SELECT distinct teamID
from team;

