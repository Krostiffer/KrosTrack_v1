package com.krostiffer.krostrack.ui.run.modes

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.krostiffer.krostrack.MainActivity
import com.krostiffer.krostrack.R



class SpeedSetting : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_speed_setting, container, false)
        val mainAct: MainActivity = activity as MainActivity
        val seperatorSymbol = root.findViewById<TextView>(R.id.seperatorSymbol)
        val seperatorSymbol2 = root.findViewById<TextView>(R.id.seperatorSymbol2)

        //Die Picker werden auf die in sharedPreference gespeicherten Werte initialisiert und beim ändern wird der Wert sofort in die Einstellungen übernommen
        //links
        val leftPick = root.findViewById<NumberPicker>(R.id.leftPicker)
        leftPick.minValue = 0
        leftPick.maxValue = 99
        leftPick.value = mainAct.prefs!!.getInt(mainAct.LEFT_STORE, 0)
        leftPick.setOnValueChangedListener{ _, _, _ ->
            changePref(mainAct.LEFT_STORE, leftPick.value)
        }
        //mitte
        val middlePick = root.findViewById<NumberPicker>(R.id.middlePicker)
        middlePick.minValue = 0
        middlePick.maxValue = 99
        middlePick.value = mainAct.prefs!!.getInt(mainAct.MIDDLE_STORE, 0)
        middlePick.setFormatter(NumberPicker.Formatter((fun (value:Int): String { return String.format("%02d", value)} )))
        middlePick.setOnValueChangedListener{ _, _, _ ->
            changePref(mainAct.MIDDLE_STORE, middlePick.value)
        }
        //rechts
        val typeSet = root.findViewById<NumberPicker>(R.id.rightPicker)
        typeSet.minValue = 0
        typeSet.maxValue = 1
        typeSet.displayedValues = arrayOf("km/h", "min/km")
        typeSet.value = mainAct.prefs!!.getInt(mainAct.RIGHT_STORE, 0)

        //ruft das changen von max und sperator einmal am anfang auf, damit bei gespeicherten werten auch die richtigen symbole/max stehen
        changeMaxAndSeperator(typeSet.value, seperatorSymbol, seperatorSymbol2, leftPick, middlePick)

        //Beim Moduswechsel wird max und seperator gewechselt und der Modus in den sharedPreferences gespeichert
        typeSet.setOnValueChangedListener { _, _, _ ->
            changeMaxAndSeperator(typeSet.value, seperatorSymbol, seperatorSymbol2, leftPick, middlePick)
            changePref(mainAct.RIGHT_STORE, typeSet.value)
        }

        return root
    }
    //ändert den Seperator von ":" zu "," sowie das maximum der Zahlen-Picker von 59 zu 99 bei Wechseln auf min/km bzw. km/h
    private fun changeMaxAndSeperator(valuePickerRight:Int, seperatorSymbol: TextView, seperatorSymbol2: TextView, leftPick: NumberPicker, middlePick: NumberPicker) {
        if (valuePickerRight == 0) {
            seperatorSymbol.visibility = View.INVISIBLE
            seperatorSymbol2.visibility = View.VISIBLE
            leftPick.maxValue = 99
            middlePick.maxValue = 99
        } else {
            seperatorSymbol2.visibility = View.INVISIBLE
            seperatorSymbol.visibility = View.VISIBLE
            leftPick.maxValue = 59
            middlePick.maxValue = 59
        }
    }
    //Hilfsfunktion um einfach sharedPreferences in der MainActivity zu ändern
    private fun changePref(side:String, value: Int) {
        val mainAct: MainActivity = activity as MainActivity
        val editor = mainAct.prefs!!.edit()
        editor.putInt(side, value)
        editor.apply()
    }

}