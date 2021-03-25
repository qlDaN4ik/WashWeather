package com.example.washweather.ui.note

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.washweather.data.db.WeatherDAO
import com.example.washweather.data.db.entity.NoteEntity
import com.example.washweather.databinding.NoteFragmentBinding
import com.example.washweather.ui.forecast.ForecastWeatherViewModel
import com.example.washweather.ui.forecast.ForecastWeatherViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class NoteFragment: Fragment() , DIAware {
    private lateinit var binding: NoteFragmentBinding
    override val di by di()
    private val forecastWeatherViewModelFactory: ForecastWeatherViewModelFactory by instance()
    val notes = mutableListOf<NoteEntity>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = NoteFragmentBinding.inflate(inflater, container, false)
        handleInserts()
        handleUpdates()
        handleDeletes()
        handleViewing()

        val forecastWeatherViewModel = ViewModelProvider(this, forecastWeatherViewModelFactory).get(ForecastWeatherViewModel::class.java)
        forecastWeatherViewModel.getAllNote().observe(viewLifecycleOwner, {
            notes.clear()
            notes.addAll(it)
        })

        return binding.root
    }
    private fun handleInserts() {
        binding.insertBtn.setOnClickListener {
            val id = readId()
            if (id != -1) {
                try {
                    val forecastWeatherViewModel = ViewModelProvider(this, forecastWeatherViewModelFactory).get(ForecastWeatherViewModel::class.java)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            forecastWeatherViewModel.insertNote(NoteEntity(readId(), binding.nameTxt.text.toString()))
                        }
                    }
                    clearEditTexts()
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun handleUpdates() {
        binding.updateBtn.setOnClickListener {
            val id = readId()
            if (id != -1) {
                try {
                    val forecastWeatherViewModel = ViewModelProvider(this, forecastWeatherViewModelFactory).get(ForecastWeatherViewModel::class.java)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            forecastWeatherViewModel.updateNote(NoteEntity(binding.idTxt.text.toString().toInt(), binding.nameTxt.text.toString()))
                        }
                    }
                    clearEditTexts()
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun handleDeletes(){
        binding.deleteBtn.setOnClickListener {
            val id = readId()
            if (id != -1) {
                try {
                    val forecastWeatherViewModel = ViewModelProvider(this, forecastWeatherViewModelFactory).get(ForecastWeatherViewModel::class.java)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            forecastWeatherViewModel.deleteNote(NoteEntity(binding.idTxt.text.toString().toInt()))
                        }
                    }
                    clearEditTexts()
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun handleViewing() {
        binding.viewBtn.setOnClickListener {
            if (notes.isEmpty()) {
                showDialog("Ошибка", "Данных нет")
            }else {
                val buffer = StringBuffer()
                notes.forEach {
                    buffer.append("ID :" + it.id + "\n")
                    buffer.append("TEXT :" + it.text + "\n\n")
                }
                showDialog("Заметки", buffer.toString())
            }
        }
    }

    private fun showToast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun showDialog(title : String, Message : String){
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    private fun clearEditTexts(){
        binding.nameTxt.setText("")
        binding.idTxt.setText("")
    }

    private fun readId(): Int {
        val id: Int
            try {
                id = Integer.parseInt(binding.idTxt.text.toString());
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                showToast("Неверный формат id")
                return -1
            }
        return id
    }
}