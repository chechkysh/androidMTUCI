package app.web.chechkysh.features.splash.presentation

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import app.web.chechkysh.core.designsystem.setOnReactiveClickListener
import app.web.chechkysh.core.designsystem.viewBinding
import app.web.chechkysh.features.splash.R
import app.web.chechkysh.features.splash.databinding.SplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : Fragment(R.layout.splash) {

    private val binding by viewBinding(SplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/onboarding".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

}