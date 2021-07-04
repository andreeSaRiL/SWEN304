--STEP WISE APPROACH:
CREATE VIEW securityAmt as (
SELECT b.BankName, b.City, ,b.Security, r.Amount
FROM Bank b NATURAL JOIN Robberies r
ORDER BY b.Security);

CREATE VIEW robberAmt as (
SELECT Security, COUNT(Security) AS NumberRobberies, ROUND(AVG(Amount),2 ) AS AverageAmount
FROM securityAmt 
GROUP BY Security
ORDER BY NumberRobberies DESC);

select * from robberAmt;

--SINGLE NESTED QUERY:
SELECT Security AS SecurityLevel, COUNT(Security) AS NumberRobberies, ROUND(AVG(Amount), 2) AS AverageAmount
FROM (SELECT b.BankName, b.City, b.Security, r.Amount
	FROM Robberies r NATURAL JOIN Bank b) AS sec
GROUP BY Security
ORDER BY NumberRobberies DESC;


------------------------ 2019 past exam ------------------------
--- question b
--Write an SQL query to retrieve the IDs and names of all instruments that
--are played with high expertise, ordered by the name of the instrument. 
SELECT i.InsID, i.InsName
FROM Instrument i 
JOIN (
	SELECT MAX(ExpertiseLevel) AS highExpertise
	FROM Plays
	) highExpertise ON i.IRDNo = highExpertise.IRDNo
ORDER BY i.InsName;



--- question c
--Write an SQL query to retrieve the IRDNo of musicians who play the
--largest number of instruments.
CREATE VIEW instr as (
SELECT MAX(i.InsName) as maxIns, m.Name
FROM Instrument i NATURAL JOIN Musician m
GROUP BY maxIns
ORDER BY maxIns DESC
);

CREATE VIEW final as (
SELECT IRDNo
FROM instr
);

select * from final;