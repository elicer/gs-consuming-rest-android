package org.hello;

import org.hello.Page;
import org.hello.R;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    public void onStart() {
        super.onStart();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Page page = restTemplate.getForObject("http://graph.facebook.com/gopivotal", Page.class);

        TextView textView = (TextView) this.findViewById(R.id.name);
        textView.setText(page.getName());

        textView = (TextView) this.findViewById(R.id.about);
        textView.setText(page.getAbout());

        textView = (TextView) this.findViewById(R.id.phone);
        textView.setText(page.getPhone());

        textView = (TextView) this.findViewById(R.id.website);
        textView.setText(page.getWebsite());
    }

}
