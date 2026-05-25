package factories


import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://localhost:5432/your_database_name"
        val user = "your_user"
        val password = "your_password"

        Database.connect(jdbcURL, driverClassName, user, password)
    }
}