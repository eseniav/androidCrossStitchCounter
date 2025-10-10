package com.example.androidcrossstitchcounter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.SettingsFragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val binding by lazy {
        SettingsFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Адаптер для спинера
        val adapterStartPage = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.startPageArray,
            R.layout.spinner_item
        ).also {
            it.setDropDownViewResource(R.layout.spinner_item)
        }
        binding.startPage.adapter = adapterStartPage

        val adapterLanguage = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languageArray,
            R.layout.spinner_item
        ).also {
            it.setDropDownViewResource(R.layout.spinner_item)
        }
        binding.languageSpinner.adapter = adapterLanguage

        binding.startPage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view == null) return
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Все проекты" ->  {
                        binding.projSpinner.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Все проекты", Toast.LENGTH_SHORT).show()
                    }
                    "Профиль" -> {
                        binding.projSpinner.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Профиль", Toast.LENGTH_SHORT).show()
                    }
                    "Проект" -> {
                        binding.projSpinner.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Проект", Toast.LENGTH_SHORT).show()
                    }
                    "Настройки" -> {
                        binding.projSpinner.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Настройки", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Обработка случая, когда ничего не выбрано
            }
        }

        binding.themes.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.light -> {
                    Toast.makeText(requireContext(), "Выбрана светлая тема", Toast.LENGTH_SHORT).show()
                }
                R.id.dark -> {
                    Toast.makeText(requireContext(), "Выбрана темная тема", Toast.LENGTH_SHORT).show()
                }
                R.id.system -> {
                    Toast.makeText(requireContext(), "Выбрана системная тема", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
