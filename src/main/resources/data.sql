insert into cwcase (casestatus) values ('active');
insert into child (first,last,ssn) values ('bobby', 'johnson', '123-12-1234');

INSERT INTO HEARING(ID, COUNTY_CODE, HEARING_TYPE, COURT_DEPT, DATE, STATUS, caseworker, CWCASEID, DOC, IMAGE, SUMMARY, ATTENDEE_FIRST, ATTENDEE_LAST, TIME, LANGUAGE) VALUES 
(1, 10, 'juvenile', 'juvenile', NULL, 'Created', 'Clark', NULL, NULL, NULL, 'first entry', 'james', 'johnson', NULL, 'Spanish');

INSERT INTO HEARING(ID, COUNTY_CODE, HEARING_TYPE, COURT_DEPT, DATE, STATUS, caseworker, CWCASEID, DOC, IMAGE, SUMMARY, ATTENDEE_FIRST, ATTENDEE_LAST, TIME, LANGUAGE) VALUES 
(2, 20, 'juvenile', 'juvenile', NULL, 'Created', 'Woppner', NULL, NULL, NULL, 'new entry for county 20', 'bill', 'arner', NULL, 'English');