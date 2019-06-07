package com.courtney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import java.net.URL

class MainActivity : AppCompatActivity() {


    private val RC_NICKNAME: Int = 101
    private val RC_SIGNUP: Int = 100
    var cacheService: Intent? = null
    val TAG = MainActivity::class.java.simpleName
    val auth = FirebaseAuth.getInstance()
    val signup = false
    val functions = listOf("Camera",
        "Invite Friends",
        "Parking",
        "Download coupons",
        "News",
        "Movies",
        "Maps")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /*if(!signup) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/

        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }

        // Spinner
        val colors = arrayOf("Red", "Blue", "Green")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: ${colors[position]}")
            }
        }

        // RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()

    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_function, parent, false)
            return FunctionHolder(view)
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.functionText.text = functions[position]
            holder.itemView.setOnClickListener {
                functionClicked(holder, position)
            }
        }
    }

    private fun functionClicked(holder: FunctionHolder, position: Int) {
//        Log.d(TAG, "functionClicked: $position")
        when(position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            5 -> startActivity(Intent(this, MovieActivity::class.java))
        }
    }


    class FunctionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var functionText : TextView = itemView.txt_functions
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        } else {
            Log.d(TAG, "authChanged: ${auth.currentUser?.uid}")
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
//        txt_nickname.text = getNickname()
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    txt_nickname.text = dataSnapshot.value as String
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICKNAME)
            }
        }
        if(requestCode == RC_NICKNAME) {
            if(resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_cache -> {
                doAsync {
                    val json = URL("https://api.myjson.com/bins/wk2gd").readText()
                    val movies = Gson().fromJson<List<Movie>>(json,
                        object : TypeToken<List<Movie>>(){}.type)
                    val movie = movies[0]
                    startService(intentFor<CacheService>(
                        "TITLE" to movie.Title,
                        "URL" to movie.Poster
                    ))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        stopService(cacheService)
    }
}
