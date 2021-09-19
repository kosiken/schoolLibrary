package org.allisonkosy.runner;

import org.allisonkosy.App;
import org.allisonkosy.entity.Book;

import org.allisonkosy.entity.Request;
import org.allisonkosy.entity.Student;

import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.*;

import java.util.List;

public class Library {
    /**
     * Used to create query strings for the database
     * @param modelName the name of the model to query
     * @param property the column to query by
     * @param query the placeholder to attach to find in the database
     * @return the query string
     * for example getQueryString("Student", "name", "property")
     * => "SELECT b FROM org.allisonKosy.entity.Student b WHERE b.name = :property"
     */
    private String getQueryString(String modelName, String property, String query) {

         return  "SELECT b FROM " + modelName + " b WHERE b." + property + " = :" + query;
    }

    /**
     * Find a student by one of the Student class's properties
     * @param object the value of the property
     * @param property the name of the property
     * @return a student if found or else null
     */
    public Student getStudent(Object object, String property) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        String query = getQueryString(Student.modelName(), property, "property");
        TypedQuery<Student> typedQuery = entityManager.createQuery(query, Student.class);
        Student student = null;
        typedQuery.setParameter("property", object);
        try {
            student = typedQuery.getSingleResult();
            App.logger.info(student);
        }  catch (NoResultException exception){
            App.logger.info("Not found");
            return null;
        }
        catch (Exception err) {

            err.printStackTrace();
            entityManager.close();
            return null;
        }
        return student;

    }

    /**
     * Find a book by one of the Book class's properties
     * @param object the value of the property
     * @param property the name of the property
     * @return a student if found or else null
     */
    public  Book getBook(Object object, String property) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        String query = getQueryString(Book.modelName(), property, "property");
        TypedQuery<Book> typedQuery = entityManager.createQuery(query, Book.class);
        Book book = null;
        typedQuery.setParameter("property", object);
        try {
            book = typedQuery.getSingleResult();
            App.logger.info(book);
        }
        catch (NoResultException exception){
            App.logger.info("Not found");
            return null;
        }
        catch (Exception err) {

            err.printStackTrace();
            entityManager.close();
            return null;
        }
        return book;

    }
    public Request getBookRequest(Object object, String property) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        String query = getQueryString(Request.modelName(), property, "property");

        TypedQuery<Request> typedQuery = entityManager.createQuery(query,Request.class);
        App.logger.info(query);
        Request request = null;
        typedQuery.setParameter("property", object);
        try {
            request = typedQuery.getSingleResult();
            App.logger.info(request);
        }
        catch (NoResultException exception){
            App.logger.info("Not found");
            return null;
        }
        catch (Exception err) {

            err.printStackTrace();
            entityManager.close();
            return null;
        }
        return request;

    }

    private Request createNewRequest( Student student, Book book) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        Request request = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            request = new Request();
            request.setBook(book);
            request.setStudent(student);


//            student.setId(id);

            entityManager.persist(request);
            transaction.commit();
            App.logger.info("DONE HERE");

        }
        catch (Exception err) {
            if (transaction != null) {
                transaction.rollback();

            }
            err.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return request;
    }
    public Student createNewStudent( String name, int age) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        Student student = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
           student =new Student();
            student.setAge(age);
//            student.setId(id);
            student.setName(name);
            entityManager.persist(student);
            transaction.commit();
            App.logger.info("DONE HERE");
        }
        catch (ConstraintViolationException | RollbackException exception) {
            if (transaction != null) {
                transaction.rollback();

            }
            App.logger.error("Student with " + name + " exists");

            student = null;
        }
        catch (Exception err) {
            if (transaction != null) {
                transaction.rollback();

            }
            err.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return student;
    }

    public Book createNewBook( String name, String author)  {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;Book book =null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            book =new Book();
            book.setAuthor(author);
            book.setName(name);
            entityManager.persist(book);
            transaction.commit();
            App.logger.info("DONE HERE");
        }
        catch (ConstraintViolationException | RollbackException exception) {
            if (transaction != null) {
                transaction.rollback();

            }
            App.logger.error("Book with " + name + " exists");

            book = null;
        }
        catch (Exception err) {
            if (transaction != null) {
                transaction.rollback();

            }
            err.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return book;
    }


        public List<? extends  Object> getAllEntries (String query, Class var2) {
        EntityManager entityManager = App.entityManagerFactory.createEntityManager();
        Query q = entityManager.createQuery(query, var2);

        return q.getResultList();

    }

    public List<Student> getAllStudents() {
        return  (List<Student>) getAllEntries("FROM Student s", Student.class);
    }

    public String borrowBook(String studentName, String bookName) {
        Student s = getStudent(studentName, "name");
        Book b = getBook(bookName, "name");
        boolean notFound = false;
        StringBuilder builder = new StringBuilder();
      if(s== null ) {
          builder.append(studentName).append(" not found").append('\n');
          notFound = true;

      }
      if(b == null) {
          builder.append(bookName).append(" not found").append('\n');
          notFound = true;
      }
      if (!notFound){
          builder.append(borrowBook(s, b));
      }
      return builder.toString();
    }
    private String borrowBook(Student student, Book book) {
        Request request = getBookRequest(student, "student");
        if(request == null) {
            request = createNewRequest(student, book);
        }
        else {
            return "return the previous borrowed book " + request.getBookName();
        }

        return request.getBookName() + " borrowed by " + student.getName();

    }

    /**
     * Remove all entries from the db
     */
    public static void deleteAll() {

        EntityManager em = App.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        transaction =em.getTransaction();
        try {

            transaction.begin();

            Query q3 = em.createQuery("DELETE FROM " + Student.modelName());
            Query q4 = em.createQuery("DELETE FROM " + Book.modelName());
            Query q1 = em.createQuery("DELETE FROM " + Request.modelName());


            q1.executeUpdate();

            q3.executeUpdate();
            q4.executeUpdate();

            transaction.commit();
        } catch (    SecurityException | IllegalStateException | RollbackException  e) {
            e.printStackTrace();
        }

    }
    public void returnBook(Request request) {
        App.logger.info(request.getStudentName() +" returning " + request.getBookName());
        EntityManager em = App.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        transaction =em.getTransaction();
        try {

            transaction.begin();

            Request r = em.find(Request.class, request.getId());
            em.remove(r);



            transaction.commit();
        } catch (    SecurityException | IllegalStateException | RollbackException  e) {
            e.printStackTrace();
        }

    }
}
