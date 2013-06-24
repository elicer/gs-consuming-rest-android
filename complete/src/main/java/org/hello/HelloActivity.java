package org.hello;

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
        final String url = "http://search.twitter.com/search.json?q={query}";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Page page = restTemplate.getForObject("http://graph.facebook.com/gopivotal", Page.class);
        TextView textView = (TextView) this.findViewById(R.id.text_view);
        textView.setText(page.getName());
    }

}
