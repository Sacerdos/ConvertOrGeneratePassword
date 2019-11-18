package com.indychkov.convertorgeneratepassword;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView resultTextView;
    private EditText sourceTextView;
    private View copyButton;
    private ImageView quality;
    private PasswordHelper helper;
    private TextView qualityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new PasswordHelper(getResources().getStringArray(R.array.russian),
                getResources().getStringArray(R.array.english));
        resultTextView = findViewById(R.id.result_textview);
        sourceTextView = findViewById(R.id.source_text);
        copyButton = findViewById(R.id.button_copy);
        copyButton.setEnabled(false);
        quality = findViewById(R.id.quality_indicator);
        qualityText = findViewById(R.id.quality_descr);
        sourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultTextView.setText(helper.convert(s));
                copyButton.setEnabled(s.length() > 7);
                quality.setImageLevel(helper.getQuality(s) * 1000);
                qualityText.setText(getResources().getStringArray(R.array.password_strength)[helper.getQuality(s)]);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title),
                        resultTextView.getText()
                ));
                Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
}
