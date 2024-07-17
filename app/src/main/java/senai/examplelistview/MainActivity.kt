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

        val listUsers = ArrayList<User>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listUsers)

        binding.listaItens.adapter = adapter

        binding.listaItens.setOnItemClickListener { parent, view, position, id ->
            binding.username.setText(listUsers.get(position).username)
            binding.password.setText(listUsers.get(position).password)
            this.position = position
        }

        //Atualizar o evento de cadastrar verificando se o usuário já não está cadastrado
        // na lista
        binding.cadastrar.setOnClickListener {
            if(!binding.username.text.toString().trim().isEmpty() && !binding.password.text.toString().trim().isEmpty()){
                listUsers.add(
                    User(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                )
                adapter.notifyDataSetChanged()
                binding.username.setText("")
                binding.password.setText("")
            }else{
                Snackbar.make(binding.root, "ERRO! Campos Vazios!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(com.google.android.material.R.color.design_dark_default_color_background))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }

        }

        binding.atualizar.setOnClickListener{
            if (position >= 0){
                listUsers.get(position).username = binding.username.text.toString().trim()
                listUsers.get(position).password = binding.password.text.toString().trim()

                adapter.notifyDataSetChanged()

                binding.username.setText("")
                binding.password.setText("")

                position = -1
            }
        }

        //Criar o evento para excluir usuários
    }
}