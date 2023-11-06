package com.company.ourfinances.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.company.ourfinances.R
import com.company.ourfinances.databinding.GoalItemBinding
import com.company.ourfinances.model.entity.GoalEntity
import com.company.ourfinances.view.listener.OnGoalListener
import kotlin.math.roundToInt

class GoalViewHolder(
    private val bind: GoalItemBinding,
    private val listener: OnGoalListener
) :
    RecyclerView.ViewHolder(bind.root) {
        fun bind(goal: GoalEntity){
            bind.textGoalName.text = goal.name
            bind.textGoalCurrent.text = "${itemView.context.getString(R.string.coin)} ${goal.currentValue.toString()}"
            bind.textGoalFinish.text = "${itemView.context.getString(R.string.coin)} ${goal.finalValue.toString()}"
            bind.textGoalDate.text = goal.limitDate
            bind.buttonEditGoal.setOnClickListener {
                listener.onClick(goal.goalId)
            }
            bind.buttonDeleteGoal.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle(itemView.context.getString(R.string.delete_goal))
                    .setMessage(itemView.context.getString(R.string.reallt_delete_goal))
                    .setPositiveButton(itemView.context.getString(R.string.yes)) { dialog, which ->
                        listener.onDelete(goal.goalId)
                    }
                    .setNeutralButton(itemView.context.getString(R.string.cancel), null)
                    .create()
                    .show()
            }
            bind.progressGoal.progress = getProgress(goal.currentValue, goal.finalValue).toInt()
            bind.textPercentage.text = "${getProgress(goal.currentValue, goal.finalValue).roundToInt()}%"
        }

    private fun getProgress(currentValue: Double, finalValue:Double): Double{
        return (currentValue / finalValue) * 100
    }

}
