package com.example.androidcrossstitchcounter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TableRow
import android.widget.Toast
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.AddProjFragmentBinding

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
    private val binding by lazy {
        AddProjFragmentBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgAvatar.setOnClickListener {
            Toast.makeText(requireActivity(), "Добавление картинки", Toast.LENGTH_SHORT).show()
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

//        val projType = intent.getStringExtra("projType")
//        when (projType) {
//            "present" -> {
//                binding.current.isChecked = true
//                updateVisibility()
//            }
//            "future" -> {
//                binding.future.isChecked = true
//                updateVisibility()
//            }
//            "finish" -> {
//                binding.finish.isChecked = true
//                updateVisibility()
//            }
//        }

        binding.current.setOnClickListener {
            updateVisibility()
        }

        binding.future.setOnClickListener {
            updateVisibility()
        }

        binding.finish.setOnClickListener {
            updateVisibility()
        }
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