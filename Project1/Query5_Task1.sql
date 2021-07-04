SELECT r.BankName, r.City
FROM Robberies r NATURAL JOIN Plans p 
GROUP BY r.BankName, r.City, r.Date, p.PlannedDate
HAVING (DATE_PART(‘year’, p.PlannedDate) – DATE_PART(‘year’, r.Date) = 0);
