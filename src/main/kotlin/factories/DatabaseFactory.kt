package factories


import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    class DatabaseConfigBuilder {
        private var driver: String = "org.postgresql.Driver"
        private var url: String = ""
        private var user: String = ""
        private var password: String = ""

        fun driver(driver: String) = apply { this.driver = driver }
        fun url(url: String) = apply { this.url = url }
        fun user(user: String) = apply { this.user = user }
        fun password(password: String) = apply { this.password = password }

        fun connect() {
            Database.connect(url, driver, user, password)
        }
    }

    fun configure() = DatabaseConfigBuilder()
}

