package com.pro.app.ui.views.activities

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appizona.yehiahd.fastsave.FastSave
import com.pro.app.R
import com.pro.app.utils.TextHighlighter
import kotlinx.android.synthetic.main.activity_logs.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList


class LogsActivity : AppCompatActivity() {

    lateinit var adapter: LogsAdapter
    var arrLogs = ArrayList<ModelLog>()

    var foundAtArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        var highlighter = TextHighlighter()
                .setBackgroundColor(Color.parseColor("#FFFF00"))
                .setForegroundColor(Color.BLACK)
                .setBold(true)
                .setItalic(true)

        rcLogs.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        adapter = LogsAdapter(arrLogs, highlighter)
        rcLogs.adapter = adapter

        loadLogs()

        imgClear.setOnClickListener {
            et_search.setText("")
        }

        imgMenu.setOnClickListener {
            FastSave.getInstance().saveString("logs", "")
            loadLogs()
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence, p1: Int, p2: Int, p3: Int) {

                foundAtArray.clear()

                if (str.isNotEmpty()) {
                    adapter.textToHightlight = str.toString()
                    for (i in arrLogs.indices) {
                        if (arrLogs[i].logstring.toLowerCase().contains(str.toString().toLowerCase())) {
                            foundAtArray.add(i)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    if (foundAtArray.size > 0) {
                        rcLogs.scrollToPosition(foundAtArray[0])
                    }
                } else {
                    adapter.textToHightlight = ""
                    adapter.notifyDataSetChanged()
                }
            }

        })


    }

    fun loadLogs() {

        arrLogs.clear()
        val lines = FastSave.getInstance().getString("logs", "").split(System.getProperty("line.separator")!!)
        for (i in lines.indices) {
            when {
                isJSONValid(lines[i]) -> {
                    var modellog = ModelLog(formatString(lines[i]), "json")
                    arrLogs.add(modellog)
                }

                lines[i].contains("http") -> {
                    var modellog = ModelLog(formatString(lines[i]), "http")
                    arrLogs.add(modellog)
                }
                else -> {
                    var modellog = ModelLog(formatString(lines[i]))
                    arrLogs.add(modellog)
                }
            }
        }
        adapter.notifyDataSetChanged()

        if (FastSave.getInstance().getString("logs", "") == "") {
            txtNoLogs.visibility = View.VISIBLE
        } else {
            txtNoLogs.visibility = View.GONE
        }

    }

    fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }

        }
        return true
    }

    fun formatString(text: String): String {

        val json = StringBuilder()
        var indentString = ""

        for (i in 0 until text.length) {
            val letter = text[i]
            when (letter) {
                '{', '[' -> {
                    json.append("\n" + indentString + letter + "\n")
                    indentString += "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append("\n" + indentString + letter)
                }
                ',' -> json.append(letter + "\n" + indentString)

                else -> json.append(letter)
            }
        }

        return json.toString()
    }

    class ModelLog(var logstring: String = "",
                   var logtype: String = "")

    class LogsAdapter(private var listLogs: ArrayList<ModelLog>, var highlighter: TextHighlighter)
        : RecyclerView.Adapter<LogsAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            bindViews(holder, position)
        }

        private fun bindViews(holder: MyViewHolder, position: Int) {

            if (textToHightlight == "") {
                holder.txt_log.text = listLogs[position].logstring
            } else {
                holder.txt_log.text = highlighter.highlightString(listLogs[position].logstring, textToHightlight, TextHighlighter.BASE_MATCHER)
            }

            if (listLogs[position].logtype == "http") {
                holder.txt_log.setTextColor(Color.parseColor("#3B14BE"))
            } else if (listLogs[position].logtype == "json") {
                holder.txt_log.setTextColor(Color.parseColor("#8C0032"))
            } else {
                holder.txt_log.setTextColor(Color.parseColor("#000000"))
            }
        }

        override fun getItemCount(): Int {
            return listLogs.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val txt_log: TextView = itemView.findViewById(R.id.txt_log) as TextView
        }

        var textToHightlight = ""

        fun setTextToHighLight(textToHightlight: String) {
            this.textToHightlight = textToHightlight
        }
    }
}