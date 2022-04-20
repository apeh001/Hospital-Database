# Hospital Database

> Authors: Kristie Hyun github.com/khyun004, Allan Peh github.com/apeh001

## Project Description

This project is is a model of a hospital database system. For this project, we need to build a functioning model that can track information about different hospitals, the maintenance of certain departments within each hospital, the doctors and the staff, as well as the appointments attended by the doctors and patients. 

## Step 1

Our first step was to create an ER model based on the parameters that were given to us during the start of the project. After creating the model, we were tasked with describing the model. We must first identify the entity sets, the attributes that each set contained, and the relationships between entities.

## Step 2

In our next step, we used the ER diagram in the first step and translated it into a relational database schema. This schema was to be written in the form of a single executable SQL script.

## Step 3

In our third and final step, we needed to develop a client application and use the application to support specific functionalitiy and queries. In this step, the professor had provided us a schema to which we could test to see if our code would run correctly and return the correct output. We were also provided a skeleton code for the client application. We were tasked with altering the code to ensure that our application matched our schea from step 2.

## Requirement Analysis

These were the requirements that our application needed to pass in order for it to be considered a success:

### Hospital Management:
  1. Given a department ID, get the active appointments for the week.
  2. Given a department ID and a date, find (i) the available appointments, (ii) the active appointments, (iii) the list of patients who made appointments on that given date.
  3. Given a doctor name and a date, list all the available appointments of the doctor for the given date.
  4. Given a patient ID, retrieve the patient details (Name, Age, Gender, Address, Total number of appointments to the hospital, etc.).
  5. Given a department ID and a patient ID, list all the appointments (active or past) made by the patient in that department.
  6. Given a maintenance staff ID, list all the requests addressed by the staff. N.B.: Active appointments are already booked by the patients, Past appointments are already attended by the patients, and Available appointments can be booked by patients.

### Hospital Maintenance Staff:
  1. Given a date range, list all the appointments (past or active) made by a given patient ID.
  2. Given a doctor name, list all maintenance requests made by the doctor.
  3. After a maintenance request is addressed, make necessary entries showing the available appointments for that doctor of the department.

### Patients:
  1. Given a hospital name, find the specialized departments in the hospital.
  2. Given a hospital name and a specialized department name, find all the doctors whose appointments are available on the week.
  3. Given an appointment number, find the appointment details (time slot, doctor name, department, etc.)
  4. Make an appointment for a doctor of a department: 
     - Get on the waitlist for an appointment if the chosen doctor has no available slots in the week.
 
### Doctors:
  1. Make a maintenance request with a list of available appointments (time slot, department name, number of patients per hour, etc.).
