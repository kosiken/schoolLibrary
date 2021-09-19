package org.allisonkosy;
import org.allisonkosy.entity.Book;
import org.allisonkosy.entity.Model;
import org.allisonkosy.entity.Request;
import org.allisonkosy.entity.Student;
import org.allisonkosy.runner.Library;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class App {
    public static final Logger logger = LogManager.getLogger(App.class);

    public static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("schoolLibrary");
    public static final String csv = "John Lennon,23\n"
    +"Philip Lahm,22\n"+
            "Jake Peralta,34\n"
    +"Frank Sinatra,44\n"
            + "Phoebe Buffay,33\n" + "Kelechi Edwards, 22";

    public static String[] csv2 = ("The Lord of the Rings,The Silmarillion," +

            "The Shawshank Redemption,The Shinning,Oliver Twist,Ebenezer Scrouge," +

            "Things Fall Apart,Arrow of God").split(",");
    public static Student currentStudent = null;
    public static Request currentRequest = null;
    public static void main( String[] args ) {
        Scanner in = new Scanner(System.in);
        Library library= new Library();
        addUsers(library); // add users to db
        addBooks(library); // add books to db
        int choice;




        System.out.println("Welcome to the library");
        while (true) {
            if(currentStudent != null) { // user is already logged in
                System.out.println("Input 3 to borrow book\nInput 4 to return book\nInput 5 to logout \nInput >=6 to close");
                try {
                    choice = in.nextInt();
                    System.out.println(choice);
                    if(choice>= 6) {
                        // user wants to end program
                        break;
                    }

                    else if (choice == 3 ) {
                        borrowBook(in,library);
                        continue;
                    }
                    else if(choice == 4) {
                       returnBook(library);
                    }
                    else if(choice == 5) {
                        // log out by resetting the current student
                        currentStudent = null;
                    }
                    continue;



                }
                catch (InputMismatchException err) {
                    System.out.println("Incorrect input");
                    in.nextLine();

                }

            }
            System.out.println("Input 1 to create student\nInput 2 to log in as existing student");
            System.out.println("Input >=3 to close");
            try {
                choice = in.nextInt();
                System.out.println(choice);
                if(choice>= 3) {
                    break;
                }
                else if(choice == 1) {
                    createNewStudent(in, library);
                }
               else if(choice == 2) {
                    selectExistingStudent(in, library);
                }




            }
            catch (InputMismatchException err) {
                System.out.println("Incorrect input");
                in.nextLine();

            }


        }


        close();




    }




     public static void addUsers(Library library) {

        String[] rows = csv.split("\n");
        int len = rows.length;
        for (int i = 0; i < len; i++) {
            String[] lines = rows[i].split(",");
            String name = lines[0];

            library.createNewStudent(lines[0], Integer.parseInt(lines[1].strip()));
        }
    }

    public static void addBooks(Library library) {
        String[] authors = {
                "J.R.R Tolkien",
                "Stephen King",
                "Charles Dickens",
                "Chinua Achebe"
        };

        int i = 0;
        for (String book :
                csv2) {
            int index  = i /2; // each author will havve 2 books in the db
            library.createNewBook(book, authors[index]);
            i++;

        }
    }
    
    public static void printAllEntities(List<? extends Object> list) {
        for (Object o :
                list) {
            System.out.println(o);
        }
    }

    public static void close( ) {
        logger.info("Closing");
        entityManagerFactory.close();
    }

    public static void createNewStudent(Scanner in, Library library) {
        in.nextLine();
        String[] questions = {
                "What is your username: ",
                "What is your age: "
        };

        int index = 0;
        String name = "";
        int age = 0;

        while (index <  2) {
            System.out.print(questions[index]);

          if(index<1)  name = in.nextLine();
          else {
             try {
                  age = in.nextInt();
              }
             catch (InputMismatchException e) {
                 System.out.println("Incorrect input");
                 in.nextLine();
                 continue;
             }
          }
          index++;
        }
        Student student = library.createNewStudent(name,  age);
        currentStudent = student;
        if (currentStudent == null) {
            System.out.println("Cannot use " + name );
        }
    }

    public static void selectExistingStudent(Scanner in, Library library) {
        in.nextLine();
        printAllEntities(library.getAllEntries(Student.printIdentifier(), Student.class));
        int index = -1;

        while (index == -1) {
            System.out.println("Input an id to select a student");
            try {
                index = in.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Incorrect input");
                in.nextLine();

            }
        }

        currentStudent = library.getStudent(Long.valueOf(index), "id");

    }
    public static void borrowBook(Scanner in, Library library) {
        in.nextLine();
        printAllEntities(library.getAllEntries(Book.printIdentifier(), Book.class));
        Book book = null;
        String s = "";
        int index = -1;

        while (index == -1) {
            System.out.println("Input an id or name to select a student");
            if(in.hasNextInt()){
                // user inputted a number
                try {
                    index = in.nextInt();
                    book = library.getBook(Long.valueOf(index),"id");
                } catch (InputMismatchException e) {
                    System.out.println("Incorrect input");
                    in.nextLine();

                }
            }
            else {
                // user inputed the name of the book
                s = in.nextLine();
                book = library.getBook(s,"name");
                break;
            }
        }
        if (book == null) { // no book found with the input provided
            logger.info("Invalid selection");

            borrowBook(in, library);
            return;
        }

        String response = library.borrowBook(currentStudent.getName(), book.getName());
        currentRequest = library.getBookRequest(currentStudent, "student");
        logger.info(response);
    }

    public static void returnBook(Library library){
        if(currentRequest == null) {
            logger.info("Nothing to return");
            return;
        }
        library.returnBook(currentRequest);
        currentRequest = null;
    }
}
