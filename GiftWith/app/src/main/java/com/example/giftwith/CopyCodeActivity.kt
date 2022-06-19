package com.example.giftwith

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityCopyCodeBinding
import com.example.giftwith.databinding.ActivityEditGroupBinding

class CopyCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCopyCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCopyCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = intent
        val gid = intent.getStringExtra("GID")
        binding.groupCode.setText(gid)
    }

    fun copyContent(view: View){
        val code = binding.groupCode.text.toString()
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", code)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this,"Code coppied",Toast.LENGTH_LONG).show()
        val intent = Intent(this,EditGroupActivity::class.java)
        startActivity(intent)
        finish()
    }
}