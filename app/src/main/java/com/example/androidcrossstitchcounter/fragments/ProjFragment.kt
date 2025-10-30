package com.example.androidcrossstitchcounter.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.activities.AddProjActivity
import com.example.androidcrossstitchcounter.activities.MainActivity
import com.example.androidcrossstitchcounter.activities.ProjDiaryActivity
import com.example.androidcrossstitchcounter.adapters.ProjectAdapter
import com.example.androidcrossstitchcounter.databinding.ProjFragmentBinding
import com.example.androidcrossstitchcounter.models.AppDataBase
import com.example.androidcrossstitchcounter.models.DataBaseProvider
import com.example.androidcrossstitchcounter.models.ProjDao
import com.example.androidcrossstitchcounter.services.Animation
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val mainActivity get() = requireActivity() as MainActivity
    private val binding by lazy {
        ProjFragmentBinding.inflate(layoutInflater)
    }
    private lateinit var currentAdapter: ProjectAdapter
    private lateinit var futureAdapter: ProjectAdapter
    private lateinit var finishedAdapter: ProjectAdapter
    private lateinit var projectDao: ProjDao

    private val app: App by lazy {
        requireActivity().application as App
    }

    fun loadProjects() {
        lifecycleScope.launch {
            val projects = projectDao.getProjectByUserId(app.user!!.id)
            currentAdapter.updateProjects(projects.filter { it.projStatusId == 2 })
            futureAdapter.updateProjects(projects.filter { it.projStatusId == 1 })
            finishedAdapter.updateProjects(projects.filter { it.projStatusId == 3 })
        }
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
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun goToFragment(value: String) {
        val addProjFragment = AddProjFragment()
        val bundle = Bundle()
        bundle.putString("projType", value)
        addProjFragment.arguments = bundle
        mainActivity.toggleFragment(addProjFragment)
    }

    private fun goToFragment(value: Int) {
        val projDiaryFragment = ProjDiaryFragment()
        val bundle = Bundle()
        bundle.putInt("projId", value)
        projDiaryFragment.arguments = bundle
        mainActivity.toggleFragment(projDiaryFragment)
    }

    fun Animation() {
        binding.present.setOnClickListener {
            Animation.Companion.hiding(binding.currentProjHead)
            Animation.Companion.hiding(binding.currentList)
        }
        binding.future.setOnClickListener {
            Animation.Companion.hiding(binding.futureProjHead)
            Animation.Companion.hiding(binding.futureList)
        }
        binding.finish.setOnClickListener {
            Animation.Companion.hiding(binding.finishedProjHead)
            Animation.Companion.hiding(binding.finishedList)
        }
    }

    fun setValues() {
        binding.userName.text = app.user!!.userName ?: "не указано"
        binding.regDate.text = app.user!!.regDate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValues()
        val db = AppDataBase.getInstance(requireContext())
        projectDao = db.projDao()
        binding.logProj.setOnClickListener {
            mainActivity.toggleFragment(mainActivity.binding.profile,
                ProfileFragment())
        }
        binding.avgSpeed.setOnClickListener {
            goToFragment(1)
        }
        // Переходы на страницы добавления проектов
        binding.imageAdd.setOnClickListener {
            goToFragment("present")
        }
        binding.imageAddF.setOnClickListener {
            goToFragment("future")
        }
        binding.imageAddFinish.setOnClickListener {
            goToFragment("finish")
        }

        Animation()

        binding.currentList.layoutManager = LinearLayoutManager(requireContext())
        binding.futureList.layoutManager = LinearLayoutManager(requireContext())
        binding.finishedList.layoutManager = LinearLayoutManager(requireContext())
        currentAdapter = ProjectAdapter(emptyList())
        binding.currentList.adapter = currentAdapter
        futureAdapter = ProjectAdapter(emptyList())
        binding.futureList.adapter = futureAdapter
        finishedAdapter = ProjectAdapter(emptyList())
        binding.finishedList.adapter = finishedAdapter
        loadProjects()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
