package com.example.androidcrossstitchcounter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.Toast
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.ProjDiaryFragmentBinding
import com.example.androidcrossstitchcounter.models.DataBaseProvider
import com.example.androidcrossstitchcounter.models.ProjDiary
import com.example.androidcrossstitchcounter.models.ProjDiaryDao
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
    private lateinit var diary: ProjDiary

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

    fun setCalendar() {
        binding.date.isFocusable = false
        binding.date.isClickable = true
        binding.date.setOnClickListener {
            val calendar = CalendarUtils.setDisplayCalendar(requireActivity(), binding.date)
            Validation.checkStartFinishDate(calendar)
            calendar.show()
        }
    }

    fun changeVisibility(isEdit: Boolean) {
        if(isEdit) {
            binding.addCross.visibility = View.VISIBLE
            binding.imageCheck.visibility = View.VISIBLE
            binding.imageCancel.visibility = View.VISIBLE
            binding.imageAdd.visibility = View.GONE
        } else {
            binding.addCross.visibility = View.GONE
            binding.imageCheck.visibility = View.GONE
            binding.imageCancel.visibility = View.GONE
            binding.imageAdd.visibility = View.VISIBLE
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

    fun addDiaryEntry() {
        val diaryEntry = ProjDiary(
            date = binding.date.text.toString(),
            crossQuantity = binding.crossDayQuantity.text.toString().toInt(),
            projId = projId!!
        )
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.insertProjDiary(diaryEntry)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireActivity(), "Запись добавлена!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseProvider.getDB(requireContext())
        diaryDao = db.diaryDao()

        Animation()
        changeVisibility(false)

        binding.imageAdd.setOnClickListener {
            changeVisibility(true)
        }
        binding.imageCheck.setOnClickListener {
            changeVisibility(false)
        }
        binding.imageCancel.setOnClickListener {
            changeVisibility(false)
        }
        setCalendar()

        binding.imageCheck.setOnClickListener {
            addDiaryEntry()
        }
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
