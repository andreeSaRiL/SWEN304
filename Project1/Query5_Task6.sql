--STEP WISE APPROACH:
CREATE VIEW RobberId as (
SELECT DISTINCT a.RobberId, b.Security 
FROM Accomplices a NATURAL JOIN Bank b 
ORDER BY b.Security);

CREATE VIEW SkillsId as (
SELECT h.RobberId, r.Security
FROM HasSkills h NATURAL JOIN RobberId r);

CREATE VIEW Descriptions as (
SELECT s.Security, skill.Description
FROM SkillsId s NATURAL JOIN Skills skill);

CREATE VIEW Nickname as (
SELECT des.Security, des.Description, r.Nickname
FROM Robbers r NATURAL JOIN Descriptions des
GROUP BY des.Security, des.Description, r.Nickname
ORDER BY des.Security ASC);

select * from Nickname;

--SINGLE NESTED QUERY:
SELECT secL.Security, secL.Description, r.Nickname
FROM Robbers r NATURAL JOIN (
 SELECT sec.Security, sec.RobberId, s.Description
              FROM (SELECT h.Robberid, h.SkillId, lev.Security
              FROM HasSkills h JOIN (
  			SELECT DISTINCT a.RobberId, b.security 
FROM Bank b NATURAL JOIN Accomplices a) lev ON                       lev.RobberId = h.RobberId) sec 
NATURAL JOIN Skills s) secL
ORDER BY secL.Security ASC;
