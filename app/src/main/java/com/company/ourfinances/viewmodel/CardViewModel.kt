package com.company.ourfinances.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.repository.CardRepository

class CardViewModel(application: Application) : AndroidViewModel(application) {

    private val cardRepository = CardRepository(application.applicationContext)

    private val _cardRecordList = MutableLiveData<List<CardEntity>>()
    val cardRecordList: LiveData<List<CardEntity>> = _cardRecordList

    fun insertCard(cardEntity: CardEntity){
        cardRepository.insert(cardEntity)
    }

    fun getAllCards(){
        _cardRecordList.value = cardRepository.getAll()
    }

    fun deleteCard(id:Long) {
        cardRepository.deleteCard(id)
    }

}