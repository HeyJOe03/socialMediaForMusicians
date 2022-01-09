package com.example.socialmedia.addPostFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentAddPostBinding
import com.example.socialmedia.databinding.PostPreview2Binding
import org.json.JSONArray
import org.json.JSONObject

class PostPreview2Fragment : Fragment() {

    private var _binding: PostPreview2Binding? = null
    private val b get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_preview_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = PostPreview2Binding.bind(view)
        // setResult("requestKey", bundleOf("bundleKey" to result))
    }

    fun getData(): JSONObject{

        val hashtag: MutableList<String> = b.hashtagET.text.toString().split(" ").toMutableList()
        val tag: MutableList<String> = b.tagET.text.toString().split(" ").toMutableList()

        hashtag.removeIf { !it.contains('#')}

        for (i in hashtag.indices) {
            hashtag[i] = hashtag[i].filterNot { it == '#' }
            hashtag[i] = hashtag[i].filterNot { it == ' ' }
            //hashtag[i] = "\"${hashtag[i]}\""
        }

        tag.removeIf { !it.contains('@') }

        for (i in tag.indices) {
            tag[i] = tag[i].filterNot { it == '@' }
            tag[i] = tag[i].filterNot { it == ' ' }
            //tag[i] = "\"${tag[i]}\""
        }

        val json: JSONObject = JSONObject()
        json.put("hashtag",JSONArray(hashtag.toList()))
        json.put("tag",JSONArray(tag.toList()))
        json.put("description", b.descriptionET.text.toString())
        json.put("author",b.authorET.text.toString())
        json.put("title",b.titleET.text.toString())

        return json
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}