package com.example.androidcrossstitchcounter.fragments

import User
import UserDao
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.ProfileFragmentBinding
import com.example.androidcrossstitchcounter.services.Validation
import com.example.androidcrossstitchcounter.watchers.PhoneMaskWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.net.toUri

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val binding by lazy {
        ProfileFragmentBinding.inflate(layoutInflater)
    }
    private lateinit var userDao: UserDao
    private lateinit var user: User
    private val app: App by lazy {
        requireActivity().application as App
    }
    private var imageUri: Uri? = null

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

    private fun updateUserProp(propName: String, propValue: String) {
        when(propName) {
            "phoneNumber" -> user.phoneNumber = propValue
            "email" -> user.email = propValue
            "password" -> user.password = propValue
        }
        CoroutineScope(Dispatchers.IO).launch {
            userDao.updateUser(user)
        }
    }

    private fun getPrefs() = requireActivity()
            .getSharedPreferences("user_${user.id}", MODE_PRIVATE)
    private val setImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? -> uri?.let {
        imageUri = it
        binding.imgAvatar.setImageURI(it)
        saveImage()
    }
    }

    private fun saveImage() {
        getPrefs().edit().apply {
            putString("image_uri", imageUri.toString())
            apply()
        }
    }

    private fun loadImage() {
        val uri = getPrefs()?.getString("image_uri", null)
        if(uri != null) {
            binding.imgAvatar.setImageURI(uri.toUri())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = app.user!!
        val db = DataBaseProvider.getDB(requireContext())
        userDao = db.userDao()
        //loadImage()
        binding.nameRow.setLabel("Имя")
        binding.patrRow.setLabel("Отчество")

        binding.logRow.setLabel("Логин")
        binding.logRow.setValue(user.login)

        binding.passRow.setLabel("Пароль")
        binding.passRow.setValue(user.password)
        binding.passRow.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        binding.passRow.onSaveValue = fun(newValue) {
            binding.passRow.clearError()
            if (!Validation.Companion.checkPassword(newValue)) {
                binding.passRow.setError("Пароль должен соответствовать критериям сложности")
                return
            }
            if (!Validation.Companion.checkMatch(newValue, binding.repeatPassRow.text.toString())) {
                binding.passRow.setError("Пароли должны совпадать")
                return
            }
            updateUserProp("password", newValue)
        }
        binding.passRow.onEdit = { isEdit ->
            binding.repeatPassRow.visibility = if(isEdit) View.VISIBLE else View.GONE
        }

        binding.phoneRow.setLabel("Телефон")
        binding.phoneRow.setTxtWatcher(PhoneMaskWatcher())
        binding.phoneRow.setValue(user.phoneNumber)
        binding.phoneRow.onSaveValue = { newValue ->
            updateUserProp("phoneNumber", newValue)
        }

        binding.emailRow.setLabel("Email")
        binding.emailRow.setValue(user.email)
        binding.emailRow.onSaveValue = { newValue ->
            updateUserProp("email", newValue)
        }

        binding.birthDateRow.setLabel("Дата рождения")

        binding.imgAvatar.setOnClickListener {
            setImage.launch("image/*")
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
