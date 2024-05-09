Key Features:

Person Search:

Search for individuals based on various criteria, including: person type, name, age, PESEL (Polish national identification number), gender, height, weight, email address, salary (for employees), university (for students), and pension (for retirees).
Numeric value searches allow specifying a range (from, to).
Text value searches utilize the "contains ignore case" method.
Paginated search results.
Adding Persons:

Add new individuals of any type (employee, student, retiree).
API contract enables adding new person types without modifying existing classes.
Meaningful data validation and error handling.
Person Editing:

Edit the details of any person in the system.
Optimistic locking mechanism prevents data overwriting by concurrent transactions.
Employee Position Management:

Assign work positions to employees with specific names, salaries, and validity periods.
Prevent overlapping validity periods for positions assigned to the same employee.
CSV Data Import:

Import large volumes of person data from CSV files.
Background importing with the ability to monitor import status.
Handle CSV files with headers and person data in subsequent rows.
Allow only one CSV file import at a time.
Security:

Authorize access to endpoints based on user roles (admin, importer, employee).
Only administrators can add persons.
Only administrators can edit persons.
Administrators or importers can import persons.
Administrators or other employees can assign positions.
Testing:

Cover all functionalities with integration tests.
