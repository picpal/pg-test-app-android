package com.example.pgdummyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.pgdummyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val spinnerItems = listOf("(개발) 표준 결제창", "(개발) 빌링 카드 등록", "(운영) 표준 결제창", "(운영) 빌링 카드 등록", "직접 입력")
    private val urls = listOf(
        "https://devpg.bluewalnut.co.kr/dlp/cnspayRequestM.jsp",
        "https://devpg.bluewalnut.co.kr/bill/hpayBillCardReqM.jsp",
        "https://pg.bluewalnut.co.kr/dlp/cnspayRequestM.jsp",
        "https://pg.bluewalnut.co.kr/bill/hpayBillCardReqM.jsp",
        ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.editText.setText(urls[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonWebView.setOnClickListener {
            onClickWebViewButton()
        }
    }

    private fun onClickWebViewButton() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", binding.editText.text.toString())
        startActivity(intent)
    }
}