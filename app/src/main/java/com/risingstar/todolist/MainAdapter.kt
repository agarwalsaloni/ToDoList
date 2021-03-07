package com.risingstar.todolist

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent.getIntent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainAdapter(val context: Context, val itemList : ArrayList<NotesEntity>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtNotesTitle : TextView = view.findViewById(R.id.txtNotesTitle)
        val txtNotesDesc : TextView = view.findViewById(R.id.txtNotesDesc)
        val btEdit : ImageView = view.findViewById(R.id.bt_edit)
        val btDelete : ImageView = view.findViewById(R.id.bt_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_main,parent,false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val notes = itemList[position]

        holder.txtNotesTitle.text = notes.title
        holder.txtNotesDesc.text = notes.desc

        holder.btEdit.setOnClickListener {
            val n = itemList.get(holder.adapterPosition)

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_update)
            val width = WindowManager.LayoutParams.MATCH_PARENT
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width,height)
            dialog.show()

            val newTitle:EditText = dialog.findViewById(R.id.etTitleUpdate)
            val newDesc : EditText = dialog.findViewById(R.id.etDescUpdate)
            val btnUpdate : Button = dialog.findViewById(R.id.btnUpdate)

            newTitle.setText(n.title)
            newDesc.setText(n.desc)

            btnUpdate.setOnClickListener {
                dialog.dismiss()
                val uTitle = newTitle.text.toString().trim()
                val uDesc = newDesc.text.toString().trim()
                val uNotes = NotesEntity(n.id,uTitle,uDesc)
                if(DBAsyncMain(context,uNotes,3).execute().get()){
                    Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"Update Failed",Toast.LENGTH_SHORT).show()
                }
                itemList.clear()
                itemList.addAll(RetriveAsyncMain(context).execute().get())
                notifyDataSetChanged()
            }
        }

        holder.btDelete.setOnClickListener {
            val pos = holder.adapterPosition
            val d = itemList.get(pos)
            if(DBAsyncMain(context,d,2).execute().get()){
                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Deletion Failed",Toast.LENGTH_SHORT).show()
            }
            itemList.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos,itemList.size)
            (context as Activity).recreate()
        }
    }
}

class DBAsyncMain(context: Context, val notes: NotesEntity, val mode : Int) : AsyncTask<Void,Void,Boolean>(){
    val db = Room.databaseBuilder(context,NotesDatabase :: class.java,"notes_db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        /*
            mode 1 : Insert data
            mode 2 : Delete data
            mode 3 : Update data    .....implemented in next class
            mode 4 : reset data   .....implemented in next class
            mode 5 : get data   .....implemented in next class
        */
        when (mode){
            1->{
                db.notesDao().insert(notes)
                db.close()
                return true
            }
            2->{
                db.notesDao().delete(notes)
                db.close()
                return true
            }
            3->{
                db.notesDao().update(notes.id,notes.title,notes.desc)
                db.close()
                return true
            }
        }
        return false
    }

}

class ResetAsyncMain(context: Context/*, val noteslist : List<NotesEntity>*/) : AsyncTask<Void,Void,Boolean>(){
    val db = Room.databaseBuilder(context,NotesDatabase :: class.java,"notes_db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        db.notesDao().reset(/*noteslist*/)
        db.close()
        return true
    }
}

class RetriveAsyncMain(context: Context) : AsyncTask<Void,Void,List<NotesEntity>>(){
    val db = Room.databaseBuilder(context,NotesDatabase :: class.java,"notes_db").build()
    override fun doInBackground(vararg params: Void?): List<NotesEntity> {
        return db.notesDao().getAllNotes()
    }
}