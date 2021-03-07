package com.risingstar.todolist

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerNotes : RecyclerView
    lateinit var btnAdd : View
    lateinit var btnReset : Button
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: MainAdapter
    lateinit var dbNotes : List<NotesEntity>
    lateinit var rlNoWork : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerNotes = findViewById(R.id.recycler_notes)
        btnAdd = findViewById(R.id.btnAdd)
        btnReset = findViewById(R.id.btnReset)
        rlNoWork = findViewById(R.id.rlNoWork)

        dbNotes = RetriveAsyncMain(this@MainActivity).execute().get()

        val datalist = dbNotes as ArrayList<NotesEntity>

        recyclerAdapter = MainAdapter(this@MainActivity, datalist)
        linearLayoutManager = LinearLayoutManager(this@MainActivity)

        if(dbNotes.isNotEmpty()){
            rlNoWork.visibility = View.INVISIBLE
            recyclerNotes.adapter = recyclerAdapter
            recyclerNotes.layoutManager = linearLayoutManager
        }
        else{
            rlNoWork.visibility = View.VISIBLE
        }



        btnAdd.setOnClickListener {

            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.dialog_add)
            val width = WindowManager.LayoutParams.MATCH_PARENT
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width,height)
            dialog.show()

            val etTitleAdd: EditText = dialog.findViewById(R.id.etTitleAdd)
            val etDescAdd : EditText = dialog.findViewById(R.id.etDescAdd)
            val btAdd : Button = dialog.findViewById(R.id.btAdd)

            lateinit var nlist : NotesEntity
            btAdd.setOnClickListener {
                dialog.dismiss()
                val title = etTitleAdd.text.toString().trim()
                val desc = etDescAdd.text.toString().trim()
                if(!(title.equals("") && desc.equals(""))){
                    nlist = NotesEntity(0,title,desc)
                    if (DBAsyncMain(this@MainActivity,nlist,1).execute().get()){
                        Toast.makeText(this@MainActivity,"Added Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Add Failed", Toast.LENGTH_SHORT).show()
                    }
                    etTitleAdd.setText("")
                    etDescAdd.setText("")
                    datalist.clear()
                    datalist.addAll(RetriveAsyncMain(this@MainActivity).execute().get())
                    recyclerAdapter.notifyDataSetChanged()
                    recreate()
                    /*val intent = getIntent()
                    finish()
                    startActivity(intent)*/
                }
                else{
                    Toast.makeText(this@MainActivity,"Nothing added", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnReset.setOnClickListener {
            if(ResetAsyncMain(this@MainActivity/*,dbNotes*/).execute().get()){
                Toast.makeText(this@MainActivity,"All rows deleted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@MainActivity, "Could not delete all", Toast.LENGTH_SHORT).show()
            }
            datalist.clear()
            //datalist.removeAll(datalist)
            //dbNotes.isEmpty()
            datalist.addAll(RetriveAsyncMain(this@MainActivity).execute().get())
            recyclerAdapter.notifyDataSetChanged()
            recreate()
//            val intent = getIntent()
//            finish()
//            startActivity(intent)
        }


    }
}