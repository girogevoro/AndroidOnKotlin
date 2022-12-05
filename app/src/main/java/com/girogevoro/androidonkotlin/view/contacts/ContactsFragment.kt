package com.girogevoro.androidonkotlin.view.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.girogevoro.androidonkotlin.databinding.FragmentContactsBinding

const val REQUEST_CODE = 42

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

        checkPermission()
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


    // Проверяем, разрешено ли чтение контактов
    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
//Доступ к контактам на телефоне есть
                    getContacts()
                }
//Опционально: если нужно пояснение перед запросом разрешений
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Объяснение")

                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Не надо") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
//Запрашиваем разрешение
                    requestPermission()
                }
            }
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getContacts()
            } else {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("список пуст. нужно разрешение на доступ к контактам")
                        .setNegativeButton("Закрыть") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val list: MutableList<String> = mutableListOf()

        context?.let {
// Получаем ContentResolver у контекста
            val contentResolver: ContentResolver = it.contentResolver
// Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
// Переходим на позицию в Cursor
                    if (cursor.moveToPosition(i)) {
// Берём из Cursor столбец с именем
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        list.add(name)
                    }
                }
            }
            cursorWithContacts?.close()
        }
        viewModel.setListContacts(list)
    }

}