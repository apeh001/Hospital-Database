drop table Hospital cascade;
drop table Department cascade;
drop table Doctor cascade;
drop table Patient cascade;
drop table Staff cascade;
drop table Appointment cascade;
drop table past cascade;
drop table active cascade;
drop table available cascade;
drop table waitlisted cascade;
drop table schedules cascade;
drop table request_maintenance cascade;
drop table searches cascade;
drop table has cascade;


CREATE TABLE Hospital (hospital_ID VARCHAR(11) not null,
                               name TEXT,
                               PRIMARY KEY (hospital_ID));


CREATE TABLE Department (dept_ID VARCHAR(11) not null,
                                 hospital_ID VARCHAR(11) not null,
                                 name TEXT,
                                 PRIMARY KEY (dept_ID),
                                 FOREIGN KEY (hospital_ID) REFERENCES Hospital);


CREATE TABLE Doctor (doctor_ID VARCHAR(11) not null,
                            name TEXT,
                            specialty TEXT,
                            dept_ID VARCHAR(11) not null,
                            PRIMARY KEY (doctor_ID),
    FOREIGN KEY (dept_ID) REFERENCES Department);


CREATE TABLE Patient (pID VARCHAR(11),
                             name TEXT,
                             gender TEXT,
                             age INT,
                             address TEXT,
                             num_appts INT,
                             PRIMARY KEY (pID));


CREATE TABLE Staff (staff_ID VARCHAR(11) not null,
                         name TEXT,
                         hospital_ID VARCHAR(11) not null,
                         PRIMARY KEY (staff_ID),
 FOREIGN KEY (hospital_ID) REFERENCES Hospital);




CREATE TABLE Appointment (appnt_ID VARCHAR(11),
                                   time_slot TEXT,
                                   date TEXT,
   PRIMARY KEY (appnt_ID));


CREATE TABLE past (appnt_ID VARCHAR(11),
                         PRIMARY KEY (appnt_ID),
                         FOREIGN KEY (appnt_ID) REFERENCES Appointment);


CREATE TABLE active (appnt_ID VARCHAR(11),
                           PRIMARY KEY (appnt_ID),
                           FOREIGN KEY (appnt_ID) REFERENCES Appointment);


CREATE TABLE available (appnt_ID VARCHAR(11),
                                PRIMARY KEY (appnt_ID),
                                FOREIGN KEY (appnt_ID) REFERENCES Appointment);


CREATE TABLE waitlisted (appnt_ID VARCHAR(11),
                                PRIMARY KEY (appnt_ID),
                                FOREIGN KEY (appnt_ID) REFERENCES Appointment);


CREATE TABLE schedules (appnt_ID VARCHAR(11),
                                   staff_ID VARCHAR(11),
                                PRIMARY KEY (appnt_ID, staff_ID),
                                FOREIGN KEY (appnt_ID) REFERENCES Appointment,
                                FOREIGN KEY (staff_ID) REFERENCES Staff);


CREATE TABLE request_maintenance (doctor_ID VARCHAR(11),
                                             staff_ID VARCHAR(11),
                                             patient_per_hour TEXT,
                                             dept_name TEXT,
                                             time_slot TEXT,
                                             PRIMARY KEY (doctor_ID, staff_ID),
                                             FOREIGN KEY (doctor_ID) REFERENCES Doctor,
                                             FOREIGN KEY (staff_ID) REFERENCES Staff);


CREATE TABLE searches (hospital_ID VARCHAR(11),
                                patient_ID VARCHAR(11),
                                appnt_ID VARCHAR(11),
                                PRIMARY KEY (hospital_ID, patient_ID, appnt_ID),
                                FOREIGN KEY (hospital_ID) REFERENCES Hospital,
                                FOREIGN KEY (patient_ID) REFERENCES Patient,
                                FOREIGN KEY (appnt_ID) REFERENCES Appointment);


CREATE TABLE has (appnt_ID VARCHAR(11),
                           doctor_ID VARCHAR(11),
                           PRIMARY KEY (appnt_ID, doctor_ID),
                           FOREIGN KEY (appnt_ID) REFERENCES Appointment,
                           FOREIGN KEY (doctor_ID) REFERENCES Doctor);