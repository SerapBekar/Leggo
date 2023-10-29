package com.serapbekar.fprojectsb.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.CreditCardFormatter
import com.serapbekar.fprojectsb.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding : FragmentPaymentBinding

    private val monthList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val yearList = listOf(23, 24, 25, 26)

    private var monthValue = 0
    private var yearValue = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentMonth()
        paymentYear()
        paymentCreditCart()

        with(binding){
            btnPay.setOnClickListener {
                if(etName.text.isNotEmpty() && etAdresses.text.isNotEmpty() && etCvc.text.isNotEmpty() && checkMonth(monthValue) && checkYear(yearValue)){
                    findNavController().navigate(R.id.action_paymentFragment_to_paymentSuccessFragment)
                }else{
                    Toast.makeText(
                        requireContext(),
                        "Please! Enter the required information in the fields.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.paymentFragment)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun paymentYear() = with(binding) {
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, yearList)
        actYear.setAdapter(yearAdapter)

        actYear.setOnItemClickListener { _, _, position, _ ->
            yearValue = yearList[position]
        }
    }

    private fun paymentMonth() = with(binding) {
        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, monthList)
        actMonth.setAdapter(monthAdapter)

        actMonth.setOnItemClickListener { _, _, position, _ ->
            monthValue = monthList[position]
        }
    }


    private fun paymentCreditCart() = with(binding) {
        etCreditCart.addTextChangedListener(CreditCardFormatter())
    }

    private fun checkMonth(value: Int) : Boolean {
        return value in monthList
    }

    private fun checkYear(value: Int) : Boolean {
        return value in yearList
    }
}