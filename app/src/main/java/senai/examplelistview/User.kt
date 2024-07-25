package senai.examplelistview

class User(var id: Int, var nome: String, var password: String, var email: String, var telefone:String){


    override fun toString(): String {
        return this.nome
    }
}