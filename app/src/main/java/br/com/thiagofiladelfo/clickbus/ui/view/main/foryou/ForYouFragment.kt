package br.com.thiagofiladelfo.clickbus.ui.view.main.foryou

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thiagofiladelfo.clickbus.databinding.ForYouFragmentBinding
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment

class ForYouFragment : BaseFragment() {

    private var _binding: ForYouFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForYouFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}