package com.company.ourfinances.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.model.repository.GoalRepository

class GoalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GoalRepository(application.applicationContext)

    private var _goalList = MutableLiveData<List<GoalEntity>>()
    private val goalList: LiveData<List<GoalEntity>> = _goalList

    private var _goal = MutableLiveData<GoalEntity>()
    private val goal: LiveData<GoalEntity> = _goal

    fun save(goal: GoalEntity){
        if (goal.goalId == 0L){
            repository.save(goal)
        } else{
            repository.update(goal)
        }
    }

    fun findAll(){
        _goalList.value = repository.findAll()
    }

    fun delete(id: Long){
        repository.delete(id)
    }

    fun finGoalById(id: Long){
        _goal.value = repository.findById(id)
    }
}