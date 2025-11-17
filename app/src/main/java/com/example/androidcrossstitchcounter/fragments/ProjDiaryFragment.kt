package com.example.androidcrossstitchcounter.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.adapters.ProjDiaryAdapter
import com.example.androidcrossstitchcounter.adapters.ProjectAdapter
import com.example.androidcrossstitchcounter.databinding.ProjDiaryFragmentBinding
import com.example.androidcrossstitchcounter.listeners.SwipeToDeleteCallback
import com.example.androidcrossstitchcounter.models.DataBaseProvider
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.ProjDiaryDao
import com.example.androidcrossstitchcounter.models.ProjDiaryEntry
import com.example.androidcrossstitchcounter.models.Project
import com.example.androidcrossstitchcounter.models.User
import com.example.androidcrossstitchcounter.models.UserDao
import com.example.androidcrossstitchcounter.services.Animation
import com.example.androidcrossstitchcounter.services.CalendarUtils
import com.example.androidcrossstitchcounter.services.Validation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjDiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjDiaryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var projId: Int? = null
    private val binding by lazy {
        ProjDiaryFragmentBinding.inflate(layoutInflater)
    }
    private val app: App by lazy {
        requireActivity().application as App
    }
    private lateinit var diaryDao: ProjDiaryDao
    private lateinit var projDao: ProjDao
    private lateinit var diary: ProjDiary
    private lateinit var project: Project
    private lateinit var diaryAdapter: ProjDiaryAdapter
    private lateinit var diaryNotes: List<ProjDiaryEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            projId = it.getInt("projId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    fun setCalendar(date: EditText) {
        date.isFocusable = false
        date.isClickable = true
        date.setOnClickListener {
            val calendar = CalendarUtils.setDisplayCalendar(requireActivity(), date)
            Validation.checkStartFinishDate(calendar)
            calendar.show()
        }
    }

    fun Animation() {
        binding.aboutProj.setOnClickListener {
            Animation.Companion.hiding(binding.innerLayout)
        }
        binding.statistics.setOnClickListener {
            Animation.Companion.hiding(binding.statisticsInnerL)
        }
    }

    private fun showEditDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Редактировать запись")

        // Создаём View для диалога (можно использовать layout)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_diary, null)

        val editDate = dialogView.findViewById<TextView>(R.id.date)
        val editCross = dialogView.findViewById<EditText>(R.id.editCross)
        val editDateField = dialogView.findViewById<EditText>(R.id.editDate)
        val textMessage = dialogView.findViewById<LinearLayout>(R.id.textMessage)
        val addVal = dialogView.findViewById<RadioButton>(R.id.add)
        val editVal = dialogView.findViewById<RadioButton>(R.id.edit)
        var isEqual = false
        // Заполняем текущие значения
        editDateField.visibility = View.VISIBLE
        editDate.visibility = View.GONE

        editDateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        builder.setView(dialogView)
        setCalendar(editDateField)
        fun handleDateChange() {
            val date = LocalDate.parse(editDateField.text.toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            if(diaryNotes.any{it.diary.date.isEqual(date)}) {
                textMessage.visibility = View.VISIBLE
                isEqual = true
            }
        }
        handleDateChange()

        editDateField.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                textMessage.visibility = View.INVISIBLE
                isEqual = false
                handleDateChange()
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }
        })

        builder.setPositiveButton("Сохранить") { _, _ ->
            // Получаем новые значения
            val newCross = editCross.text.toString().toIntOrNull() ?: return@setPositiveButton
            val date = LocalDate.parse(editDateField.text.toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            if(isEqual) {
                val foundEntry = diaryNotes.find { it.diary.date.isEqual(date) }
                updateDiaryEntry(foundEntry!!.diary, addVal.isChecked, newCross)
            } else {
                addDiaryEntry(date, newCross)
            }
        }
        builder.setNegativeButton("Отмена", null)
        builder.show()
    }

    fun addDiaryEntry(date: LocalDate, crossDayQuantityVal: Int) {
        val diaryEntry = ProjDiary(
            date = date,
            crossQuantity = crossDayQuantityVal,
            projId = projId!!
        )
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.insertProjDiary(diaryEntry)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireActivity(), "Запись добавлена!", Toast.LENGTH_SHORT).show()
                loadEntries()
            }
        }
    }

    fun updateDiaryEntry(diaryEntry: ProjDiary, isAdd: Boolean, newCross: Int) {
        if(isAdd) {
            diaryEntry.crossQuantity += newCross
        } else {
            diaryEntry.crossQuantity = newCross
        }
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.updateProjDiary(diaryEntry)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireActivity(), "Запись обновлена!", Toast.LENGTH_SHORT).show()
                loadEntries()
            }
        }
    }

    fun loadEntries() {
        lifecycleScope.launch {
            val dbEntries = diaryDao.getProjEntriesById(projId!!)
            var done = 0
            val entries = dbEntries.map {
                done += it.crossQuantity + project.stitchedCrossBeforeRegistration
                val remains = project.totalCross?.minus(done)
                ProjDiaryEntry(it, done, remains)
            }
            diaryAdapter.updateDiaryNotes(entries.reversed())
            diaryNotes = entries
        }
    }
    fun loadProject() {
        lifecycleScope.launch {
            project = projDao.getProjectById(projId!!)!!
            binding.headProfile.text = project.projName
            binding.designerVal.text = project.projDesigner ?: "Не указано"
            binding.sizeVal.text = "${project.width} X ${project.height}"
            binding.allCrossVal.text = project.totalCross.toString()
            binding.beforeRegCrossVal.text = project.stitchedCrossBeforeRegistration.toString()
            binding.startDateVal.text = project.startDate
            if(project.projStatusId == 3) {
                binding.finishDate.visibility = View.VISIBLE
                binding.finishDateVal.visibility = View.VISIBLE
                binding.finishDateVal.text = project.finishDate
            }
            binding.stitchedVal.text = "/"
            if(project.projStatusId == 3) {
                binding.restVal.visibility = View.GONE
            }
            binding.restVal.text = "/"
            binding.avgSpeedVal.text = ""
            binding.prognosisVal.text = ""
            binding.percentVal.text = "%"
            binding.necessarySpeedVal.text = "кр./день"
            loadEntries()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseProvider.getDB(requireContext())
        diaryDao = db.diaryDao()
        projDao = db.projDao()

        Animation()

        binding.imageAdd.setOnClickListener {
            showEditDialog()
        }

        binding.diaryList.layoutManager = LinearLayoutManager(requireContext())
        diaryAdapter = ProjDiaryAdapter(emptyList(), diaryDao, projDao,
            requireContext() as LifecycleOwner, binding.diaryList,
        {
            loadEntries()
        }
        )
        binding.diaryList.adapter = diaryAdapter
        val swipeCallback = SwipeToDeleteCallback(diaryAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.diaryList)
        loadProject()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjDiaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjDiaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
