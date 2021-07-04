SELECT RobberId, Nickname, earnings
FROM (SELECT RobberId, SUM(Share) AS earnings FROM Accomplices GROUP BY RobberId) AS totalEarnings NATURAL JOIN Robbers
WHERE Earnings > 30000  ORDER BY Earnings DESC;
