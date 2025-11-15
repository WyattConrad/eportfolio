package com.wyattconrad.cs_360weighttracker.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wyattconrad.cs_360weighttracker.R


class LogFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: LogViewModel by viewModels {
        LogViewModel.Factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView.setContent {

            val state by viewModel.state.collectAsState()

            LogScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigate = {
                    // navigate to AddEditWeightFragment
                    findNavController().navigate(
                        R.id.action_logFragment_to_addEditWeightFragment
                    )
                }
            )
        }

    }

}