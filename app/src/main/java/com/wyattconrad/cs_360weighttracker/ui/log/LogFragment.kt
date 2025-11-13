package com.wyattconrad.cs_360weighttracker.ui.log

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wyattconrad.cs_360weighttracker.R
import com.wyattconrad.cs_360weighttracker.adapter.WeightAdapter
import com.wyattconrad.cs_360weighttracker.databinding.FragmentLogBinding
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.LoginService
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel

class LogFragment : Fragment() {
    private var adapter: WeightAdapter? = null
    private var binding: FragmentLogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogBinding.inflate(inflater, container, false)

        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Use the ViewModel
        val loginService: LoginService?

        // Get the user's ID from SharedPreferences, if one doesn't exist, set it to -1
        loginService = LoginService(requireContext())
        val userId = loginService.getUserId()

        val weightListViewModel = ViewModelProvider(this).get<WeightListViewModel>(WeightListViewModel::class.java)

        // Initialize the recycler view
        val recyclerView: RecyclerView?

        // Set up the recycler view to display the recorded weights list
        recyclerView = binding!!.listArea
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))

        // Set up the adapter for the recycler view
        adapter = WeightAdapter(Application())
        recyclerView.setAdapter(adapter)

        // Get the user's weights from the view model
        weightListViewModel.getWeightByUserId(userId)

        // Observe the LiveData and update the adapter when the data changes
        weightListViewModel.getWeightByUserId(userId).observe(getViewLifecycleOwner(), Observer { weights: MutableList<Weight?>? ->
            // Update the adapter with the user's weights
            if (weights != null && !weights.isEmpty()) {
                adapter!!.setWeightList(weights)
            } else {
                adapter!!.setWeightList(ArrayList<Weight?>())
            }
        })

        // Set up the FAB click listener
        setFABClickListener(view)
    }

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }

        private fun setFABClickListener(root: View) {
            // Get the FAB from the layout
            val fab = root.findViewById<FloatingActionButton>(R.id.fab)
            // Set the click listener for the FAB
            fab.setOnClickListener(View.OnClickListener { view: View? ->
                // Navigate to the AddWeightFragment when the FAB is clicked
                val navController = findNavController(view!!)
                navController.navigate(R.id.navigation_addweight)
            })
        }
    }
}