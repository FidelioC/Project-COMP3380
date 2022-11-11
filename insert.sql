BULK INSERT city
FROM '/SplittedData/City.csv'
WITH(
  FIRSTROW = 2,
  FIELDTERMINATOR = ',',
  ROWTERMINATOR = '\n',
  BATCHSIZE = 25000
);