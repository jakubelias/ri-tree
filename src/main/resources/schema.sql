/*
create database ritree;
*/

CREATE TABLE Intervals (node int, lower int, upper int, id int);
CREATE INDEX lowerIndex ON Intervals (node, lower);
CREATE INDEX upperIndex ON Intervals (node, upper);