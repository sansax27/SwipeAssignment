package com.example.swipeassignment.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeassignment.R
import com.example.swipeassignment.adapter.rv.ProductListRVAdapter
import com.example.swipeassignment.data.UIStatus
import com.example.swipeassignment.databinding.FragmentAddProductBinding
import com.example.swipeassignment.databinding.FragmentProductListBinding
import com.example.swipeassignment.viewmodel.ProductListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private val binding: FragmentProductListBinding by lazy {
        FragmentProductListBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<ProductListFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.productDataLiveData.observe(this) {
            when(it) {
                is UIStatus.Success -> {
                    val rvAdapter = ProductListRVAdapter(it.data)
                    binding.productListRv.apply {
                        layoutManager = LinearLayoutManager(activity).apply {
                            orientation = LinearLayoutManager.VERTICAL
                        }
                        adapter = rvAdapter
                    }
                    binding.addProductBt.setOnClickListener {
                        findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToAddProductFragment())
                    }
                    binding.productSearchEt.doAfterTextChanged { search ->
                        if (!(search.isNullOrBlank())) {
                            val query = search.toString()
                            rvAdapter.updateList(query)
                        }
                    }
                    binding.productListRv.visibility = View.VISIBLE
                    binding.addProductBt.visibility = View.VISIBLE
                    binding.productListPb.visibility = View.GONE
                }
                is UIStatus.Loading -> {}
                is UIStatus.Empty -> {
                    Toast.makeText(activity, getString(R.string.no_product_data_string), Toast.LENGTH_LONG).show()
                    binding.productListPb.visibility = View.GONE
                }
                is UIStatus.Error -> {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    binding.productListPb.visibility = View.GONE
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

}