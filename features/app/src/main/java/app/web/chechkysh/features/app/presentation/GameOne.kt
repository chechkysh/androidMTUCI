package app.web.chechkysh.features.app.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import app.web.chechkysh.core.designsystem.gone
import app.web.chechkysh.core.designsystem.viewBinding
import app.web.chechkysh.core.designsystem.visible
import app.web.chechkysh.features.app.R
import app.web.chechkysh.features.app.databinding.GameOneFragmentBinding
import app.web.chechkysh.features.app.databinding.ProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class GameOne : Fragment(R.layout.game_one_fragment) {

    private val binding by viewBinding(GameOneFragmentBinding::bind)
    private lateinit var prefManager: ProfilePrefManager
    private lateinit var supabaseClient: SupabaseClient


    private fun checkButton() {
        binding.checkButton.setOnClickListener {
            if (binding.animalText.text.toString().toLowerCase() == prefManager.pastAnimalGame) {
                binding.gameWin.visible()
                binding.gameLoose.gone()
                binding.gameOne.gone()
                prefManager.isStreak += 1
                prefManager.points += 1f
                if (prefManager.isStreak >= 2f) {
                    prefManager.points += (0.2f * prefManager.isStreak)
                }
                setPoints()
            } else {
                prefManager.isStreak = 0
                binding.looseText.text =
                    getString(R.string.that_is) + " " + prefManager.pastAnimalGame
                binding.gameWin.gone()
                binding.gameLoose.visible()
                binding.gameOne.gone()
            }
        }
    }

    private fun nextClick() {
        binding.looseNextButton.setOnClickListener {
            binding.gameWin.gone()
            binding.gameLoose.gone()
            binding.gameOne.visible()
            binding.animalText.setText("")
            binding.animalImage.setImageResource(android.R.color.transparent)
            downloadAllAnimal()
        }
        binding.looseTryAgainButton.setOnClickListener {
            binding.gameWin.gone()
            binding.gameLoose.gone()
            binding.animalText.setText("")
            binding.gameOne.visible()
        }
        binding.winButton.setOnClickListener {
            binding.gameWin.gone()
            binding.gameLoose.gone()
            binding.gameOne.visible()
            binding.animalText.setText("")
            binding.animalImage.setImageResource(android.R.color.transparent)
            downloadAllAnimal()
        }
    }

    private fun setPoints() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                supabaseClient.from("users").delete {
                    filter {
                        Users::email eq prefManager.email
                    }
                }
                val city = Users(email = prefManager.email!!, points = prefManager.points)
                supabaseClient.from("users").insert(city)
            } catch (e: Exception) {

            }
        }
    }

    private fun backButton() {
        binding.back.setOnClickListener {
            prefManager.isStreak = 0
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/main".toUri())
                .build()
            findNavController().navigate(request)
        }
        binding.backLoose.setOnClickListener {
            prefManager.isStreak = 0
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/main".toUri())
                .build()
            findNavController().navigate(request)
        }
        binding.backWin.setOnClickListener {
            prefManager.isStreak = 0
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/main".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    private fun downloadAllAnimal() {
        CoroutineScope(Dispatchers.Main).launch {
            try {

                val animal = supabaseClient.from("game1").select().decodeList<Animal>()

                val randomAnimal = animal.random()
                prefManager.pastAnimalGame = randomAnimal.animal

                downloadAndDisplayProfilePicture(randomAnimal.animal)
            } catch (e: Exception) {
            }
        }
    }

    private fun downloadAndDisplayProfilePicture(animal: String) {
        val filename = "$animal.jpg"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageData = supabaseClient.storage
                    .from("animal_image")
                    .downloadPublic(filename)

                if (imageData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    withContext(Dispatchers.Main) {
                        binding.animalImage.setImageBitmap(bitmap)
                    }
                } else {
                    // Handle case where image is not found
                    // (e.g., display default image)
                }
            } catch (e: Exception) {

            }
        }
    }

}
