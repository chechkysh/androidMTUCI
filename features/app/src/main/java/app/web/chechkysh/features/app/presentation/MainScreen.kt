package app.web.chechkysh.features.app.presentation


import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import app.web.chechkysh.core.designsystem.viewBinding
import app.web.chechkysh.features.app.R
import app.web.chechkysh.features.app.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreen : Fragment(R.layout.main_fragment) {

    private val binding by viewBinding(MainFragmentBinding::bind)
    private lateinit var prefManager: ProfilePrefManager
    private lateinit var supabaseClient: SupabaseClient

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun getAllUsersData() {
        CoroutineScope(Dispatchers.Main).launch {


            val users = supabaseClient.from("users").select().decodeList<Users>()

            val user = users.find { it.email == prefManager.email }

            if (user != null) {
                prefManager.points = user.points
            } else {
                prefManager.points = 0f
            }

            val sortedUsers = users.sortedByDescending { it.points }

            binding.firstTopEmail.text = sortedUsers[0].email
            binding.secondTopEmail.text = sortedUsers[1].email
            binding.thirdTopEmail.text = sortedUsers[2].email


            binding.firstTopPoints.text =
                "${sortedUsers[0].points} ${binding.firstTopPoints.text}"
            binding.secondTopPoints.text =
                "${sortedUsers[1].points} ${binding.secondTopPoints.text}"
            binding.thirdTopPoints.text =
                "${sortedUsers[2].points} ${binding.thirdTopPoints.text}"


        }
    }

    private fun toGame() {
        binding.firstGame.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/gameone".toUri())
                .build()
            findNavController().navigate(request)
        }

    }

    private fun toProfile() {
        binding.profileAvatar.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://app.web.chechkysh/profile".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    private fun downloadAndDisplayProfilePicture() {
        val userId = prefManager.id // Get user ID from shared preferences
        val filename = "image$userId.png"

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val imageData = supabaseClient.storage
                    .from("user-avatars")
                    .downloadPublic(filename)

                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                withContext(Dispatchers.Main) {
                    binding.profileAvatar.setImageBitmap(bitmap)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun changeTheme() {
        if (prefManager.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
