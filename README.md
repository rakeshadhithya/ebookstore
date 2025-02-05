# ebookstore

Backend for an online bookstore application. Implemented using spring boot. Apart from crud operations, used many concepts like validations, file handling, logging, exception handling, pagination and sorting. more in README file

# features

- get all books from the database
- get books with pagination and sorting.
- add a book with cover photo file. file is saved in project folder, other details saved in database.
- update an existing book details. (just details keeping existing photo same or new photo)
- delete book with id

# skills

as mentioned in description.

- Validations for the right data at the server level and database level
- file handling like, uploading a file along with entities and sending entities with its file.
- exception handling, handling all exceptions throughout application in a single class global exception handler
- logging for debugging puposes
- pagination and sorting for getting data from database as per requirements

# installation guide

- make sure you have installed jdk and mysql. versions used for this app: jdk 17, mysql 8.0.40
- download the project,
- make sure database properties are matching as per your local machine.
- create a table manually in mysql with name ebookstoredb. that's it
- run the spring application.

# usage

- after application successfully opened on a server.
- see the controller to see which all apis available
- try out each api with postman or any other tool
