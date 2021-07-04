SELECT r.BankName, r.City
FROM Bank b NATURAL JOIN Robberies r
WHERE b.NoAccounts > 1;
