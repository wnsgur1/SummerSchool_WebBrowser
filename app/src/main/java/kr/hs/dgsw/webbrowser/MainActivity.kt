package kr.hs.dgsw.webbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import kr.hs.dgsw.webbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.apply{
            settings.javaScriptEnabled = true
            webViewClient = object:WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.urlEditText.setText(url)
                }
            }
        }
        // webView에 ()에 있는 url로드
        binding.webView.loadUrl("https://www.google.com")
        // urlEditText에 글자가 하나라도 들어오면 실행
        binding.urlEditText.setOnEditorActionListener{ _, actionId, _ ->
            // 만약 검색버튼이 클릭되면
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                // urlEditText에 적힌 글자를 로드 하겠다
                binding.webView.loadUrl(binding.urlEditText.text.toString())
                true
            }
            else{
                false
            }
        }
        // textMenu를 webView에 binding(불러겠다)
        registerForContextMenu(binding.webView)
    }

    // 뒤로가기 함수
    override fun onBackPressed() {
        // 만약 뒤로가기 버튼을 눌렀는가?
        if(binding.webView.canGoBack()){
            // 눌렀으면 이전 webView로 이동
            binding.webView.goBack()
        }
        else{
            super.onBackPressed()
        }

    }

    // mainMenu 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // mainMenu ItemSlected에 기능 추가
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 홈, 구글, 네이버, 다음, 전화하기, 이메일, 메세지 보내는 기능을 포함하는 메뉴를 보여줌
        when(item.itemId){
            // 구글 버튼이나 홈 버튼을 누르먄 webView가 구글로 변함
            R.id.action_google, R.id.action_home -> {
                binding.webView.loadUrl("https://www.google.com")
                return true
            }
            // 네이버 버튼을 누르면 webView가 네이버로 변함
            R.id.action_naver -> {
                binding.webView.loadUrl("https://www.naver.com")
                return true
            }
            // 다음 버튼을 누르면 webView가 다음로 변함
            R.id.action_daum -> {
                binding.webView.loadUrl("https://www.daum.net")
                return true
            }

            R.id.action_call -> {
                // Intent.ACTION_DIAL기능을 선언
                val intent = Intent(Intent.ACTION_DIAL)
                // "tel:***-***-****"  *자리의 들어가는 번호에 전화를 검
                intent.data = Uri.parse("tel:053-123-4567")
                if (intent.resolveActivity(packageManager)!=null){
                    startActivity(intent)
                }
                return true
            }

            R.id.action_send_text -> {
                // senSMS함수에 "053-123-4567"와 해당 url을 넘겨줌
                binding.webView.url?.let { url -> sendSMS("053-123-4567", url)
                }
                return true

            }

            // email함수에 이메일 주소와 제목, 해당 url을 넘겨줌
            R.id.action_email -> {
                binding.webView.url?.let { url ->  email("text@text.com", "good siete", url)

                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    // contextMenu 생성
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?)

    {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    // ContextItemSelected에 기능 추가
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share -> {binding.webView.url?.let { url -> share(url) }
                return true
            }
            R.id.action_browser -> {
                binding.webView.url?.let{url -> browse(url)}
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

}