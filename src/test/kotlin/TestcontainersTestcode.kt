import org.junit.ClassRule
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

open class TestcontainersTestcode {
    private fun create(): Connection {
        val url = postgres.jdbcUrl
        val user = postgres.username
        val password = postgres.password
        return DriverManager.getConnection(url, user, password)
    }

    protected fun withConnection(c: ConnectionConsumer) {
        val connection = create()
        setup(connection)
        c.executeWith(connection)
    }

    private fun setup(c: Connection) {
        val dropStmt = "DROP TABLE IF EXISTS authors"
        val createStmt = "CREATE TABLE authors (id SERIAL, name varchar(255))"
        c.prepareStatement(dropStmt).execute()
        c.prepareStatement(createStmt).execute()
    }

    fun interface ConnectionConsumer {
        fun executeWith(connection: Connection)
    }

    companion object {
        @ClassRule
        @JvmField
        var postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:11.1")
    }
}