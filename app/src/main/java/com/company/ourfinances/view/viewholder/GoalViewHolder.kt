package com.company.ourfinances.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.databinding.GoalItemBinding
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.view.listener.OnGoalListener

class GoalViewHolder(
    private val bind: GoalItemBinding,
    private val listener: OnGoalListener
) :
    RecyclerView.ViewHolder(bind.root) {
        fun bind(goal: GoalEntity){
            bind.textGoalName.text = goal.name
            bind.textGoalCurrent.text = "R$ ${goal.currentValue.toString()}"
            bind.textGoalFinish.text = "R$ ${goal.finalValue.toString()}"
            bind.textGoalDate.text = goal.limitDate
            bind.buttonEditGoal.setOnClickListener {
                listener.onClick(goal.goalId)
            }
        }

}
