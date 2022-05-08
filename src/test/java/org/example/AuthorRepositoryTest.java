package org.example;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AuthorRepositoryTest extends TestcontainersTestcode {


    @Test
    public void save_should_set_id() throws Exception {
        withConnection(connection -> {
            AuthorRepository sut = new AuthorRepository(connection);
            Author author = new Author("Kevin");

            sut.save(author);
            assertEquals(Long.valueOf(1), author.getId());
        });
    }

    @Test
    public void findAll_should_contain_saved_author() throws Exception {
        withConnection(connection -> {
            AuthorRepository sut = new AuthorRepository(connection);

            Author author = new Author("Michael");
            sut.save(author);

            List<Author> authors = sut.findAll();
            assertEquals(1, authors.size());
            assertEquals("Michael", authors.get(0).getName());
        });
    }

}