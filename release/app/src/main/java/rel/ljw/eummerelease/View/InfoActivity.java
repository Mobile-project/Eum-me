package rel.ljw.eummerelease.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import rel.ljw.eummerelease.R;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false).setImage(R.drawable.cow)
                .setDescription("음메\n텍스트와 음성과 함께 기록하고 저장하세요\n개발자: 이형욱 이준호 이재현\nGachon Univ.\nSoftware Dept.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("Bigleeuk@naver.com")
                .addEmail("taristmas@gmail.com")
                .addEmail("izg0820@naver.com")
                .addPlayStore("jwh.com.eumme")
                .addGitHub("Mobile-project/Eum-me").create();
        setContentView(aboutPage);
    }
}