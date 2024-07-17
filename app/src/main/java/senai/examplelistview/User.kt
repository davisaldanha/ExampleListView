package senai.examplelistview

class User(var Nome: String, var password: String, var email: String, var telefone:String){


    override fun toString(): String {
        return this.Nome
    }
}