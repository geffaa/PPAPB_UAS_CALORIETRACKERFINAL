package com.example.ppapb_uas.Welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.FragmentWelcomeBinding

// Fragment untuk menampilkan tampilan selamat datang (onboarding) satu per satu.
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class WelcomeFragment : Fragment() {
    private lateinit var title: String
    private lateinit var description: String
    private var imageResource = 0
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvDescription: AppCompatTextView
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengambil argumen yang dikirimkan saat membuat instance fragment.
        if (arguments != null) {
            title = requireArguments().getString(ARG_PARAM1)!!
            description = requireArguments().getString(ARG_PARAM2)!!
            imageResource = requireArguments().getInt(ARG_PARAM3)
        }
    }

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root
        // Menginisialisasi tampilan dan mengatur teks serta gambar berdasarkan argumen.
        tvTitle = binding.textOnboardingTitle
        tvDescription = binding.textOnboardingDescription
        image = binding.imageOnboarding
        tvTitle.text = title
        tvDescription.text = description
        image.setImageResource(imageResource)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Companion object untuk membuat instance fragment dengan argumen.
    companion object {
        fun newInstance(
            title: String,
            description: String,
            imageResource: Int
        ): WelcomeFragment {
            val fragment = WelcomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, title)
            args.putString(ARG_PARAM2, description)
            args.putInt(ARG_PARAM3, imageResource)
            fragment.arguments = args
            return fragment
        }
    }
}
