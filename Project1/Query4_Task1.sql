SELECT DISTINCT r.RobberId, r.Nickname, r.Age, s.description
FROM Robbers r NATURAL JOIN Skills s WHERE r.age >=20 and r.age <= 40;
