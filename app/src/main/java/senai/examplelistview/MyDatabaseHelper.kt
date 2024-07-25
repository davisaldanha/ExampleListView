package senai.examplelistview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "my_db.db", null, 1)  {

    // Criando variável com script SQL
    val sql = arrayOf(
        "CREATE TABLE user(" +
                "idUser INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "senha TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "telefone TEXT NOT NULL);",
        "INSERT INTO user VALUES(null, 'admin', 'admin', 'admin@teste.com', '000');"
    )


    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
       db.execSQL("DROP TABLE user")
       onCreate(db)
    }

    fun readUsers(): List<User> {
        val users = mutableListOf<User>()

        //1º Opção para consulta no banco
        val cursor = readableDatabase.query( true,"user",null,null,null,null,null,null,null )

        //2º Opção para consulta no banco
        /*val db = this.readableDatabase
         *val cursor = db.rawQuery("SELECT * FROM user", null)
         */

        cursor.use {
            while (it.moveToNext()){
                //Capturar o index de cada coluna na consulta
                var idIndex = it.getColumnIndex("idUser")
                var nomeIdex = it.getColumnIndex("nome")
                var senhaIndex = it.getColumnIndex("senha")
                var emailIndex = it.getColumnIndex("email")
                var telefoneIndex = it.getColumnIndex("telefone")

                //Adicionar o Objeto User a lista users. Os argumentos serão recuperados
                // à partir do nosso cursor
                users.add(
                    User(
                        it.getInt(idIndex),
                        it.getString(nomeIdex),
                        it.getString(senhaIndex),
                        it.getString(emailIndex),
                        it.getString(telefoneIndex)
                    )
                )
            }
        }

        return users
    }

    fun insertUser(nome: String, senha: String, email: String, telefone: String):Long{
        val user = ContentValues().apply {
            put("nome", nome)
            put("senha", senha)
            put("email", email)
            put("telefone", telefone)
        }

        return writableDatabase.insert("user", null, user)
    }

    fun updateUser(user: User): Int{
        val values = ContentValues().apply {
            put("nome", user.nome)
            put("senha", user.password)
            put("email", user.email)
            put("telefone", user.telefone)
        }

        return writableDatabase.update(
            "user",
            values,
            "idUser = ?",
            arrayOf(
                user.id.toString()
            )
        )
    }

    fun deleteUser(id: Int): Int{
        return writableDatabase.delete("user", "idUser = ?", arrayOf(id.toString()))
    }
}












