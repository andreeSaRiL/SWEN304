SELECT DISTINCT BankName, City
FROM Bank NATURAL JOIN HasAccounts NATURAL JOIN Robbers WHERE Nickname=’Al Capone’;
