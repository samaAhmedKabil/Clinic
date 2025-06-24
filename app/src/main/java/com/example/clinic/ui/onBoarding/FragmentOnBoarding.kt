package com.example.clinic.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.clinic.R
import com.example.clinic.databinding.OnBoardingFragmentBinding

class FragmentOnBoarding: Fragment() {

    private var _binding: OnBoardingFragmentBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.on_boarding_fragment , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = OnBoardingFragmentBinding.bind(view)
        val fragments = listOf(
            OnBoardingOne(),
            OnBoardingTwo()
        )

        binding.onboardingViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragments.size
            override fun createFragment(position: Int) = fragments[position]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}