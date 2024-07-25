package senai.examplelistview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import senai.examplelistview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = MyDatabaseHelper(this)
        var listUsers = database.readUsers()
        var adapter = setAdapter(listUsers)

        binding.listaItens.adapter = adapter

        binding.listaItens.setOnItemClickListener { parent, view, position, id ->
            binding.id.text = listUsers.get(position).id.toString()
            binding.nome.setText(listUsers.get(position).nome)
            binding.email.setText(listUsers.get(position).email)
            binding.telefone.setText(listUsers.get(position).telefone)
            binding.password.setText(listUsers.get(position).password)
            this.position = position
        }

        //Atualizar o evento de cadastrar verificando se o usuário já não está cadastrado
        // na lista

        binding.cadastrar.setOnClickListener {

            if(!binding.nome.text.toString().trim().isEmpty() &&
                    !binding.password.text.toString().trim().isEmpty() &&
                        !binding.telefone.text.toString().trim().isEmpty() &&
                            !binding.email.text.toString().trim().isEmpty()){

                val value = database.insertUser(
                    binding.nome.text.toString(),
                    binding.password.text.toString(),
                    binding.email.text.toString(),
                    binding.telefone.text.toString()
                )

                if(value > -1){
                    Snackbar.make(
                        binding.root,
                        "Usuário " + binding.nome.text.toString() + " cadastrado com sucesso!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                listUsers = database.readUsers()
                adapter = setAdapter(listUsers)
                binding.listaItens.adapter = adapter
                //adapter.notifyDataSetChanged()
                limparCampos()
            }else{
                Snackbar.make(binding.root, "ERRO! Campos Vazios!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(com.google.android.material.R.color.design_dark_default_color_background))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }

        }

        binding.atualizar.setOnClickListener{

            var id = binding.id.text.toString().toInt()
            var nome = binding.nome.text.toString()
            var telefone = binding.telefone.text.toString()
            var email = binding.email.text.toString()
            var senha = binding.password.text.toString()

            var user: User = User(id, nome, senha, email, telefone)

            database.updateUser(user)

            listUsers = database.readUsers()
            adapter = setAdapter(listUsers)
            binding.listaItens.adapter = adapter
            limparCampos()
        }

        //Criar o evento para excluir usuários

        binding.excluir.setOnClickListener {
            val value = database.deleteUser(binding.id.text.toString().toInt())

            if (value != 0){
                Snackbar.make(
                    binding.root,
                    "Usuário " + binding.nome.text.toString() + " excluído com sucesso!",
                    Snackbar.LENGTH_SHORT
                    ).show()
            }

            listUsers = database.readUsers()
            adapter = setAdapter(listUsers)
            binding.listaItens.adapter = adapter
            limparCampos()
        }

    }

    fun limparCampos(){
        binding.id.text = ""
        binding.nome.setText("")
        binding.password.setText("")
        binding.email.setText("")
        binding.telefone.setText("")
    }


    fun setAdapter(array: List<User>): ArrayAdapter<User>{
        return ArrayAdapter(this, android.R.layout.simple_list_item_1, array)
    }
}