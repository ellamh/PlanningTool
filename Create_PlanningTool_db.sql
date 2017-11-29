CREATE TABLE IF NOT EXISTS courses (
 courseName varchar(20) NOT NULL,
 courseId varchar(6) NOT NULL,
 semester varchar(15) NOT NULL,
 courseStatus varchar(15) NOT NULL,
 year int(4) NOT NULL,
  PRIMARY KEY (courseName)
);

INSERT INTO courses (courseName, courseId, semester, courseStatus, year) VALUES
(‘JavaOO’, ‘209847’, ‘Autumn’, ‘Scheduled’, 2018);