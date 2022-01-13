package com.example.socialmedia.addPostFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.SubFragmentPostPreview2Binding
import org.json.JSONArray
import org.json.JSONObject

class PostPreview2Fragment : Fragment() {

    private var _binding: SubFragmentPostPreview2Binding? = null
    private val b get() = _binding!!
    private var contentType: String = GLOBALS.CONTENT_POST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_fragment_post_preview_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SubFragmentPostPreview2Binding.bind(view)

        b.btnSelectorPost.setOnClickListener {
            contentType = GLOBALS.CONTENT_POST
            b.layoutSheetAndShop.visibility = View.VISIBLE
            b.layoutShop.visibility = View.GONE
            b.labelContentType.text = getString(R.string.new_post)
        }
        b.btnSelectorSheet.setOnClickListener {
            contentType = GLOBALS.CONTENT_SHEET
            b.layoutSheetAndShop.visibility = View.VISIBLE
            b.layoutShop.visibility = View.GONE
            b.labelContentType.text = getString(R.string.new_sheet)
        }
        b.btnSelectorShop.setOnClickListener {
            contentType = GLOBALS.CONTENT_SHOP
            b.layoutSheetAndShop.visibility = View.GONE
            b.layoutShop.visibility = View.VISIBLE
            b.labelContentType.text = getString(R.string.new_shop)
        }
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
        json.put("contentType",contentType)
        json.put("hashtag",JSONArray(hashtag.toList()))
        json.put("tag",JSONArray(tag.toList()))

        if(contentType != GLOBALS.CONTENT_SHOP){
            json.put("description", b.descriptionET.text.toString())
            json.put("author",b.authorET.text.toString())
            json.put("title",b.titleET.text.toString())
        } else {
            json.put("instrument_description",b.instrumentDescriptionET.text.toString())
            json.put("price",b.priceET.text.toString().toFloatOrNull())
        }

        return json
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}