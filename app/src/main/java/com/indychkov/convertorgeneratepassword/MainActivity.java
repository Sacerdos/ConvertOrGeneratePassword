package com.indychkov.convertorgeneratepassword;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView resultTextView;
    private EditText sourceTextView;
    private View copyButton;
    private ImageView quality;
    private PasswordHelper helper;
    private TextView qualityText;

    private TextView generatedResultTextView;
    private View generateButton;
    private SeekBar seekbarLengthGenerate;
    private TextView sizeDescription;
    private CompoundButton isCapitalize, isNumber, isSymbol;
    private final int MIN_LENGTH = 8;

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
                int qualityRate = helper.getQuality(s.toString());
                quality.setImageLevel(qualityRate * 1000);
                qualityText.setText(getResources().getStringArray(R.array.password_strength)[qualityRate]);
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
        generatedResultTextView = findViewById(R.id.result_generated_textview);
        generateButton = findViewById(R.id.button_copy_generated);
        seekbarLengthGenerate = findViewById(R.id.length_generate);
        sizeDescription = findViewById(R.id.password_size_descr);
        isCapitalize = findViewById(R.id.is_capitalize);
        isNumber = findViewById(R.id.is_number);
        isSymbol = findViewById(R.id.is_symbol);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultGenerate = helper.generatePassword(
                        MIN_LENGTH + seekbarLengthGenerate.getProgress(),
                        isCapitalize.isChecked(),
                        isNumber.isChecked(),
                        isSymbol.isChecked());
                generatedResultTextView.setText(resultGenerate);
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title_generated),
                        generatedResultTextView.getText()
                ));
                Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        seekbarLengthGenerate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeDescription.setText(
                        getString(R.string.length_format,
                                progress,
                                getResources().getQuantityString(R.plurals.symbols_count, progress),
                                MIN_LENGTH + progress,
                                getResources().getQuantityString(R.plurals.symbols_count, MIN_LENGTH + progress)
                        ));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
