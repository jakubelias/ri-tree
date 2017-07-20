CREATE TABLE Intervals (node int, lower int, upper int, varchar(100) int);
CREATE INDEX lowerIndex ON Intervals (node, lower);
CREATE INDEX upperIndex ON Intervals (node, upper);