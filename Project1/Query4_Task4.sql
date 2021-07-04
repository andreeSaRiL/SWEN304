SELECT BankName, City, NoAccounts 
FROM Bank
WHERE BankName NOT IN (SELECT BankName FROM Bank WHERE City = ‘Deerfield’)
ORDER BY NoAccounts ASC;
