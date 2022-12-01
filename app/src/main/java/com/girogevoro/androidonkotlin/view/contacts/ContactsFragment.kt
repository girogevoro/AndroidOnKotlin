package com.girogevoro.androidonkotlin.view.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.girogevoro.androidonkotlin.databinding.FragmentContactsBinding

class ContactsFragment() : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!

    private val contactsAdapter by lazy {
        ContactsAdapter()
    }

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this).get(ContactsViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactsFragmentRecyclerview.adapter = contactsAdapter
        viewModel.contactsLiveData.observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModel.setListContacts(listOf("ere", "rty", "yty"))
    }

    private fun renderData(list: List<String>?) {
        list?.let {
            contactsAdapter.setData(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

}