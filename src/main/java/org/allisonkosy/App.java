package org.allisonkosy;
import org.allisonkosy.entity.Book;
import org.allisonkosy.entity.Model;
import org.allisonkosy.entity.Request;
import org.allisonkosy.entity.Student;
import org.allisonkosy.runner.Library;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.List;


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
    public static void main( String[] args ) {
        Library library= new Library();
        addUsers(library);
        addBooks(library);
        printAllEntities(library.getAllEntries(Student.printIdentifier(), Student.class));
        printAllEntities(library.getAllEntries(Book.printIdentifier(), Book.class));
      String s=  library.borrowBook("Philip Lahm","The Shinning");
      logger.info(s);
        s=  library.borrowBook("Philip Lahm","The Shinning");
        logger.info(s);
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
            int index  = i /2;
            library.createNewBook(book, authors[index]);
            i++;

        }
    }
    
    public static void printAllEntities(List<? extends Object> list) {
        for (Object o :
                list) {
            logger.info(o);
        }
    }

    public static void close( ) {
        logger.info("Closing");
        entityManagerFactory.close();
    }
}
