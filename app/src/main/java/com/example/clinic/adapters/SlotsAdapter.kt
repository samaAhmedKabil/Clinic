import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic.R

class SlotsAdapter(
    private var slots: List<String>,
    private var takenSlots: List<String> = emptyList(),
    private val onSlotSelected: (String) -> Unit
) : RecyclerView.Adapter<SlotsAdapter.SlotViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSlot: TextView = itemView.findViewById(R.id.tv_slot)
        val cardSlot: CardView = itemView.findViewById(R.id.card_slot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.tvSlot.text = slot

        val isTaken = takenSlots.contains(slot)

        // Set visual appearance for taken/free slots
        if (isTaken) {
            // Taken slot appearance (gray background, gray text)
            holder.cardSlot.setCardBackgroundColor(Color.parseColor("#D8D8D8"))
            holder.tvSlot.setTextColor(Color.DKGRAY)
        } else {
            // Free slot appearance
            if (selectedPosition == position) {
                holder.cardSlot.setCardBackgroundColor(Color.parseColor("#FFE6F1"))
                holder.tvSlot.setTextColor(Color.BLACK)
            } else {
                holder.cardSlot.setCardBackgroundColor(Color.WHITE)
                holder.tvSlot.setTextColor(Color.BLACK)
            }
        }

        // Enable click only if slot is free
        holder.itemView.isEnabled = !isTaken

        holder.itemView.setOnClickListener {
            if (isTaken) return@setOnClickListener  // ignore clicks on taken slots

            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onSlotSelected(slot)
        }
    }

    fun updateSlots(newSlots: List<String>, newTakenSlots: List<String> = emptyList()) {
        this.slots = newSlots
        this.takenSlots = newTakenSlots
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = slots.size
}
