package com.ethereum.securex

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethereum.securex.adapters.CredentialsAdapter
import com.ethereum.securex.fragments.AddFragment
import com.ethereum.securex.fragments.KeyFragment
import com.ethereum.securex.models.Credentials
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_key.*

class MainActivity : AppCompatActivity() {

    private var credentialsList = ArrayList<Credentials>()
    private var adapter:CredentialsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.parseColor("#20111111");
        setContentView(R.layout.activity_main)

        val credentials = intent.getStringExtra("Credentials")
        if (credentials!=null) {
            decodeCredentials(credentials)
            prompt.visibility = View.GONE
        }

        key_btn.setOnClickListener {
            val keyFragment = KeyFragment()
            keyFragment.show(supportFragmentManager, keyFragment::class.java.simpleName)
        }
        add.setOnClickListener {
            val addFragment = AddFragment(credentials)
            addFragment.show(supportFragmentManager, addFragment::class.java.simpleName)
        }
    }

    fun decodeCredentials(credentials: String){

        val list = credentials.split('*')
        for(position in 1..list.size-3 step 3){
            credentialsList.add(Credentials(list[position], list[position + 1], list[position + 2]))
        }

        adapter = CredentialsAdapter(baseContext, credentialsList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(baseContext)

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    var deletedFlag: Credentials? = null

    private var simpleCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                deletedFlag = credentialsList[position]
                credentialsList.removeAt(position)
                adapter!!.notifyItemRemoved(position)
                //tinyDB.putListString("Links", flags)
                Snackbar.make(
                    recyclerView,
                    deletedFlag!!.appName,
                    BaseTransientBottomBar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        credentialsList.add(position, deletedFlag!!)
                        adapter!!.notifyItemInserted(position)
                        //tinyDB.putListString("Links", flags)
                    }.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.black
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.delete)
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
}