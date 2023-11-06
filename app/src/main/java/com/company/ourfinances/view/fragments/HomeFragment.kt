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
import com.company.ourfinances.model.enums.EnumUtils
import com.company.ourfinances.model.enums.RegisterTypeEnum
import com.company.ourfinances.view.ShowRecordListActivity
import com.company.ourfinances.view.adapters.HomeComponentAdapter
import com.company.ourfinances.view.utils.HomeComponent
import com.company.ourfinances.view.listener.OnComponentHomeListener

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeComponentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var componentsList: ArrayList<HomeComponent>

    lateinit var imageId: Array<Int>
    lateinit var textDescription: Array<String>
    lateinit var textTitle: Array<String>

    lateinit var receita: String
    lateinit var despesa: String
    lateinit var transferencia: String


    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        receita = EnumUtils.getRegisterType(RegisterTypeEnum.RECEITA, context)
        despesa = EnumUtils.getRegisterType(RegisterTypeEnum.DESPESA, context)
        transferencia = EnumUtils.getRegisterType(RegisterTypeEnum.TRANSFERENCIA, context)

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
                    receita -> {
                        bundle.putString(getString(R.string.fragmentIdentifier), receita)
                    }

                    despesa -> {
                        bundle.putString(getString(R.string.fragmentIdentifier), despesa)
                    }

                    transferencia -> {
                        bundle.putString(getString(R.string.fragmentIdentifier), transferencia)
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
            despesa,
            receita,
            transferencia
        )

        for (item in imageId.indices) {
            val component = HomeComponent(textTitle[item], textDescription[item], imageId[item])
            componentsList.add(component)
        }
    }

}