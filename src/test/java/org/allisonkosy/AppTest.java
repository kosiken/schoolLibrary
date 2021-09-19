package org.allisonkosy;

import org.allisonkosy.entity.Book;
import org.allisonkosy.entity.Request;
import org.allisonkosy.entity.Student;
import org.allisonkosy.runner.Library;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Before
    public void beforeFunc() {
        Library.deleteAll();
    }
    /**
     * Rigorous Test :-)
     */
    @After
    public void close() {
//        App.close();
    }


    @Test
    public void shouldCreateAndAddBookToDb() {
        Library library = new Library();

        Book book = library.createNewBook("Alice in Wonderland", "Lewis Carroll");

        assertNotNull(book);
        assertEquals("Alice in Wonderland", book.getName());
        assertEquals("Lewis Carroll", book.getAuthor());
        Long id = 36L;
        assertEquals( id, book.getId());
    }

    @Test
    public void shouldCreateAndAddStudentToDb() {
        Library library = new Library();

        Student student = library.createNewStudent("Amy Santiago", 30);
        assertNotNull(student);
        assertEquals("Amy Santiago", student.getName());
        assertEquals(Integer.valueOf(30), student.getAge());

        assertEquals( Long.valueOf(74L), student.getId());
    }

    @Test
    public void shouldGetStudentByName() {
        Library library = new Library();
        App.addUsers(library);
        Student student = library.getStudent("Jake Peralta", "name");
        assertNotNull(student);
        assertEquals("Jake Peralta", student.getName());
        assertEquals(Integer.valueOf(34), student.getAge());
    }

    @Test
    public void shouldGetBookByName() {
        Library library = new Library();
        App.addBooks(library);
        Book book = library.getBook("The Shinning", "name");
        assertNotNull(book);
        assertEquals("The Shinning", book.getName());
        assertEquals("Stephen King", book.getAuthor());
    }

    @Test
    public void shouldBorrowBook() {
        Library library = new Library();
        App.addBooks(library);
        App.addUsers(library);

        String response = library.borrowBook("Jake Peralta","The Shinning");

        assertEquals("The Shinning borrowed by Jake Peralta", response);
        Student student = library.getStudent("Jake Peralta", "name");
        assertNotNull(student);

        Request request = library.getBookRequest(student, "student");

        assertNotNull(request);
        assertEquals(student.getId(), request.getStudent().getId());
    }
    @Test
    public void shouldNotBorrowToUnidentifiedUser() {
        Library library = new Library();
        App.addBooks(library);
        App.addUsers(library);
        String response;


        response = library.borrowBook("Unknown User","Oliver Twist");
        assertEquals("Unknown User not found\n", response);
    }

    @Test
    public void shouldNotBorrowUnidentifiedbook() {
        Library library = new Library();
        App.addBooks(library);
        App.addUsers(library);
        String response;


        response = library.borrowBook("Jake Peralta","Unknown Book");
        assertEquals("Unknown Book not found\n", response);
    }



    @Test
    public void shouldNotBorrowBook() {
        Library library = new Library();
        App.addBooks(library);
        App.addUsers(library);

        String response;
        library.borrowBook("Jake Peralta","The Shinning");

        response = library.borrowBook("Jake Peralta","Oliver Twist");
        assertEquals("return the previous borrowed book The Shinning", response);
    }
}
