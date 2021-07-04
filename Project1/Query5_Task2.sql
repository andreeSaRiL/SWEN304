SELECT r.RobberId, r.Nickname
FROM Bank b NATURAL JOIN Robbers r NATURAL JOIN HasAccounts h
WHERE b.NoAccounts = 0
GROUP BY r.RobberId, r.Nickname;
