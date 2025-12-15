package com.example.androidcrossstitchcounter.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TableRow
import android.widget.Toast
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.activities.MainActivity
import com.example.androidcrossstitchcounter.databinding.AddProjFragmentBinding
import com.example.androidcrossstitchcounter.models.AppDataBase
import com.example.androidcrossstitchcounter.models.DataBaseProvider
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.Project
import com.example.androidcrossstitchcounter.services.CalendarUtils
import com.example.androidcrossstitchcounter.services.Validation
import com.example.androidcrossstitchcounter.utils.toStringOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddProjFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProjFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var projType: String? = null
    private lateinit var projDao: ProjDao
    private val binding by lazy {
        AddProjFragmentBinding.inflate(layoutInflater)
    }
    private val app: App by lazy {
        requireActivity().application as App
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            projType = it.getString("projType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseProvider.getDB(requireContext())
        projDao = db.projDao()
        binding.imgAvatar.setOnClickListener {
            Toast.makeText(requireActivity(), "Добавление картинки", Toast.LENGTH_SHORT).show()
        }

        fun addProj() {
            val project = Project(
                projName = binding.nameProj.text.toString(),
                userId = app.user!!.id,
                projDesigner = binding.designerProj.text.toString().toStringOrNull(),
                totalCross = binding.totalCross.text.toString().toIntOrNull(),
                finishDreamDate = binding.finishDream.text.toString().toStringOrNull(),
                stitchedCrossBeforeRegistration = binding.beforeRegCross.text.toString().toIntOrNull() ?: 0,
                startDate = binding.startDate.text.toString().toStringOrNull(),
                finishDate = binding.finishDate.text.toString().toStringOrNull()?.let { LocalDate.parse(it)
                },
                width = binding.width.text.toString().toIntOrNull() ?: 0,
                height = binding.height.text.toString().toIntOrNull() ?: 0,
                projStatusId =
                    when {
                        binding.current.isChecked -> 2
                        binding.future.isChecked  -> 1
                        binding.finish.isChecked -> 3
                        else -> 1
                    }
            )
            CoroutineScope(Dispatchers.IO).launch {
                projDao.insertProject(project)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireActivity(), "Проект добавлен!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun updateVisibility() {
            when {
                binding.current.isChecked -> {
                    binding.finishRow.visibility = View.GONE
                    binding.stitchesBeforeRow.visibility = View.VISIBLE
                    binding.startDateRow.visibility = View.VISIBLE
                    binding.planDateRow.visibility = View.VISIBLE
                }
                binding.future.isChecked -> {
                    binding.finishRow.visibility = View.GONE
                    binding.stitchesBeforeRow.visibility = View.GONE
                    binding.startDateRow.visibility = View.GONE
                    binding.planDateRow.visibility = View.VISIBLE
                }
                binding.finish.isChecked -> {
                    binding.finishRow.visibility = View.VISIBLE
                    binding.stitchesBeforeRow.visibility = View.GONE
                    binding.startDateRow.visibility = View.VISIBLE
                    binding.planDateRow.visibility = View.GONE
                }
            }
        }

        when (projType) {
            "present" -> {
                binding.current.isChecked = true
                updateVisibility()
            }
            "future" -> {
                binding.future.isChecked = true
                updateVisibility()
            }
            "finish" -> {
                binding.finish.isChecked = true
                updateVisibility()
            }
        }

        binding.current.setOnClickListener {
            updateVisibility()
        }

        binding.future.setOnClickListener {
            updateVisibility()
        }

        binding.finish.setOnClickListener {
            updateVisibility()
        }

        binding.saveBtn.setOnClickListener {
            addProj()
        }

        fun showCalendar(editText: EditText) {
            editText.isFocusable = false
            editText.isClickable = true

            editText.setOnClickListener {
                try {
                    val calendarDialog = CalendarUtils.setDisplayCalendar(requireActivity(), editText)
                    when (editText) {
                        binding.finishDream -> {
                            Validation.checkPlanDate(calendarDialog)
                        }
                        else -> {
                            Validation.checkStartFinishDate(calendarDialog)
                        }
                    }
                    calendarDialog.show()
                } catch (e: Exception) {
                    Log.e("CalendarError", "Ошибка при показе календаря: ${e.message}")
                }
            }
        }
        showCalendar(binding.startDate)
        showCalendar(binding.finishDream)
        showCalendar(binding.finishDate)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddProjFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddProjFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}