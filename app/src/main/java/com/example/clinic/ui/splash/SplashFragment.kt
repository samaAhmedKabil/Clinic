package com.example.clinic.ui.splash

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentSplashBinding

class SplashFragment: Fragment() {
    private var _binding: FragmentSplashBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = requireContext().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val isShown = sharedPref.getBoolean("shown", false)

            if (isShown) {
                findNavController().navigate(R.id.roleSelectionFragment)
            } else {
                findNavController().navigate(R.id.onBoardingOne)
            }
        }, 2000)
        progressBarHandler()
    }

    private fun progressBarHandler()
    {
        binding.loading.max = 1000
        val currentProgress = 1000
        ObjectAnimator.ofInt(binding.loading , "progress" , currentProgress).setDuration(3000).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}