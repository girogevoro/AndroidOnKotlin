package com.girogevoro.androidonkotlin.view.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.girogevoro.androidonkotlin.databinding.FragmentContactsRecyclerItemBinding

class ContactsAdapter() : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    var list = listOf<String>()

    fun setData(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = FragmentContactsRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ContactsViewHolder(private val binding: FragmentContactsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) {
            binding.name.text = name
        }
    }
}
