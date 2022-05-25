import org.junit.ClassRule
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.Connection
import java.sql.DriverManager

open class TestcontainersTestcode {
    private fun create(): Connection {
        with(postgres) {
            return DriverManager.getConnection(jdbcUrl, username, password)
        }
    }

    protected fun withConnection(func: (Connection) -> Unit) {
        val connection = create()
        setup(connection)
        func.invoke(connection)
    }

    private fun setup(connection: Connection) {
        val dropStmt = "DROP TABLE IF EXISTS authors"
        val createStmt = "CREATE TABLE authors (id SERIAL, name varchar(255))"
        connection.prepareStatement(dropStmt).execute()
        connection.prepareStatement(createStmt).execute()
    }

    companion object {
        @ClassRule
        @JvmField
        var postgres = PostgreSQLContainer<Nothing>("postgres:11.1")
    }
}
