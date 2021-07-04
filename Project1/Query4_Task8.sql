SELECT RobberId, Nickname, Description
FROM Robbers NATURAL JOIN HasSkills NATURAL JOIN Skills 
ORDER BY Description;
