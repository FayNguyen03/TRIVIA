package com.example.trivia_vivia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class SettingFragment: Fragment() {
    private lateinit var view: View
    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var difficultySpinner: Spinner
    private lateinit var answerTypeRadioGroup: RadioGroup
    private lateinit var imageTypeRadio: RadioButton
    private lateinit var textTypeRadio: RadioButton
    private lateinit var bothTypeRadio: RadioButton
    val radioButtons = listOf(R.id.img_radio, R.id.text_radio, R.id.both_radio)
    private lateinit var rateAppButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.setting, container, false)
        val context = view.context
        // Get references to UI elements
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)
        difficultySpinner = view.findViewById(R.id.difficultySpinner)
        answerTypeRadioGroup = view.findViewById(R.id.answerTypeRadioGroup)
        imageTypeRadio = view.findViewById(R.id.img_radio)
        textTypeRadio = view.findViewById(R.id.text_radio)
        bothTypeRadio = view.findViewById(R.id.both_radio)
        rateAppButton = view.findViewById(R.id.rateAppButton)

        //load saved settings
        loadSettings()

        // Set up listener for Dark Mode Switch
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleDarkMode(isChecked)
        }

        //Set up listerner for difficulty Spinner
        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                saveDifficulty(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set up listener for RadioGroup
        answerTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            saveAnswerType(checkedId)}

        return view
    }

    private fun loadSettings(){
        val sharedPrefs = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)
        val selectedDifficulty = sharedPrefs.getInt("difficulty", 3)

        // Apply saved settings
        darkModeSwitch.isChecked = isDarkMode
        difficultySpinner.setSelection(selectedDifficulty)
        textTypeRadio.isChecked = true

    }
    //Dark Mode
    private fun toggleDarkMode(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        saveDarkModePreference(isDarkMode)
    }

    private fun saveDarkModePreference(isDarkMode: Boolean) {
        val sharedPrefs = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean("dark_mode", isDarkMode)
            apply()
        }
    }

    private fun saveDifficulty(position: Int) {
        val sharedPrefs = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt("difficulty", position)
            apply()
        }
    }

    private fun saveAnswerType(checkedId: Int) {
        val answerType = when (checkedId) {
            R.id.img_radio -> "image"
            R.id.text_radio -> "text"
            R.id.both_radio -> "both"
            else -> "text" // Default to text if none selected
        }

        val sharedPrefs = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString("answer_type", answerType)
            apply()
        }
    }
}