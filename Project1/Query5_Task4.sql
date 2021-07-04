SELECT a.BankName, a.City, a.Date
FROM Accomplices a
JOIN ( SELECT City, MAX(Share) AS HighestShare
	FROM Accomplices
	GROUP BY City)
HighestShare ON (HighestShare.City = a.City);

--Below is another query which ensures that the cities shown above, really contain the highest shares. 
SELECT City, MAX(Share) AS HighestShare
FROM Accomplices 
GROUP BY City; 
