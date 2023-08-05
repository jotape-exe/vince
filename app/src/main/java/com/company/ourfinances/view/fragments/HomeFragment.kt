package com.company.ourfinances.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.databinding.FragmentHomeBinding
import com.company.ourfinances.model.enums.TitleEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.adapters.HomeComponentAdapter
import com.company.ourfinances.view.assets.HomeComponent
import com.company.ourfinances.view.listener.OnComponentHomeListener

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeComponentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var componentsList: ArrayList<HomeComponent>

    lateinit var imageId: Array<Int>
    lateinit var textDescription: Array<String>
    lateinit var textTitle: Array<String>

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialzr()

        val layoutManager = LinearLayoutManager(context)

        recyclerView = view.findViewById(R.id.recycler_home)
        recyclerView.layoutManager = layoutManager

        adapter = HomeComponentAdapter()
        recyclerView.adapter = adapter

        adapter.updateList(componentsList)

        val listener = object : OnComponentHomeListener {
            override fun onClick(fragmentIdentifier: String) {

                val bundle = Bundle()

                when (fragmentIdentifier) {
                    TitleEnum.DESPESA.value -> {
                        bundle.putString(getString(R.string.fragmentidentifier), TitleEnum.DESPESA.value)
                    }

                    TitleEnum.RECEITA.value -> {
                        bundle.putString(getString(R.string.fragmentidentifier), TitleEnum.RECEITA.value)
                    }

                    TitleEnum.TRANSFERENCIA.value -> {
                        bundle.putString(getString(R.string.fragmentidentifier), TitleEnum.TRANSFERENCIA.value)
                    }
                }

                activity!!.startActivity(
                    Intent(
                        activity!!.applicationContext,
                        ShowRecordListActivity::class.java
                    ).putExtras(bundle)
                )
            }
        }

        adapter.attachToListener(listener)

    }

    private fun dataInitialzr() {
        componentsList = arrayListOf()

        imageId = arrayOf(
            R.drawable.ic_expense,
            R.drawable.ic_money,
            R.drawable.ic_transfer
        )

        textDescription = arrayOf(
            getString(R.string.show_expense),
            getString(R.string.show_revenue),
            getString(R.string.show_trasnfer)
        )

        textTitle = arrayOf(
            TitleEnum.DESPESA.value,
            TitleEnum.RECEITA.value,
            TitleEnum.TRANSFERENCIA.value
        )

        for (item in imageId.indices) {
            val component = HomeComponent(textTitle[item], textDescription[item], imageId[item])
            componentsList.add(component)
        }
    }

}