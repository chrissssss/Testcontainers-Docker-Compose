package org.example

import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement


class AuthorRepository(private val connection: Connection) {

    private val INSERT_STATEMENT = "INSERT INTO authors (name ) VALUES (?)"
    private val READ_ALL_STATEMENT = "SELECT id, name FROM authors"

    fun save(author: Author): Author {
        with(connection.prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
            setString(1, author.name)
            val affectedRows = executeUpdate()
            if (affectedRows == 0) {
                throw SQLException("Could not save author")
            }
            with(generatedKeys) {
                if (next()) {
                    return author.copy(id = getLong(1))
                } else {
                    throw SQLException("Could not save author")
                }
            }
        }
    }

    fun findAll(): List<Author> {
        val authors: MutableList<Author> = ArrayList()
        with(connection.prepareStatement(READ_ALL_STATEMENT)) {
            with(executeQuery()) {
                while (next()) {
                    val author = Author(name = getString("name"), id = getLong("id"))
                    authors.add(author)
                }
            }
        }
        return authors
    }

}