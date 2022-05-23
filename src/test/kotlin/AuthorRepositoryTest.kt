import org.example.Author
import org.example.AuthorRepository
import org.junit.Assert
import org.junit.Test

class AuthorRepositoryTest : TestcontainersTestcode() {
    @Test
    @Throws(Exception::class)
    fun save_should_set_id() {
        withConnection { connection ->
            val sut = AuthorRepository(connection)
            val author = Author(name = "Kevin")
            val savedAuthor = sut.save(author)
            Assert.assertEquals(java.lang.Long.valueOf(1), savedAuthor.id)
        }
    }

    @Test
    @Throws(Exception::class)
    fun findAll_should_contain_saved_author() {
        withConnection { connection ->
            val sut = AuthorRepository(connection)
            val author = Author(name = "Michael")
            sut.save(author)
            val authors = sut.findAll()
            Assert.assertEquals(1, authors.size.toLong())
            Assert.assertEquals("Michael", authors[0].name)
        }
    }
}